/**
 * 
 */
package de.flexguse.soundseeder.ui.component;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Image;

import de.flexguse.soundseeder.ui.events.SongChangeEvent;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@UIScope
@Component
public class CoverImage extends Image implements InitializingBean, DisposableBean {

	private static final long serialVersionUID = -4086710862987597509L;

	@Autowired
	private EventBus.ApplicationEventBus applicationEventBus;

	/**
	 * default play icon
	 */
	private ThemeResource speakerIcon = new ThemeResource("img/speaker_playing.svg");

	/**
	 * Every time the song changed the album cover is loaded.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.APPLICATION)
	public void listenForSongChange(SongChangeEvent event) {

		Resource cover = new ExternalResource(String.format("http://%s:5353/0", event.getPlayerIp()));
		if (cover == null || StringUtils.isEmpty(cover.getMIMEType())) {
			if(getUI() != null){
				getUI().access(() -> setSource(speakerIcon));
			}
			

		} else {

			if(getUI() != null){
				getUI().access(() -> setSource(cover));
			}
			
		}

	}

	@Override
	public void destroy() throws Exception {
		applicationEventBus.unsubscribe(this);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		applicationEventBus.subscribe(this);

		setSource(speakerIcon);
	}
}
