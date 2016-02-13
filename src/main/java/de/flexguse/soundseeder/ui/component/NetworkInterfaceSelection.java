/**
 * 
 */
package de.flexguse.soundseeder.ui.component;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;

import de.flexguse.soundseeder.service.NetworkInterfaceService;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringComponent
@UIScope
public class NetworkInterfaceSelection extends ComboBox implements InitializingBean {

	private static final long serialVersionUID = 5093486729341334334L;

	@Autowired
	private NetworkInterfaceService networkInterfaceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		IndexedContainer container = new IndexedContainer();
		
		networkInterfaceService.getNetworkInterfaces().forEach( networkInterface -> {
			
			try {
				container.addItem(networkInterface.getIndex());
				setItemCaption(networkInterface.getIndex(), networkInterface.getDisplayName());
			} catch (Exception e) {
				// intended to do nothing
			} 
			
			
			
		});
		
		setContainerDataSource(container);

		setNullSelectionAllowed(false);

	}

}
