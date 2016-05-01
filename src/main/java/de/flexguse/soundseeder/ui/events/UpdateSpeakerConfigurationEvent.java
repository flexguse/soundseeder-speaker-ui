/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import lombok.Builder;
import lombok.Data;

/**
 * This event is dispatched when the {@link SpeakerConfiguration} was loaded or
 * if it was updated.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@Builder
public class UpdateSpeakerConfigurationEvent implements SpeakerEvent {

	private static final long serialVersionUID = 4245314328378849035L;

	private Object EventSource;

	private SpeakerConfiguration speakerConfiguration;

}
