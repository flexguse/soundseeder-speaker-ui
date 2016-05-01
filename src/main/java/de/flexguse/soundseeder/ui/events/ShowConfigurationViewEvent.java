/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import lombok.Builder;
import lombok.Data;

/**
 * Dispatch this event to show the configuration view.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class ShowConfigurationViewEvent implements SpeakerEvent {

	private static final long serialVersionUID = 7506338400446610172L;

	private Object eventSource;

}
