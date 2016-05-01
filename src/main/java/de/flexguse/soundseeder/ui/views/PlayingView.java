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
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.flexguse.soundseeder.service.SoundSeederService;
import de.flexguse.soundseeder.ui.component.CoverImage;
import de.flexguse.soundseeder.ui.component.MusicTitleInfo;
import de.flexguse.soundseeder.ui.component.VolumeSlider;
import de.flexguse.soundseeder.ui.events.ShowStoppedViewEvent;
import de.flexguse.soundseeder.ui.events.StopPlayingEvent;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringView(name = PlayingView.VIEW_NAME)
public class PlayingView extends SpeakerView {

	public static final String VIEW_NAME = "playing";

	@Autowired
	private ApplicationEventBus applicationEventBus;

	@Autowired
	private I18N i18n;

	@Autowired
	private CoverImage coverImage;

	@Autowired
	private MusicTitleInfo musicTitleInfo;

	@Autowired
	private VolumeSlider volumeSlider;
	
	@Autowired
	private SoundSeederService soundSeederService;
	
	@Autowired
	private SessionEventBus sessionEventBus;

	/**
	 * 
	 */
	private static final long serialVersionUID = 559452897025885750L;

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

		if(soundSeederService.getLastSong() != null){
			sessionEventBus.publish(this, soundSeederService.getLastSong());
		}
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		createStopButton();

		/*
		 * add cover image and volume slider
		 */
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setMargin(true);

		VerticalLayout coverLayout = new VerticalLayout();
		coverLayout.setSizeFull();
		layout.addComponent(coverLayout);

		coverImage.setSizeFull();
		coverLayout.addComponent(coverImage);
		coverLayout.setComponentAlignment(coverImage, Alignment.MIDDLE_CENTER);

		musicTitleInfo.setWidth(100, Unit.PERCENTAGE);
		musicTitleInfo.setHeight(150, Unit.PIXELS);
		coverLayout.addComponent(musicTitleInfo);
		coverLayout.setComponentAlignment(musicTitleInfo, Alignment.MIDDLE_CENTER);
		coverLayout.setExpandRatio(coverImage, 0.99f);

		volumeSlider.setOrientation(SliderOrientation.VERTICAL);
		volumeSlider.setHeight(100, Unit.PERCENTAGE);
		layout.addComponent(volumeSlider);
		layout.setComponentAlignment(volumeSlider, Alignment.MIDDLE_CENTER);

		setContent(layout);

	}

	/**
	 * This helper method adds the stop button to the view.
	 */
	private void createStopButton() {
		Button stopButton = new Button(i18n.get("label.button.stop"));
		stopButton.setIcon(FontAwesome.STOP);
		stopButton.setHeight(100, Unit.PERCENTAGE);
		stopButton.addClickListener(clickEvent -> handleStopButtonClick(clickEvent));

		VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		buttonLayout.addComponent(stopButton);
		buttonLayout.setComponentAlignment(stopButton, Alignment.MIDDLE_CENTER);

		addToButtonBar(buttonLayout);
	}

	/**
	 * This helper method handles the click on the stop button
	 * 
	 * @param clickEvent
	 */
	private void handleStopButtonClick(ClickEvent clickEvent) {

		// publish stop playing event
		applicationEventBus.publish(this, StopPlayingEvent.builder().eventSource(this).build());

		// switch to stopped view
		applicationEventBus.publish(this, ShowStoppedViewEvent.builder().eventSource(this).build());

	}

}
