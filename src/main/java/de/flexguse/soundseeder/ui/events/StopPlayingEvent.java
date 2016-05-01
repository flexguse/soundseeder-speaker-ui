/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event if the speaker shall stop playing a stream provided by
 * the player.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class StopPlayingEvent implements SpeakerEvent {

	private static final long serialVersionUID = 1227357258252594309L;

	private Object eventSource;

}
