/**
 * 
 */
package de.flexguse.soundseeder.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeakerConfiguration {

	public static final String ATTR_SPEAKER_NAME = "speakerName";
	public static final String ATTR_MIXER = "mixerIndex";
	public static final String ATTR_SPEAKER_CHANNEL = "speakerChannel";
	public static final String ATTR_NETWORK_INTERFACE = "networkInterfaceIndex";
	public static final String ATTR_VOLUME = "volume";
	public static final String ATTR_AUTOPLAY = "autoplay";

	public static final String ERROR_MISSING_NAME = "{error.missing.speakername}";
	public static final String ERROR_MISSING_MIXER = "{error.missing.mixer}";
	public static final String ERROR_MISSING_CHANNEL = "{error.missing.channel}";
	public static final String ERROR_MISSING_NETWORKINTERFACE = "{error.missing.networkinterface}";
	public static final String ERROR_MISSING_VOLUME = "{error.missing.volume}";
	public static final String ERROR_VOLUME_RANGE = "{error.volume.range}";

	/**
	 * The network name of this speaker device.
	 */
	@NotBlank(message = ERROR_MISSING_NAME)
	private String speakerName;

	/**
	 * The index of the Audio mixer which is used for playing audio.
	 */
	@NotNull(message = ERROR_MISSING_MIXER)
	private Integer mixerIndex;

	/**
	 * The audio channel this speaker instance is.
	 */
	@NotNull(message = ERROR_MISSING_CHANNEL)
	private SpeakerChannel speakerChannel;

	/**
	 * The network interface index which is used for listening to audio.
	 */
	@NotNull(message = ERROR_MISSING_NETWORKINTERFACE)
	private Integer networkInterfaceIndex;

	/**
	 * The volume which is used to play audio.
	 */
	@NotNull(message = ERROR_MISSING_VOLUME)
	@Min(value = 0, message = ERROR_VOLUME_RANGE)
	@Max(value = 15, message = ERROR_VOLUME_RANGE)
	private Double volume;

	/**
	 * Flag if the Speaker starts listening when the application is started.
	 */
	private Boolean autoplay;

	/**
	 * This helper method fills the current instance of
	 * {@link SpeakerConfiguration} with values from another
	 * {@link SpeakerConfiguration}. Useful if i.e. a central spring managed
	 * instance of {@link SpeakerConfiguration} needs to be updated.
	 * 
	 * @param other
	 */
	public void fill(SpeakerConfiguration other) {
		setAutoplay(other.getAutoplay());
		setMixerIndex(other.getMixerIndex());
		setNetworkInterfaceIndex(other.getNetworkInterfaceIndex());
		setSpeakerChannel(other.getSpeakerChannel());
		setSpeakerName(other.getSpeakerName());
		setVolume(other.getVolume());
	}

}
