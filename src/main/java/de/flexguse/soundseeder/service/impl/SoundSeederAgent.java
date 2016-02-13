/**
 * 
 */
package de.flexguse.soundseeder.service.impl;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import com.soundseeder.speaker.Speaker;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.NetworkInterfaceService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Christoph Guse, info@flexguse.de
 */
@Data
@Builder
@AllArgsConstructor
public class SoundSeederAgent implements Runnable {

	private SpeakerConfiguration speakerConfiguration;

	private NetworkInterfaceService networkInterfaceService;

	private Boolean initialStart;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		String[] args = createSpeakerArgs(speakerConfiguration);
		Speaker.main(args);

	}

	/**
	 * This helper method creates the command line interface (CLI) start options
	 * for Speaker.
	 * 
	 * @param configuration
	 * @return
	 */
	private String[] createSpeakerArgs(SpeakerConfiguration configuration) {

		List<String> arguments = new ArrayList<>();

		// arguments.add("--debug");

		// set the channel
		arguments.add("--channel");
		arguments.add(configuration.getSpeakerChannel().toString().toLowerCase());

		// set the network interface
		NetworkInterface networkInterface = networkInterfaceService
				.getNetworkInterfaceByIndex(configuration.getNetworkInterfaceIndex());
		if (networkInterface != null) {
			arguments.add("--ip");
			arguments.add(networkInterface.getInetAddresses().nextElement().getHostAddress().trim());
		}

		// set mixer
		arguments.add("--mixer");
		arguments.add(configuration.getMixerIndex().toString().trim());

		// set speaker name
		arguments.add("--name");
		arguments.add(configuration.getSpeakerName().trim());

		// set volume
		arguments.add("--volume");
		arguments.add(Integer.valueOf(configuration.getVolume().intValue()).toString());

		return arguments.toArray(new String[] {});

	}

}
