/**
 * 
 */
package de.flexguse.soundseeder.ui.component;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.flexguse.soundseeder.ui.events.SongChangeEvent;

/**
 * The {@link MusicTitleInfo} component consists of two rows:
 * <ol>
 * <li>the title of the music</li>
 * <li>the name of the artist</li>
 * </ol>
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@UIScope
@Component
public class MusicTitleInfo extends VerticalLayout implements InitializingBean, DisposableBean {

	private static final long serialVersionUID = -6482907751398569386L;

	private Label title;
	private Label artist;

	@Autowired
	private EventBus.ApplicationEventBus applicationEventBus;

	@Autowired
	private EventBus.SessionEventBus sessionEventBus;

	@Override
	public void destroy() throws Exception {
		applicationEventBus.unsubscribe(this);
		sessionEventBus.unsubscribe(this);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		applicationEventBus.subscribe(this);
		sessionEventBus.subscribe(this);

		setResponsive(true);

		setWidth(100, Unit.PERCENTAGE);
		setSpacing(true);
		setMargin(true);

		title = new Label("no title information");
		title.addStyleName("music_title");
		addComponent(title);
		setComponentAlignment(title, Alignment.MIDDLE_CENTER);

		artist = new Label("no artist information");
		artist.addStyleName("music_artist");
		addComponent(artist);
		setComponentAlignment(artist, Alignment.MIDDLE_CENTER);

	}

	/**
	 * Every time the song changed the information is updated.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.APPLICATION)
	public void listenForSongChange(SongChangeEvent event) {
		updateMusicInfo(event);
	}

	/**
	 * Listens for the event on the session event bus which normally is
	 * published when the application instance is started and soundseeder
	 * already is playing a song.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.SESSION)
	public void listenForPlayingSong(SongChangeEvent event) {
		updateMusicInfo(event);
	}

	/**
	 * This helper method updates the cover and the information of the song.
	 * 
	 * @param event
	 */
	private void updateMusicInfo(SongChangeEvent event) {
		if (getUI() != null) {
			getUI().access(() -> {
				title.setValue(event.getSongName());
				artist.setValue(event.getArtistName());
			});

		}
	}

}
