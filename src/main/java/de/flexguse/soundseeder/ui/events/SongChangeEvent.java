/**
 * 
 */
package de.flexguse.soundseeder.ui.events;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
@EqualsAndHashCode
@Builder
public class SongChangeEvent implements SpeakerEvent {

	private static final long serialVersionUID = -8040383504574908149L;

	private Object eventSource;
	
	/**
	 * The current album name.
	 */
	private String albumName;

	/**
	 * The current artist name.
	 */
	private String artistName;

	/**
	 * The current song name.
	 */
	private String songName;

	/**
	 * The current song duration.
	 */
	private String songDuration;

	/**
	 * The IP address of the song player.
	 */
	private String playerIp;

}
