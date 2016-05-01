/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event if the volume was changed in the player or in the speaker
 * application.
 * <p>
 * If the volume was changed in the speaker application the updated volume needs
 * to be sent to the player. If the volume was changed by the player the volume
 * component must be adjusted.
 * </p>
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class VolumeChangedEvent implements SpeakerEvent {

	private static final long serialVersionUID = 7503157781701324592L;

	/**
	 * The object / component who started the event.
	 */
	private Object eventSource;

	/**
	 * The updated volume.
	 */
	private Integer changedVolume;
}
