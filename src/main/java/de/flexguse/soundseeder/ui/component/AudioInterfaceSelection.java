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

import de.flexguse.soundseeder.service.AudioInterfaceService;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringComponent
@UIScope
public class AudioInterfaceSelection extends ComboBox implements InitializingBean {

	private static final long serialVersionUID = 1645671332251008180L;

	@Autowired
	private AudioInterfaceService audioInterfaceService;

	@Override
	public void afterPropertiesSet() throws Exception {

		IndexedContainer container = new IndexedContainer();

		audioInterfaceService.getMixers().forEach(mixerInfo -> {
			container.addItem(mixerInfo.getIndex());
			setItemCaption(mixerInfo.getIndex(), mixerInfo.getName());
		});

		setContainerDataSource(container);

		setNullSelectionAllowed(false);

	}

}
