/**
 * 
 */
package de.flexguse.soundseeder.ui.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.SessionEventBus;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

import de.flexguse.soundseeder.service.SoundSeederService;
import de.flexguse.soundseeder.ui.component.CoverImage;
import de.flexguse.soundseeder.ui.component.MusicTitleInfo;
import de.flexguse.soundseeder.ui.component.VolumeSlider;
import de.flexguse.soundseeder.ui.events.ShowStoppedViewEvent;
import de.flexguse.soundseeder.ui.events.StopPlayingEvent;
import de.flexguse.soundseeder.util.SpeakerUIConstants;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringView(name = PlayingView.VIEW_NAME)
public class PlayingView extends SpeakerView {

    private static final long serialVersionUID = 559452897025885750L;
    
    public static final String VIEW_NAME = "playing";

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

    @Override
    public void enter(ViewChangeEvent event) {

        super.enter(event);

        if (soundSeederService.getLastSong() != null) {
            sessionEventBus.publish(this, soundSeederService.getLastSong());
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // main layout
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setSpacing(true);

        // cover
        VerticalLayout coverLayout = new VerticalLayout();
        coverLayout.addStyleName(SpeakerUIConstants.STYLE_MUSIC_PANE);
        coverLayout.setSizeFull();
        coverImage.setSizeFull();
        coverImage.addStyleName(SpeakerUIConstants.STYLE_COVER_IMAGE);
        coverLayout.addComponent(coverImage);
        coverLayout.setComponentAlignment(coverImage, Alignment.MIDDLE_CENTER);
        // label
        musicTitleInfo.setWidth(100, Unit.PERCENTAGE);
        coverLayout.addComponent(musicTitleInfo);
        coverLayout.setComponentAlignment(musicTitleInfo, Alignment.MIDDLE_CENTER);
        coverLayout.setExpandRatio(coverImage, 1.0f);

        // add cover and label
        layout.addComponent(coverLayout);
        // add slider
        layout.addComponent(volumeSlider);
        layout.setComponentAlignment(volumeSlider, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(coverLayout, 1.0f);

        setContent(layout);
        
        createButtons();
    }

    void createButtons() {
        // stop button
        Button stopButton = new Button(i18n.get("label.button.disconnect"));
        stopButton.setIcon(FontAwesome.CHAIN_BROKEN);
        stopButton.addClickListener(this::handleStopButtonClick);

        super.createButtons(stopButton);
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
