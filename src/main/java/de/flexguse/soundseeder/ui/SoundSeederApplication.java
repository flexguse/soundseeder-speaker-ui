/**
 * 
 */
package de.flexguse.soundseeder.ui;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.flexguse.soundseeder.model.SpeakerChannel;
import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.ConfigurationService;
import de.flexguse.soundseeder.service.ConfigurationServiceException;
import de.flexguse.soundseeder.service.SoundSeederService;
import de.flexguse.soundseeder.ui.component.ConfigurationForm;
import de.flexguse.soundseeder.ui.push.PlayEvent;
import de.flexguse.soundseeder.ui.push.PlayEventBroadcaster;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Theme("soundseeder")
@SpringUI(path = "")
@Push
public class SoundSeederApplication extends UI implements InitializingBean {

	private static final long serialVersionUID = -394340152951891751L;

	@Autowired
	private ConfigurationForm configurationForm;

	@Autowired
	private SoundSeederService soundSeederService;

	/**
	 * The service which persists/loads the speaker configuration.
	 */
	@Autowired
	private ConfigurationService configurationService;

	private Button playButton;

	private Button stopButton;

	private SpeakerConfiguration speakerConfiguration;

	@Autowired
	private I18N i18n;

	/**
	 * do some initialization work here
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		/*
		 * create the play button
		 */
		playButton = new Button(i18n.get("label.button.play"));
		playButton.setIcon(FontAwesome.PLAY_CIRCLE_O);
		playButton.setClickShortcut(KeyCode.ENTER);
		playButton.setStyleName(ValoTheme.BUTTON_HUGE);
		playButton.addClickListener(clickEvent -> handlePlayButtonClick(clickEvent));

		/*
		 * create the stop button
		 */
		stopButton = new Button(i18n.get("label.button.stop"));
		stopButton.setIcon(FontAwesome.STOP_CIRCLE_O);
		stopButton.setClickShortcut(KeyCode.ESCAPE);
		stopButton.setStyleName(ValoTheme.BUTTON_HUGE);
		stopButton.addClickListener(clickEvent -> handleStopButtonClick(clickEvent));

		/*
		 * load the speaker configuration
		 */
		speakerConfiguration = configurationService.loadConfiguration();
		if (speakerConfiguration == null) {
			speakerConfiguration = SpeakerConfiguration.builder().speakerName("soundseeder Speaker")
					.speakerChannel(SpeakerChannel.STEREO).volume(12.0).build();
		}
	}

	@Override
	protected void init(VaadinRequest request) {

		setContent(createConfigurationForm());
		PlayEventBroadcaster.register(this::handlePlayingBroadcast);

	}

	@Override
	public void detach() {
		PlayEventBroadcaster.unregister(this::handlePlayingBroadcast);
		super.detach();
	}

	/**
	 * This helper method creates the configuration form and adds the handlers
	 * to the buttons.
	 * 
	 * @return
	 */
	private Layout createConfigurationForm() {

		/*
		 * the layout containing the logo and all configuration inputs
		 */
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setWidth(100, Unit.PERCENTAGE);
		formLayout.setSpacing(true);
		formLayout.setMargin(new MarginInfo(true, true, true, true));

		/*
		 * add the logo to the form layout
		 */
		ThemeResource logoResource = new ThemeResource("img/sspLogo.png");
		Image logo = new Image("", logoResource);
		logo.setStyleName("logo");
		formLayout.addComponent(logo);
		formLayout.setExpandRatio(logo, 0.1f);

		/*
		 * add the configuration form
		 */
		configurationForm.setSpeakerConfiguration(speakerConfiguration);
		configurationForm.setSizeFull();
		formLayout.addComponent(configurationForm);
		formLayout.setExpandRatio(configurationForm, 0.5f);

		checkPlayStatus();

		/*
		 * add the play button
		 */
		formLayout.addComponent(playButton);
		formLayout.setComponentAlignment(playButton, Alignment.MIDDLE_CENTER);

		/*
		 * add the stop button
		 */
		formLayout.addComponent(stopButton);
		formLayout.setComponentAlignment(stopButton, Alignment.MIDDLE_CENTER);

		/*
		 * create the layout holding centered panel with 75% of width
		 */
		VerticalLayout panelLayout = new VerticalLayout();
		panelLayout.setSizeFull();

		/*
		 * create the panel
		 */
		Panel panel = new Panel();
		panel.setWidth(75, Unit.PERCENTAGE);
		panel.setHeight(75, Unit.PERCENTAGE);
		panel.setContent(formLayout);
		
		panelLayout.addComponent(panel);
		panelLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

		return panelLayout;

	}

	/**
	 * This helper method handles the click on the play button. The
	 * soundseederspeaker is started listening for music.
	 * 
	 * @param clickEvent
	 */
	private void handlePlayButtonClick(Button.ClickEvent clickEvent) {
		try {
			configurationForm.commit();

			speakerConfiguration = configurationForm.getSpeakerConfiguration();
			configurationService.saveConfiguration(speakerConfiguration);
			soundSeederService.listen(speakerConfiguration);
			PlayEventBroadcaster.broadcast(PlayEvent.builder().isPlaying(true).build());
		} catch (CommitException | ConstraintViolationException | ConfigurationServiceException e) {
			Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}
	}

	/**
	 * This helper method handles the click on the stop button. The
	 * soundseederspeaker is stopped listening for music.
	 * 
	 * @param clickEvent
	 */
	private void handleStopButtonClick(Button.ClickEvent clickEvent) {

		soundSeederService.stopListening();
		PlayEventBroadcaster.broadcast(PlayEvent.builder().isPlaying(false).build());

	}

	/**
	 * This helper method checks if the soundseeder speaker ist listening to
	 * music. If not listening the configuration form is enabled, else it is
	 * disabled.
	 */
	private void checkPlayStatus() {

		if (soundSeederService.isListening()) {
			setPlayStatus();
		} else {
			setConfigureStatus();
		}

	}

	/**
	 * This helper method enables the configuration form.
	 */
	private void setConfigureStatus() {
		access(() -> {
			playButton.setVisible(true);
			stopButton.setVisible(false);
			configurationForm.setEnabled(true);

		});
	}

	/**
	 * This helper method disabled the configuration form.
	 */
	private void setPlayStatus() {
		access(() -> {
			configurationForm.setEnabled(false);
			playButton.setVisible(false);
			stopButton.setVisible(true);
		});
	}

	/**
	 * This is the listener method of a {@link PlayEvent} broadcasted in the
	 * {@link PlayEventBroadcaster}.
	 * 
	 * @param playEvent
	 */
	private void handlePlayingBroadcast(PlayEvent playEvent) {

		if (playEvent.isPlaying()) {
			setPlayStatus();
		} else {
			setConfigureStatus();
		}

	}

}
