/**
 * 
 */
package de.flexguse.soundseeder.ui.component;

import java.net.InetAddress;
import java.net.NetworkInterface;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;

import de.flexguse.soundseeder.service.NetworkInterfaceService;
import de.flexguse.soundseeder.ui.converter.NetworkInterfaceToIndexConverter;

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

		setConverter(new NetworkInterfaceToIndexConverter(networkInterfaceService));
		
		IndexedContainer container = new IndexedContainer();

		networkInterfaceService.getNetworkInterfaces().forEach(networkInterface -> {

			try {
				container.addItem(networkInterface);
				setItemCaption(networkInterface, createCaption(networkInterface));
			} catch (Exception e) {
				// intended to do nothing
			}

		});

		setContainerDataSource(container);

		setNullSelectionAllowed(false);

	}

	/**
	 * This helper method extracts the caption of the network interface.
	 * 
	 * @param networkInterface
	 * @return
	 */
	private String createCaption(NetworkInterface networkInterface) {

		if (networkInterface != null) {

			// extract IP address
			String ipAddress = "";
			if (networkInterface.getInetAddresses() != null && networkInterface.getInetAddresses().hasMoreElements()) {
				InetAddress address = networkInterface.getInetAddresses().nextElement();
				ipAddress = address.getHostAddress();
			}

			return String.format("%s (%s)", networkInterface.getDisplayName(), ipAddress);

		}

		return "";

	}
	
	@Override
	public void setConverter(Converter<Object, ?> converter) {
		
		if(converter != null){
			super.setConverter(converter);
		}
			
	}

}
