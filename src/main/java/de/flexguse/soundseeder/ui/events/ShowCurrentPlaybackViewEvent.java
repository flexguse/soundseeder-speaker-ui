/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event to show the current view.
 * 
 */
@Data
@Builder
public class ShowCurrentPlaybackViewEvent implements SpeakerEvent {

	private static final long serialVersionUID = -8837845552494410467L;
	
	private Object eventSource;

}
