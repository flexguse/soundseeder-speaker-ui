/**
 * 
 */
package de.flexguse.soundseeder.model.converter;

import org.springframework.core.convert.converter.Converter;

import com.soundseeder.speaker.model.ChannelConf;

import de.flexguse.soundseeder.model.SpeakerChannel;

/**
 * This converter converts a {@link SpeakerChannel} into a {@link ChannelConf}.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class SpeakerChannelToChannelConfConverter implements Converter<SpeakerChannel, ChannelConf> {

	@Override
	public ChannelConf convert(SpeakerChannel speakerChannel) {

		switch (speakerChannel) {
		case LEFT:
			return ChannelConf.Left;
		case RIGHT:
			return ChannelConf.Right;
		case MONO:
			return ChannelConf.Mono;
		default:
			return ChannelConf.Stereo;
		}
	}
}
