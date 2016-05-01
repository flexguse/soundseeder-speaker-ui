/**
 * 
 */
package de.flexguse.soundseeder.ui.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ApplicationEventBus;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.ConfigurationService;
import de.flexguse.soundseeder.service.ConfigurationServiceException;
import de.flexguse.soundseeder.ui.component.VolumeSlider;
import de.flexguse.soundseeder.ui.events.ShowPlayingViewEvent;
import de.flexguse.soundseeder.ui.events.StartPlayingEvent;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringView(name = StoppedView.VIEW_NAME)
public class StoppedView extends SpeakerView {

	private static final long serialVersionUID = -422412905596083099L;

	public static final String VIEW_NAME = "";

	@Autowired
	private I18N i18n;

	@Autowired
	private VolumeSlider volumeSlider;

	@Autowired
	private SessionEventBus sessionEventBus;

	@Autowired
	private ApplicationEventBus applicationEventBus;

	@Autowired
	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.
	 * ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
		super.enter(event);

		// set initial value of volume slider
		SpeakerConfiguration configuration;
		try {
			configuration = configurationService.loadConfiguration();

			if (configuration != null && configuration.getVolume() != null) {
				volumeSlider.setValue(configuration.getVolume());
			}

		} catch (ConfigurationServiceException e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}

	}

	/**
	 * In this method the complete view content is initialized.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		super.afterPropertiesSet();

		// create stop information (icon + label)
		VerticalLayout stopIconLayout = new VerticalLayout();
		stopIconLayout.setSizeFull();
		stopIconLayout.setSpacing(true);
		stopIconLayout.setMargin(true);
		
		Resource iconResource = new ThemeResource("img/speaker_stopped.svg");
		Image icon = new Image();
		icon.setSource(iconResource);
		icon.setSizeFull();
		Label stopInfo = new Label();
		stopInfo.setValue(i18n.get("label.stop"));
		stopInfo.setStyleName(ValoTheme.LABEL_HUGE);
		
		stopIconLayout.addComponent(icon);
		stopIconLayout.addComponent(stopInfo);
		stopIconLayout.setExpandRatio(icon, .99f);

		// create play button
		Button playButton = new Button(i18n.get("label.button.play"));
		playButton.setIcon(FontAwesome.PLAY);
		playButton.addClickListener(clickEvent -> handlePlayButtonClick(clickEvent));

		volumeSlider.setHeight(100, Unit.PERCENTAGE);
		volumeSlider.setOrientation(SliderOrientation.VERTICAL);

		// add a row containing stopped label and volume slider
		HorizontalLayout infoRow = new HorizontalLayout();
		infoRow.setSpacing(true);
		infoRow.setWidth(100, Unit.PERCENTAGE);
		infoRow.setHeight(100, Unit.PERCENTAGE);
		infoRow.addComponent(stopIconLayout);
		infoRow.addComponent(volumeSlider);
		infoRow.setComponentAlignment(volumeSlider, Alignment.MIDDLE_CENTER);
		setContent(infoRow);

		// add the play button
		HorizontalLayout buttonRow = new HorizontalLayout();
		buttonRow.setSpacing(true);
		buttonRow.setWidth(100, Unit.PERCENTAGE);

		buttonRow.setHeight(75, Unit.PIXELS);
		buttonRow.addComponent(playButton);
		buttonRow.setComponentAlignment(playButton, Alignment.MIDDLE_CENTER);
		addToButtonBar(buttonRow);

	}

	/**
	 * This helper method handles the play button click.
	 * 
	 * @param clickEvent
	 */
	private void handlePlayButtonClick(ClickEvent clickEvent) {

		// load speaker configuration
		SpeakerConfiguration speakerConfiguration;
		try {
			speakerConfiguration = configurationService.loadConfiguration();

			// dispatch StartPlayingEvent
			sessionEventBus.publish(this,
					StartPlayingEvent.builder().eventSource(this).speakerConfiguration(speakerConfiguration).build());

			// dispatch ShowPlayingViewEvent
			applicationEventBus.publish(this, ShowPlayingViewEvent.builder().eventSource(this).build());

		} catch (ConfigurationServiceException e) {

			Notification.show(i18n.get("error.service.config.caption"),
					i18n.get("error.service.config.description", e.getMessage()), Type.ERROR_MESSAGE);

		}

	}

}
