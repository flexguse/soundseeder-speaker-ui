/**
 * 
 */
package de.flexguse.soundseeder.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ApplicationEventBus;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.flexguse.soundseeder.model.GitRepositoryState;
import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.ConfigurationService;
import de.flexguse.soundseeder.service.ConfigurationServiceException;
import de.flexguse.soundseeder.service.SoundSeederService;
import de.flexguse.soundseeder.ui.events.SaveSpeakerConfigurationEvent;
import de.flexguse.soundseeder.ui.events.ShowConfigurationViewEvent;
import de.flexguse.soundseeder.ui.events.ShowPlayingViewEvent;
import de.flexguse.soundseeder.ui.events.ShowStoppedViewEvent;
import de.flexguse.soundseeder.ui.events.VolumeChangedEvent;
import de.flexguse.soundseeder.ui.views.ConfigurationView;
import de.flexguse.soundseeder.ui.views.PlayingView;
import de.flexguse.soundseeder.ui.views.StoppedView;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Theme("soundseeder")
@SpringUI(path = "")
@Title("soundseeder Speaker")
@Push
public class SoundSeederApplication extends UI implements DisposableBean {

	private static final long serialVersionUID = -394340152951891751L;

	@Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private ApplicationEventBus applicationEventBus;

	@Autowired
	private SessionEventBus sessionEventBus;

	@Autowired
	private ConfigurationService speakerConfigurationService;

	@Autowired
	private SoundSeederService soundSeederService;

	@Autowired
	private SpeakerConfiguration speakerConfiguration;
	
	@Autowired
	private ServletContext servletContext;

	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {

		applicationEventBus.subscribe(this);
		sessionEventBus.subscribe(this);

		// create master layout
		final VerticalLayout root = new VerticalLayout();
		root.addStyleName("root");
		root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);

		// header
		CssLayout headerRow = new CssLayout();
		headerRow.setSizeUndefined();
		headerRow.addStyleName("header");
		Image logo = new Image("", new ThemeResource("img/sspLogo.png"));
		logo.addStyleName("logo");
		logo.setSizeUndefined();
		headerRow.addComponent(logo);
		root.addComponent(headerRow);
		root.setComponentAlignment(headerRow, Alignment.MIDDLE_CENTER);

		// view panel
		final Panel viewPanel = new Panel();
		viewPanel.addStyleName("view-panel");
		viewPanel.setSizeFull();		
		root.addComponent(viewPanel);
		root.setComponentAlignment(viewPanel, Alignment.MIDDLE_CENTER);
		root.setExpandRatio(viewPanel, 1.0f);
		
		navigator = new Navigator(this, viewPanel);
		navigator.addProvider(viewProvider);

		setSizeFull();
		addStyleName("main-view");
		
		setContent(root);
	              
		setStartView();
	}

	/**
	 * This helper method navigates to the view which currently is correct.
	 */
	private void setStartView() {

		if (soundSeederService.isListening()) {
			// open playing view
			handleShowPlayingViewEvent(ShowPlayingViewEvent.builder().eventSource(this).build());

		} else {
			// open stopped view
			handleShowStoppedViewEvent(ShowStoppedViewEvent.builder().eventSource(this).build());
		}
	}

	/**
	 * This event handler method switches to the {@link PlayingView}.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.APPLICATION)
	public void handleShowPlayingViewEvent(ShowPlayingViewEvent event) {

		try {
			soundSeederService.listen(speakerConfigurationService.loadConfiguration());

			getUI().access(() -> {
				// switch to playing view
				navigator.navigateTo(PlayingView.VIEW_NAME);
			});

		} catch (ConfigurationServiceException e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}

	}

	/**
	 * This event handler method switches to the {@link StoppedView}.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.APPLICATION)
	public void handleShowStoppedViewEvent(ShowStoppedViewEvent event) {

		soundSeederService.stopListening();

		getUI().access(() -> {
			// switch to stopped view
			navigator.navigateTo(StoppedView.VIEW_NAME);

		});
	}

	/**
	 * This event handler method switches to the {@link ConfigurationView}.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.SESSION)
	public void handleShowConfigurationViewEvent(ShowConfigurationViewEvent event) {

		getUI().access(() -> {
			// switch to configuration view
			navigator.navigateTo(ConfigurationView.VIEW_NAME);
		});

	}

	/**
	 * This event handler method handles the
	 * {@link SaveSpeakerConfigurationEvent}. The configuration is updated.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.SESSION)
	public void handleUpdateSpeakerConfigurationEvent(SaveSpeakerConfigurationEvent event) {

		try {
			speakerConfigurationService.saveConfiguration(event.getUpdatedSpeakerConfiguration());
			speakerConfiguration.fill(event.getUpdatedSpeakerConfiguration());
		} catch (ConstraintViolationException | ConfigurationServiceException e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}

	}

	/**
	 * This event handler method updates the volume in the configuration and in
	 * the soundseeder speaker.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.SESSION)
	public void handleVolumeChanged(VolumeChangedEvent event) {

		try {

			speakerConfiguration.setVolume(event.getChangedVolume().doubleValue());
			speakerConfigurationService.saveConfiguration(speakerConfiguration);
			soundSeederService.updateVolume(event.getChangedVolume());

		} catch (ConstraintViolationException | ConfigurationServiceException e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}

	}

	@Override
	public void destroy() throws Exception {
		sessionEventBus.unsubscribe(this);
		applicationEventBus.unsubscribe(this);

	}
	
	// see: https://github.com/vaadin/spring/issues/13
	@PostConstruct
	public void init() throws IOException {
	    Theme annotation = getUI().getClass().getAnnotation(Theme.class);
	    if (annotation != null) {
	        String root = servletContext.getRealPath("/");
	        if (root != null && Files.isDirectory(Paths.get(root))) {
	            Files.createDirectories(Paths.get(servletContext.getRealPath("/VAADIN/themes/" + annotation.value())));
	        }
	    }
	}

}
