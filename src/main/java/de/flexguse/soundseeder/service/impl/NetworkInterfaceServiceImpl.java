/**
 * 
 */
package de.flexguse.soundseeder.service.impl;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import de.flexguse.soundseeder.service.NetworkInterfaceService;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class NetworkInterfaceServiceImpl implements NetworkInterfaceService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.flexguse.soundseeder.ui.service.NetworkInterfaceService#
	 * getNetworkInterfaces()
	 */
	@Override
	public List<NetworkInterface> getNetworkInterfaces() {

		List<NetworkInterface> result = new ArrayList<>();

		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

			while (networkInterfaces.hasMoreElements()) {

				NetworkInterface networkInterface = networkInterfaces.nextElement();

				// only add network interfaces having network addess
				if (!networkInterface.getInterfaceAddresses().isEmpty()) {
					result.add(networkInterface);
				}

			}

		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public NetworkInterface getNetworkInterfaceByIndex(Integer index) {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

			while (networkInterfaces.hasMoreElements()) {

				NetworkInterface networkInterface = networkInterfaces.nextElement();
				if (networkInterface.getIndex() == index) {
					return networkInterface;
				}

			}

		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
