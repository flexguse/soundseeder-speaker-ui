/**
 * 
 */
package de.flexguse.soundseeder;

import java.io.IOException;
import java.util.Properties;

import javax.validation.Validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.vaadin.spring.events.annotation.EnableEventBus;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;
import org.vaadin.spring.i18n.annotation.EnableI18N;

import de.flexguse.soundseeder.model.GitRepositoryState;
import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.AudioInterfaceService;
import de.flexguse.soundseeder.service.ConfigurationService;
import de.flexguse.soundseeder.service.ConfigurationServiceException;
import de.flexguse.soundseeder.service.NetworkInterfaceService;
import de.flexguse.soundseeder.service.SoundSeederService;
import de.flexguse.soundseeder.service.impl.AudioInterfaceServiceImpl;
import de.flexguse.soundseeder.service.impl.JsonConfigurationService;
import de.flexguse.soundseeder.service.impl.NetworkInterfaceServiceImpl;
import de.flexguse.soundseeder.service.impl.SoundSeederServiceImpl;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableI18N
@EnableEventBus
public class SoundSeederSpeakerApplicationConfiguration implements InitializingBean {

	/**
	 * The spring framework factory which provides the JSR303 validator. Use
	 * something like
	 * 
	 * <pre>
	 * &#064;Autowired
	 * private Validator validator;
	 * </pre>
	 * 
	 * for getting an {@link Validator} instance autowired.
	 * 
	 * @return
	 */
	@Bean
	public SpringValidatorAdapter validator() {

		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		return localValidatorFactoryBean;

	}

	/**
	 * This spring framework bean postprocessor enables JSR303 bean validation
	 * for method arguments.
	 * 
	 * <p>
	 * Must only be configured and is used automatically by spring.
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		final MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
		methodValidationPostProcessor.setValidator(validator());

		return methodValidationPostProcessor;
	}

	@Bean
	public AudioInterfaceService audioInterfaceService() {
		return new AudioInterfaceServiceImpl();
	}

	@Bean
	public NetworkInterfaceService networkInterfaceService() {
		return new NetworkInterfaceServiceImpl();
	}

	@Bean
	public MessageProvider labelsProvider() {
		return new ResourceBundleMessageProvider("locale.speaker");
	}

	@Bean
	public SoundSeederService soundSeederService() {
		return new SoundSeederServiceImpl();
	}

	/**
	 * The service which manages the {@link SpeakerConfiguration}.
	 * 
	 * @return
	 */
	@Bean
	public ConfigurationService configurationService() {
		return new JsonConfigurationService();
	}

	/**
	 * The speaker configuration
	 * 
	 * @return
	 * @throws ConfigurationServiceException
	 */
	@Bean
	public SpeakerConfiguration speakerConfiguration() throws ConfigurationServiceException {
		return configurationService().loadConfiguration();
	}

	/**
	 * do the autostart if configured
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		SpeakerConfiguration speakerConfiguration = configurationService().loadConfiguration();
		if (speakerConfiguration != null && speakerConfiguration.getAutoplay() != null
				&& speakerConfiguration.getAutoplay()) {
			soundSeederService().listen(speakerConfiguration);
		}

	}

	/**
	 * Reads the Git properties from the properties file.
	 * 
	 * @return
	 * @throws IOException
	 */
	@Bean
	public GitRepositoryState gitRepositoryState() throws IOException {

		Properties gitProperties = new Properties();
		gitProperties.load(SoundSeederSpeakerApplicationConfiguration.class.getResourceAsStream("/git.properties"));

		return new GitRepositoryState(gitProperties);

	}

}
