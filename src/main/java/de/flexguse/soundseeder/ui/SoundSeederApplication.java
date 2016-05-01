/**
 * 
 */
package de.flexguse.soundseeder.ui;

import java.awt.Label;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ApplicationEventBus;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
	private I18N i18n;

	private Navigator navigator;

	private Button settingsButton;

	@Override
	protected void init(VaadinRequest request) {

		applicationEventBus.subscribe(this);
		sessionEventBus.subscribe(this);

		// create master layout
		final VerticalLayout root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);

		// add header row to panel
		HorizontalLayout headerRow = new HorizontalLayout();
		headerRow.setWidth(75, Unit.PERCENTAGE);

		Image logo = new Image("", new ThemeResource("img/sspLogo.png"));
		logo.setHeight(75, Unit.PIXELS);
		headerRow.addComponent(logo);

		settingsButton = new Button(i18n.get("label.button.settings"));
		settingsButton.addClickListener(clickEvent -> applicationEventBus.publish(this,
				ShowConfigurationViewEvent.builder().eventSource(this).build()));
		settingsButton.setIcon(FontAwesome.EDIT);
		headerRow.addComponent(settingsButton);
		headerRow.setComponentAlignment(settingsButton, Alignment.BOTTOM_RIGHT);

		// add footer row containing the software version number
		HorizontalLayout footerRow = new HorizontalLayout();
		footerRow.setWidth(75, Unit.PERCENTAGE);
		
		Label versionInfo = new Label();
		//versionInfo.setText(String.format("", args));
		
		root.addComponent(headerRow);
		root.setComponentAlignment(headerRow, Alignment.TOP_CENTER);
		setContent(root);

		/*
		 * create 75% panel centered
		 */
		final Panel viewPanel = new Panel();
		viewPanel.setWidth(75, Unit.PERCENTAGE);
		viewPanel.setHeight(100, Unit.PERCENTAGE);
		root.addComponent(viewPanel);
		root.setComponentAlignment(viewPanel, Alignment.TOP_CENTER);
		root.setExpandRatio(viewPanel, 0.99f);

		navigator = new Navigator(this, viewPanel);
		navigator.addProvider(viewProvider);

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
				// disable settings button
				settingsButton.setVisible(false);
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

			// enable settings button
			settingsButton.setVisible(true);

		});
	}

	/**
	 * This event handler method switches to the {@link ConfigurationView}.
	 * 
	 * @param event
	 */
	@EventBusListenerMethod(scope = EventScope.APPLICATION)
	public void handleShowConfigurationViewEvent(ShowConfigurationViewEvent event) {

		getUI().access(() -> {
			// switch to configuration view
			navigator.navigateTo(ConfigurationView.VIEW_NAME);

			// remove settings button
			settingsButton.setVisible(false);
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

}
