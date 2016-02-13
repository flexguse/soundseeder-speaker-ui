/**
 * 
 */
package de.flexguse.soundseeder.model;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public enum SpeakerChannel {

	/**
	 * the speaker is used for stereo audio
	 */
	STEREO,

	/**
	 * audio is reduced to mono, just one speaker
	 */
	MONO,

	/**
	 * this device is used as the left stereo speaker
	 */
	LEFT,

	/**
	 * this device is used as the right stereo speaker
	 */
	RIGHT

}
