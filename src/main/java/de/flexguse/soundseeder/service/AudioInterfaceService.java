package de.flexguse.soundseeder.service;

import java.util.List;

import de.flexguse.soundseeder.model.SpeakerMixerInfo;

/**
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public interface AudioInterfaceService {

	/**
	 * Gets a list of audio mixers available in the system.
	 * 
	 * @return filled or empty list
	 */
	public List<SpeakerMixerInfo> getMixers();

}
