/**
 * 
 */
package de.flexguse.soundseeder.service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import de.flexguse.soundseeder.model.SpeakerConfiguration;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Validated
public interface ConfigurationService {

	public static final String ERROR_NULL_CONFIG = "error.saving.null.configuration";

	/**
	 * Saves the given configuration.
	 * 
	 * @param speakerConfiguration
	 * @throws ConfigurationServiceException
	 */
	public void saveConfiguration(
			@NotNull(message = ERROR_NULL_CONFIG) @Valid SpeakerConfiguration speakerConfiguration)
					throws ConfigurationServiceException, ConstraintViolationException;

	/**
	 * Removes the configuration file.
	 * 
	 * @throws ConfigurationServiceException
	 */
	public void removeConfigurationFile() throws ConfigurationServiceException;

	/**
	 * Loads the speaker configuration.
	 * 
	 * @return configuration or null if no configuration is present.
	 * @throws ConfigurationServiceException
	 */
	public SpeakerConfiguration loadConfiguration() throws ConfigurationServiceException;

}
