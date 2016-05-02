/**
 * 
 */
package de.flexguse.soundseeder.service.impl;

import static org.junit.Assert.assertEquals;

import java.net.SocketException;
import java.util.Arrays;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.flexguse.soundseeder.model.SpeakerChannel;
import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.ConfigurationService;
import de.flexguse.soundseeder.service.ConfigurationServiceException;
import de.flexguse.util.junit.ValidationViolationChecker;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JsonConfigurationServiceTestConfiguration.class })
public class JsonConfigurationServiceTest {

	@Autowired
	private ConfigurationService service;

	@Before
	public void setUp() throws ConfigurationServiceException {
		service.removeConfigurationFile();
	}

	@Test
	public void testLoadNotExistingConfigFile() throws ConfigurationServiceException {
		
		SpeakerConfiguration defaultConfiguration = SpeakerConfiguration.builder().autoplay(false)
				.speakerChannel(SpeakerChannel.STEREO).speakerName("soundseeder-speaker").mixerIndex(0)
				.networkInterfaceIndex(0).volume(13.0).build();
		
		assertEquals(defaultConfiguration, service.loadConfiguration());
	}

	@Test
	public void testSaveConfigFile() throws SocketException, ConfigurationServiceException {

		// create complete configuration
		SpeakerConfiguration config = SpeakerConfiguration.builder().autoplay(Boolean.TRUE).speakerName("test-speaker")
				.speakerChannel(SpeakerChannel.RIGHT).mixerIndex(5).networkInterfaceIndex(4).volume(12.0).build();

		// call service method
		service.saveConfiguration(config);

		// load config and check expectations
		assertEquals(config, service.loadConfiguration());
	}

	@Test
	public void testSaveInvalidConfigFile() throws ConfigurationServiceException {

		SpeakerConfiguration config = SpeakerConfiguration.builder().build();

		try {
			service.saveConfiguration(config);
		} catch (ConstraintViolationException e) {
			ValidationViolationChecker.checkExpectedValidationViolations(e, Arrays.asList(
					SpeakerConfiguration.ERROR_MISSING_CHANNEL,
					SpeakerConfiguration.ERROR_MISSING_MIXER, SpeakerConfiguration.ERROR_MISSING_NAME,
					SpeakerConfiguration.ERROR_MISSING_NETWORKINTERFACE, SpeakerConfiguration.ERROR_MISSING_VOLUME));
		}

	}

	@Test
	public void testSaveNullConfigFile() throws ConfigurationServiceException {
		try {
			service.saveConfiguration(null);
		} catch (ConstraintViolationException e) {
			ValidationViolationChecker.checkExpectedValidationViolations(e,
					Arrays.asList(ConfigurationService.ERROR_NULL_CONFIG));
		}

	}

}
