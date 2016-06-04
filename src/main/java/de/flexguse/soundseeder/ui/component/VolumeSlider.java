/**
 * 
 */
package de.flexguse.soundseeder.ui.component;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Slider;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.ui.events.VolumeChangedEvent;
import de.flexguse.soundseeder.util.SpeakerUIConstants;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@ViewScope
@org.springframework.stereotype.Component
public class VolumeSlider extends Slider implements InitializingBean, DisposableBean {

	private static final long serialVersionUID = -7110455962158736894L;

	@Autowired
	private EventBus.ApplicationEventBus applicationEventBus;

	@Autowired
	private EventBus.SessionEventBus sessionEventBus;
	
	@Autowired
	private SpeakerConfiguration speakerConfiguration;
	
	@Override
	public void afterPropertiesSet() throws Exception {

		applicationEventBus.subscribe(this);
		
		addStyleName(SpeakerUIConstants.STYLE_VOLUME_SLIDER);
		setHeight(100, Unit.PERCENTAGE);
                setOrientation(SliderOrientation.VERTICAL);
		setMin(0);
		setMax(15);
		setIcon(FontAwesome.VOLUME_UP);
		setImmediate(true);
		setResolution(0);
		if(speakerConfiguration != null){
			setValue(speakerConfiguration.getVolume());
		}

		/*
		 * dispatch the volume changed event if the volume was changed
		 */
		addValueChangeListener(changeEvent -> publishVolumeChangedEvent(changeEvent, this));

	}

	/**
	 * This helper method publishes the application wide event when the volume
	 * was changed.
	 * 
	 * @param changeEvent
	 */
	private void publishVolumeChangedEvent(Property.ValueChangeEvent changeEvent, Component eventSender) {

		int updatedVolume = ((Double) changeEvent.getProperty().getValue()).intValue();

		VolumeChangedEvent volumeChangeEvent = VolumeChangedEvent.builder().eventSource(eventSender)
				.changedVolume(updatedVolume).build();

		/*
		 * publish the change event in the applicationEventBus for updating the
		 * slider volume in all application views.
		 */
		applicationEventBus.publish(this, volumeChangeEvent);

		/*
		 * publish the change event in the sessionEventBus so the volume is
		 * changed and the updated configuration is saved. this needs only to be
		 * done once.
		 */
		sessionEventBus.publish(this, volumeChangeEvent);
	}

	/**
	 * Handles the volume change event.
	 * 
	 * @param volumeChangedEvent
	 */
	@EventBusListenerMethod(scope = EventScope.APPLICATION)
	public void handleVolumeChanged(VolumeChangedEvent volumeChangedEvent) {

		/*
		 * Only change the volume if the event was not dispatched by this
		 * component.
		 */
		if (volumeChangedEvent != null && volumeChangedEvent.getChangedVolume() != null
				&& !this.equals(volumeChangedEvent.getEventSource()) && getUI() != null) {

			getUI().access(() -> {
				try {
					setValue(volumeChangedEvent.getChangedVolume().doubleValue());
				} catch (NullPointerException e) {
					// do nothing
				}

			});

		}

	}

	@Override
	public void destroy() throws Exception {
		applicationEventBus.unsubscribe(this);

	}

}
