/**
 * 
 */
package de.flexguse.soundseeder.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.NetworkInterfaceService;
import de.flexguse.soundseeder.service.SoundSeederService;
import lombok.Setter;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class SoundSeederServiceImpl implements SoundSeederService {

	@Autowired
	@Setter
	private NetworkInterfaceService networkInterfaceService;

	private Thread listeningThread;

	private Boolean initialStart = Boolean.TRUE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.flexguse.soundseeder.service.SoundSeederService#listen(de.flexguse.
	 * soundseeder.model.SpeakerConfiguration)
	 */
	@Override
	public Boolean listen(SpeakerConfiguration configuration) {

		if (!isListening()) {

			listeningThread = new Thread(
					SoundSeederAgent.builder().networkInterfaceService(networkInterfaceService)
							.speakerConfiguration(configuration).initialStart(initialStart).build(),
					"SoundSeederSpeaker");
			listeningThread.start();

			initialStart = Boolean.FALSE;
		}

		return isListening();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.flexguse.soundseeder.service.SoundSeederService#stopListening()
	 */
	@Override
	public Boolean stopListening() {

		if (listeningThread != null) {

			/*
			 * Interrupting the listening thread is not enough, the threads
			 * started by SoundSeederSpeaker must be interrupted, too. Very ugly
			 * implementation to get list of all threads.
			 */
			Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();

			map.keySet().forEach(thread -> {
				if ("HelloListener".equals(thread.getName()) || "PlayerConnectionReceiver".equals(thread.getName())
						|| "WlanWatchDog".equals(thread.getName())) {
					thread.interrupt();
				}
			});

			listeningThread.interrupt();
			listeningThread = null;
		}

		return isListening();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.flexguse.soundseeder.service.SoundSeederService#isListening()
	 */
	@Override
	public Boolean isListening() {

		return listeningThread != null;
	}

}
