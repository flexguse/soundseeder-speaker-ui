/**
 * 
 */
package de.flexguse.soundseeder.ui.views;

import org.springframework.beans.factory.InitializingBean;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

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
		// set size of the view component
		setSizeFull();
		setSpacing(true);
		setMargin(true);

		/*
		 * add content panel
		 */
		contentPanel = new Panel();
		contentPanel.setSizeFull();
		addComponent(contentPanel);

		/*
		 * add button bar
		 */
		buttonBar = new HorizontalLayout();
		buttonBar.setHeight(50, Unit.PIXELS);
		buttonBar.setWidth(100, Unit.PERCENTAGE);
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

	/**
	 * Adds the component to the button bar. Existing components are not
	 * removed.
	 * 
	 * @param component
	 */
	public void addToButtonBar(Component component) {
		buttonBar.addComponent(component);
	}

}
