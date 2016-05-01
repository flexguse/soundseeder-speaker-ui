package de.flexguse.soundseeder.ui.converter;

import java.net.NetworkInterface;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.flexguse.soundseeder.service.NetworkInterfaceService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * This converter implementation converts NetworkInterface to an index
 * representation and vice versa.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
public class NetworkInterfaceToIndexConverter implements Converter<Object, Integer> {

	private static final long serialVersionUID = 3358579355567334853L;

	private NetworkInterfaceService networkInterfaceService;

	@Override
	public Integer convertToModel(Object value, Class<? extends Integer> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if(value != null){
			return ((NetworkInterface)value).getIndex();
		}
		
		return null;
	}

	@Override
	public Object convertToPresentation(Integer value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		if(value != null){
			return networkInterfaceService.getNetworkInterfaceByIndex((Integer) value);
		}
		
		return null;
	}

	@Override
	public Class<Integer> getModelType() {
		return Integer.class;
	}

	@Override
	public Class<Object> getPresentationType() {
		return Object.class;
	}

//	@Override
//	public Object convertToModel(Object value, Class<? extends Integer> targetType, Locale locale)
//			throws com.vaadin.data.util.converter.Converter.ConversionException {
//
//		if (value != null) {
//			return networkInterfaceService.getNetworkInterfaceByIndex((Integer) value);
//		}
//
//		return null;
//
//	}
//
//	@Override
//	public Integer convertToPresentation(Integer value, Class<? extends Object> targetType, Locale locale)
//			throws com.vaadin.data.util.converter.Converter.ConversionException {
//
//		if (value != null) {
//			return value.getIndex();
//		}
//
//		return null;
//
//	}
//
//	@Override
//	public Class<NetworkInterface> getModelType() {
//		return NetworkInterface.class;
//	}
//
//	@Override
//	public Class<Object> getPresentationType() {
//		return Object.class;
//	}

	
	
}
