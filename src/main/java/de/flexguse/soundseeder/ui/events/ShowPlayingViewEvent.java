/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event to show the playing view.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class ShowPlayingViewEvent implements SpeakerEvent {

	private static final long serialVersionUID = 3442681384529119268L;

	private Object eventSource;

}
