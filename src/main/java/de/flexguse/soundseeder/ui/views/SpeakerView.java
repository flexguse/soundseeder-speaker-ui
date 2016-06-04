/**
 * 
 */
package de.flexguse.soundseeder.ui.views;

import javax.persistence.MapKeyEnumerated;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.ApplicationEventBus;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.flexguse.soundseeder.model.GitRepositoryState;
import de.flexguse.soundseeder.ui.events.ShowConfigurationViewEvent;
import de.flexguse.soundseeder.util.SpeakerUIConstants;
import de.flexguse.soundseeder.util.Util;

/**
 * SpeakerView is the abstract view class for all views used in the Speaker
 * application. This view consists of a Panel which is scollable and a button
 * bar.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public abstract class SpeakerView extends VerticalLayout implements View, InitializingBean {

	private static final long serialVersionUID = 7799535436789855961L;

	@Autowired
	protected ApplicationEventBus applicationEventBus;
	
	@Autowired
	private SessionEventBus sessionEventBus;

	@Autowired
	protected I18N i18n;
	
	@Autowired
        private GitRepositoryState gitRepositoryState;
	

	private Panel contentPanel;

	private HorizontalLayout buttonBar;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.
	 * ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	        addStyleName(SpeakerUIConstants.STYLE_SPEAKER_VIEW);
	        Responsive.makeResponsive(this);
	        
		// set size of the view component
		setSizeFull();
		setSpacing(true);
		setMargin(true);

		/*
		 * add content panel
		 */
		contentPanel = new Panel();
		Responsive.makeResponsive(contentPanel);
		contentPanel.setSizeFull();
		contentPanel.addStyleName(SpeakerUIConstants.STYLE_MAIN_CONTENT);
		addComponent(contentPanel);

		/*
		 * add button bar
		 */
		buttonBar = new HorizontalLayout();
		buttonBar.addStyleName(SpeakerUIConstants.STYLE_BUTTON_BAR);
		buttonBar.setWidth(100, Unit.PERCENTAGE);
		Responsive.makeResponsive(buttonBar);
		addComponent(buttonBar);

		setExpandRatio(contentPanel, .99f);
	}

	/**
	 * Sets the content of the contentPanel.
	 * 
	 * @param contentComponent
	 */
	public void setContent(Component contentComponent) {
		contentPanel.setContent(contentComponent);
	}

        protected void createButtons(Button... buttons) {
            HorizontalLayout buttonRow = new HorizontalLayout();
            buttonRow.setSpacing(true);
            buttonRow.setWidth(100, Unit.PERCENTAGE);

            for (Button but : buttons) {
                buttonRow.addComponent(but);
                buttonRow.setComponentAlignment(but, Alignment.MIDDLE_CENTER);
                buttonRow.setExpandRatio(but, .99f);
            }
            
            // config menu
            MenuBar settingsMenu = getMenuButton("");
            buttonRow.addComponent(settingsMenu);
            buttonRow.setComponentAlignment(settingsMenu, Alignment.MIDDLE_RIGHT);
            
            addToButtonBar(buttonRow);
        }
	
	/**
	 * Adds the component to the button bar. Existing components are not
	 * removed.
	 * 
	 * @param component
	 */
	public void addToButtonBar(Component component) {
		buttonBar.addComponent(component);
	}
	
	protected MenuBar getMenuButton(String caption) {
	    MenuBar menu = new MenuBar();
	    menu.addStyleName(SpeakerUIConstants.STYLE_CONFIG_MENU);
	    Responsive.makeResponsive(menu);
	    MenuBar.MenuItem dropdown = menu.addItem(caption, FontAwesome.EDIT, null);
	    dropdown.addItem(i18n.get("label.button.settings"), 
	            clickEvent -> sessionEventBus.publish(this, 
	                    ShowConfigurationViewEvent.builder().eventSource(this).build()));
	    dropdown.addItem(i18n.get("label.button.info"), clickEvent -> {
	        String info = String.format(
	                "<span style=\"font-weight: bold;\">" + "Version: " + "</span>" + "%s" + "<br>" + 
	                "<span style=\"font-weight: bold;\">" + "Commit time: " + "</span>" + "%s" + "<br>" + 
	                "<span style=\"font-weight: bold;\">" + "Commit: " + "</span>" + "%s", 
	                gitRepositoryState.getBuildVersion(), 
	                gitRepositoryState.getCommitTime(), 
	                gitRepositoryState.getCommitIdDescribeShort());
	        Util.showNotification("", info);
	    });

	    return menu;
	}
}
