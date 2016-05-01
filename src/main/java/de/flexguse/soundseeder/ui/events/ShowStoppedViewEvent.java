/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event to show the stopped view.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class ShowStoppedViewEvent implements SpeakerEvent {

	private static final long serialVersionUID = 8236036455420059896L;

	private Object eventSource;
	
}
