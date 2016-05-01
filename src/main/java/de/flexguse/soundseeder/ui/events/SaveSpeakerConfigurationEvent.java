/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event if the SpeakerConfiguration needs to be updated.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class SaveSpeakerConfigurationEvent implements SpeakerEvent {

	private static final long serialVersionUID = 5228369216540040253L;

	private Object eventSource;

	/**
	 * The updated speaker configuration
	 */
	private SpeakerConfiguration updatedSpeakerConfiguration;

}
