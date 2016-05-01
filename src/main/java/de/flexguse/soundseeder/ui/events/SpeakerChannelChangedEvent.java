/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import de.flexguse.soundseeder.model.SpeakerChannel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This event is dispatched every time the {@link SpeakerChannel} configuration
 * was changed by the soundseeder player.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Builder
@Data
@EqualsAndHashCode
public class SpeakerChannelChangedEvent implements SpeakerEvent {

	private static final long serialVersionUID = 7865915289231116565L;

	private Object eventSource;

	/**
	 * contains the updated / new speaker channel
	 */
	private SpeakerChannel updatedChannel;

}
