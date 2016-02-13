/**
 * 
 */
package de.flexguse.soundseeder;

import javax.validation.Validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;
import org.vaadin.spring.i18n.annotation.EnableVaadinI18N;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.AudioInterfaceService;
import de.flexguse.soundseeder.service.ConfigurationService;
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
@EnableVaadinI18N
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
	 * do the autostart if configured
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		SpeakerConfiguration speakerConfiguration = configurationService().loadConfiguration();
		if (speakerConfiguration != null && speakerConfiguration.getAutoplay() != null && speakerConfiguration.getAutoplay()) {
			soundSeederService().listen(speakerConfiguration);
		}

	}

}
