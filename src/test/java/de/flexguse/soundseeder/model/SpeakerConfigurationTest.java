/**
 * 
 */
package de.flexguse.soundseeder.model;

import java.util.Arrays;

import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.flexguse.util.junit.ValidationViolationChecker;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpeakerConfigurationTestConfiguration.class })
public class SpeakerConfigurationTest {

	@Autowired
	private Validator validator;

	private ValidationViolationChecker<SpeakerConfiguration> checker = new ValidationViolationChecker<>();

	@Test
	public void testEmptyValidation() {
		SpeakerConfiguration conf = SpeakerConfiguration.builder().build();

		checker.checkExpectedValidationViolations(validator.validate(conf),
				Arrays.asList(SpeakerConfiguration.ERROR_MISSING_NETWORKINTERFACE,
						SpeakerConfiguration.ERROR_MISSING_CHANNEL,
						SpeakerConfiguration.ERROR_MISSING_MIXER, SpeakerConfiguration.ERROR_MISSING_NAME,
						SpeakerConfiguration.ERROR_MISSING_VOLUME));

	}
	
	@Test
	public void testTooLowVolume(){
		SpeakerConfiguration conf = SpeakerConfiguration.builder().volume(-1.0).build();

		checker.checkExpectedValidationViolations(validator.validate(conf),
				Arrays.asList(SpeakerConfiguration.ERROR_MISSING_NETWORKINTERFACE,
						SpeakerConfiguration.ERROR_MISSING_CHANNEL,
						SpeakerConfiguration.ERROR_MISSING_MIXER, SpeakerConfiguration.ERROR_MISSING_NAME,
						SpeakerConfiguration.ERROR_VOLUME_RANGE));
	}
	
	@Test
	public void testTooHighVolume(){
		SpeakerConfiguration conf = SpeakerConfiguration.builder().volume(16.0).build();

		checker.checkExpectedValidationViolations(validator.validate(conf),
				Arrays.asList(SpeakerConfiguration.ERROR_MISSING_NETWORKINTERFACE,
						SpeakerConfiguration.ERROR_MISSING_CHANNEL,
						SpeakerConfiguration.ERROR_MISSING_MIXER, SpeakerConfiguration.ERROR_MISSING_NAME,
						SpeakerConfiguration.ERROR_VOLUME_RANGE));
	}

}
