/**
 * 
 */
package de.flexguse.soundseeder.service.impl;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.soundseeder.speaker.Speaker;
import com.soundseeder.speaker.SpeakerServiceAdapter;
import com.soundseeder.speaker.interfaces.NotificationListener;
import com.soundseeder.speaker.model.ChannelConf;
import com.soundseeder.speaker.model.PlaybackStatus;
import com.soundseeder.speaker.model.Song;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.model.converter.SpeakerChannelToChannelConfConverter;
import de.flexguse.soundseeder.service.NetworkInterfaceService;
import de.flexguse.soundseeder.service.SoundSeederService;
import de.flexguse.soundseeder.ui.events.SongChangeEvent;
import de.flexguse.soundseeder.ui.events.VolumeChangedEvent;
import lombok.Setter;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class SoundSeederServiceImpl implements SoundSeederService, DisposableBean, NotificationListener {

	@Autowired
	@Setter
	private NetworkInterfaceService networkInterfaceService;

	@Autowired
	private EventBus.ApplicationEventBus applicationEventBus;
	
//	@Autowired
//	private SpeakerConfiguration speakerConfiguration;

	private SpeakerServiceAdapter speakerServiceAdapter;

	private SpeakerChannelToChannelConfConverter converter = new SpeakerChannelToChannelConfConverter();

	private SongChangeEvent lastSong;

	/**
	 * This method is called before the {@link SoundSeederServiceImpl} is
	 * destroyed by Spring. We need to unregister the
	 * {@link NotificationListener} to avoid memory leaks.
	 */
	@Override
	public void destroy() throws Exception {

		if (speakerServiceAdapter != null) {
			speakerServiceAdapter.unregisterNotificationListener();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.flexguse.soundseeder.service.SoundSeederService#listen(de.flexguse.
	 * soundseeder.model.SpeakerConfiguration)
	 */
	@Override
	public Boolean listen(SpeakerConfiguration configuration) {

		if (speakerServiceAdapter == null) {
			speakerServiceAdapter = Speaker.launchInternal(createSpeakerArgs(configuration));
			speakerServiceAdapter.registerNotificationListener(this);
		} else {
			// set the configuration
			speakerServiceAdapter.setDeviceName(configuration.getSpeakerName());
			speakerServiceAdapter.setMusicStreamVolume(configuration.getVolume().intValue());
			speakerServiceAdapter.setSpeakerChannelConf(converter.convert(configuration.getSpeakerChannel()), true);
			speakerServiceAdapter.setActiveMixerByIndex(configuration.getMixerIndex().intValue());
		}

		if (speakerServiceAdapter.isPlaying()) {
			// do nothing, already listening
		} else {
			speakerServiceAdapter.toggleSpeakerPlayback();
		}

		return isListening();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.flexguse.soundseeder.service.SoundSeederService#updateVolume(int)
	 */
	@Override
	public void updateVolume(int volume) {

		if (speakerServiceAdapter != null) {
			speakerServiceAdapter.setMusicStreamVolume(volume);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.flexguse.soundseeder.service.SoundSeederService#stopListening()
	 */
	@Override
	public Boolean stopListening() {

		if (speakerServiceAdapter != null && speakerServiceAdapter.isPlaying()) {
			speakerServiceAdapter.toggleSpeakerPlayback();
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

		if (speakerServiceAdapter != null) {
			return speakerServiceAdapter.isPlaying();
		} else {
			return Boolean.FALSE;
		}
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
		String networkInterfaceIp = getNetworkAddress(configuration);
		if (networkInterfaceIp != null) {
			arguments.add("--ip");
			arguments.add(networkInterfaceIp);
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

	/**
	 * This helper method gets the IP of the selected network interface.
	 * 
	 * @param configuration
	 * @return
	 */
	private String getNetworkAddress(SpeakerConfiguration configuration) {

		NetworkInterface networkInterface = networkInterfaceService
				.getNetworkInterfaceByIndex(configuration.getNetworkInterfaceIndex());
		if (networkInterface != null && networkInterface.getInetAddresses() != null && networkInterface.getInetAddresses().hasMoreElements() && networkInterface.getInetAddresses().nextElement() != null) {
			return networkInterface.getInetAddresses().nextElement().getHostAddress().trim();
		}

		return null;

	}

	@Override
	public void onChannelConfChange(ChannelConf channelConfiguration) {
		
		// TODO: update speaker configuration, save configuration
		String test = "";
		
	}

	@Override
	public void onPlayerDataChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSongUpdate(Song song) {

		lastSong = SongChangeEvent.builder().albumName(song.getAlbumName()).artistName(song.getArtistName())
				.songDuration(song.getDurationFormatted()).songName(song.getSongName())
				.playerIp(speakerServiceAdapter.getConnectedPlayer().getIp()).build();

		applicationEventBus.publish(this, lastSong);

	}

	@Override
	public SongChangeEvent getLastSong() {
		return lastSong;
	}

	@Override
	public void onVolumeChange(int volume) {

		applicationEventBus.publish(this, VolumeChangedEvent.builder().changedVolume(volume).eventSource(this).build());

	}

	@Override
	public void onPlaybackStatusChange(PlaybackStatus arg0) {
		// TODO Auto-generated method stub
		
	}

}
