/**
 * 
 */
package de.flexguse.soundseeder.ui.component;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;

import de.flexguse.soundseeder.model.SpeakerChannel;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringComponent
@UIScope
public class AudioChannelSelection extends ComboBox implements InitializingBean {

	private static final long serialVersionUID = -1011625583332435592L;

	@Autowired
	private I18N i18n;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		setNullSelectionAllowed(false);

		IndexedContainer container = new IndexedContainer();
		
		
		// fill selection options
		for (SpeakerChannel channel : SpeakerChannel.values()) {
			
			container.addItem(channel);
			setItemCaption(channel, i18n.get("channel."+channel.toString().toLowerCase()));
				
		}
		
		setContainerDataSource(container);

	}

}
