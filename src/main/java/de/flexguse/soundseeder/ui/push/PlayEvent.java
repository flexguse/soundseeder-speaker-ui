package de.flexguse.soundseeder.ui.push;

import lombok.Data;

import lombok.Builder;

@Data
@Builder
public class PlayEvent {

	private boolean isPlaying;

}
