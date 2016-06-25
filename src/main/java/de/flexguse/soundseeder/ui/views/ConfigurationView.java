/**
 * 
 */
package de.flexguse.soundseeder.ui.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ApplicationEventBus;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.ui.component.ConfigurationForm;
import de.flexguse.soundseeder.ui.events.SaveSpeakerConfigurationEvent;
import de.flexguse.soundseeder.ui.events.ShowCurrentPlaybackViewEvent;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringView(name = ConfigurationView.VIEW_NAME)
public class ConfigurationView extends SpeakerView {

	public static final String VIEW_NAME = "config";

	@Autowired
	private SessionEventBus sessionEventBus;
	
	@Autowired
	private ApplicationEventBus applicationEventBus;

	/**
	 * The form in which the configuration can be done.
	 */
	@Autowired
	private ConfigurationForm configurationForm;

	/**
	 * Holds all translated labels.
	 */
	@Autowired
	private I18N i18n;

	/**
	 * The save button.
	 */
	private Button saveButton;

	/**
	 * The cancel button.
	 */
	private Button cancelButton;

	@Autowired
	private SpeakerConfiguration speakerConfiguration;

	private static final long serialVersionUID = 8853354805970763672L;

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
	}

	/**
	 * This method creates the configuration form and adds the handlers to the
	 * buttons.
	 * 
	 * @return
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		super.afterPropertiesSet();
		
		initializeButtons();

		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setWidth(100, Unit.PERCENTAGE);
		formLayout.setSpacing(true);
		formLayout.setMargin(new MarginInfo(true, true, true, true));

		/*
		 * add the configuration form
		 */
		configurationForm.setImmediate(false);
		configurationForm.setSpeakerConfiguration(speakerConfiguration);
		configurationForm.setSizeFull();
		formLayout.addComponent(configurationForm);
		//formLayout.setExpandRatio(configurationForm, 0.5f);
		setContent(formLayout);
		
		/*
		 * add the button bar including cancel and save button
		 */
		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setWidth(100, Unit.PERCENTAGE);

		/*
		 * add the cancel button
		 */
		buttonBar.addComponent(cancelButton);
		buttonBar.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);

		/*
		 * add the save button
		 */
		buttonBar.addComponent(saveButton);
		buttonBar.setComponentAlignment(saveButton, Alignment.MIDDLE_CENTER);
		
		addToButtonBar(buttonBar);



	}

	/**
	 * This helper method initializes the buttons.
	 */
	private void initializeButtons() {

		/*
		 * create the play button
		 */
		saveButton = new Button(i18n.get("label.button.save"));
		saveButton.setIcon(FontAwesome.SAVE);
		saveButton.setClickShortcut(KeyCode.ENTER);
		saveButton.addClickListener(clickEvent -> handleSaveButtonClick(clickEvent));

		/*
		 * create the stop button
		 */
		cancelButton = new Button(i18n.get("label.button.cancel"));
		cancelButton.setIcon(FontAwesome.REMOVE);
		cancelButton.setClickShortcut(KeyCode.ESCAPE);
		cancelButton.addClickListener(clickEvent -> handleCancelButtonClick(clickEvent));

	}

	/**
	 * This helper method handles the click on the save button.
	 * 
	 * @param clickEvent
	 */
	private void handleSaveButtonClick(ClickEvent clickEvent) {

		try {
			// commit changes in form
			configurationForm.commit();

			// dispatch save event
			sessionEventBus.publish(this, SaveSpeakerConfigurationEvent.builder().eventSource(this)
					.updatedSpeakerConfiguration(configurationForm.getSpeakerConfiguration()).build());

			// show current view
			applicationEventBus.publish(this, ShowCurrentPlaybackViewEvent.builder().eventSource(this).build());

		} catch (CommitException e) {
			// do nothing, just show the errors in the form
		}

	}

	/**
	 * This helper method handles the click on the save button.
	 * 
	 * @param clickEvent
	 */
	private void handleCancelButtonClick(ClickEvent clickEvent) {

		// just show the current view
		applicationEventBus.publish(this, ShowCurrentPlaybackViewEvent.builder().eventSource(this).build());

	}

}
