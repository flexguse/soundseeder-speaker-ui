/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event to start playing music provided by the player.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class StartPlayingEvent implements SpeakerEvent {

	private static final long serialVersionUID = -6906749724041329577L;

	private Object eventSource;

	/**
	 * The configuration which is needed to start playing the stream provided by
	 * the player.
	 */
	private SpeakerConfiguration speakerConfiguration;

}
