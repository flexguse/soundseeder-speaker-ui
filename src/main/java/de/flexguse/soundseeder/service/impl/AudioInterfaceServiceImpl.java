/**
 * 
 */
package de.flexguse.soundseeder.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;

import de.flexguse.soundseeder.model.SpeakerMixerInfo;
import de.flexguse.soundseeder.service.AudioInterfaceService;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class AudioInterfaceServiceImpl implements AudioInterfaceService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.flexguse.soundseeder.ui.service.AudioInterfaceService#getMixers()
	 */
	@Override
	public List<SpeakerMixerInfo> getMixers() {

		List<SpeakerMixerInfo> result = new ArrayList<>();

		/*
		 * get all mixers and check if it is a playback mixer
		 */
		Line.Info sourceDLINfo = new Line.Info(SourceDataLine.class);

		Info[] allAvailableMixers = AudioSystem.getMixerInfo();

		if (allAvailableMixers != null) {

			for (int i = 0; i < allAvailableMixers.length; i++) {

				Info info = allAvailableMixers[i];
				Mixer mixer = AudioSystem.getMixer(info);

				// only add mixer if it is able to write audio
				if (mixer.isLineSupported(sourceDLINfo)) {
					result.add(SpeakerMixerInfo.builder().description(info.getDescription()).index(i)
							.name(info.getName()).vendor(info.getVendor()).version(info.getVersion()).build());
				}

			}

		}

		return result;
	}
}
