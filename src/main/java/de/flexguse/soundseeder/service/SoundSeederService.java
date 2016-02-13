/**
 * 
 */
package de.flexguse.soundseeder.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.flexguse.soundseeder.model.SpeakerConfiguration;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public interface SoundSeederService {

	/**
	 * Starts listening for soundseeder audio using the given configuration.
	 * 
	 * @param configuration
	 * @return true if start listening was successful, else false
	 */
	public Boolean listen(@Valid @NotNull SpeakerConfiguration configuration);

	/**
	 * Stops listening for soundseeder audio. As there is only one instance of
	 * this service in the whole application no arguments are needed.
	 * 
	 * @return true if listening was stopped, else false
	 */
	public Boolean stopListening();

	/**
	 * Gets the listening status.
	 * 
	 * @return true if application is currently listening for soundseeder audio,
	 *         else false
	 */
	public Boolean isListening();

}
