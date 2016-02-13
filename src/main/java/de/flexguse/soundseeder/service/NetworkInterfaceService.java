/**
 * 
 */
package de.flexguse.soundseeder.service;

import java.net.NetworkInterface;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public interface NetworkInterfaceService {

	/**
	 * Gets a list of all available network interfaces.
	 * 
	 * @return filled or empty list of network interfaces
	 */
	public List<NetworkInterface> getNetworkInterfaces();

	/**
	 * Gets the Network interface by index.
	 * 
	 * @param index
	 * @return NetworkInterface or null
	 */
	public NetworkInterface getNetworkInterfaceByIndex(@NotNull Integer index);

}
