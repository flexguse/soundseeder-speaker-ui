/**
 * 
 */
package de.flexguse.soundseeder.model;

import javax.sound.sampled.Mixer.Info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Custom extension of {@link Info} including index of audio device.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeakerMixerInfo {

	private String name;
	private String vendor;
	private String description;
	private String version;
	private Integer index;

}
