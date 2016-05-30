package de.flexguse.soundseeder;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(SoundSeederSpeakerApplicationConfiguration.class)
public class SoundSeederSpeakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoundSeederSpeakerApplication.class, args);
	}
}
