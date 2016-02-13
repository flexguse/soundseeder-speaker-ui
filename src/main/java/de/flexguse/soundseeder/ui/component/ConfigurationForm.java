/**
 * 
 */
package de.flexguse.soundseeder.ui.component;

import javax.validation.Validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.ui.validation.ConfigurableBeanValidator;

/**
 * @author Christoph Guse, info@flexguse.de
 *
 */
@SpringComponent
@UIScope
public class ConfigurationForm extends FormLayout implements InitializingBean {

	private static final long serialVersionUID = -350401422797170115L;

	@Autowired
	private AudioInterfaceSelection audioInterfaceSelection;

	@Autowired
	private NetworkInterfaceSelection networkInterfaceSelection;

	@Autowired
	private AudioChannelSelection audioChannelSelection;

	private FieldGroup fieldGroup;

	@Autowired
	private I18N i18n;

	@Autowired
	private Validator validator;

	/**
	 * This helper method creates a {@link ConfigurableBeanValidator} for a
	 * {@link SpeakerConfiguration} class and the given property.
	 * 
	 * @param propertyName
	 * @return
	 */
	private ConfigurableBeanValidator createValidator(String propertyName) {

		ConfigurableBeanValidator beanValidator = new ConfigurableBeanValidator(SpeakerConfiguration.class,
				propertyName);
		beanValidator.setI18n(i18n);
		beanValidator.setValidator(validator);

		return beanValidator;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		fieldGroup = new FieldGroup();

		// add the name input field
		TextField nameInput = new TextField(i18n.get("label.speaker.name"));
		nameInput.setWidth(100, Unit.PERCENTAGE);
		nameInput.setDescription(i18n.get("description.speaker.name"));
		nameInput.addValidator(createValidator(SpeakerConfiguration.ATTR_SPEAKER_NAME));
		fieldGroup.bind(nameInput, SpeakerConfiguration.ATTR_SPEAKER_NAME);
		addComponent(nameInput);

		// add the speaker channel selection
		audioChannelSelection.setWidth(100, Unit.PERCENTAGE);
		audioChannelSelection.setCaption(i18n.get("label.audio.channel"));
		audioChannelSelection.setDescription(i18n.get("description.audio.channel"));
		audioChannelSelection.addValidator(createValidator(SpeakerConfiguration.ATTR_SPEAKER_CHANNEL));
		fieldGroup.bind(audioChannelSelection, SpeakerConfiguration.ATTR_SPEAKER_CHANNEL);
		addComponent(audioChannelSelection);

		// add the network interface selection
		networkInterfaceSelection.setCaption(i18n.get("label.network.interface"));
		networkInterfaceSelection.setWidth(100, Unit.PERCENTAGE);
		networkInterfaceSelection.setDescription(i18n.get("description.network.interface"));
		networkInterfaceSelection.addValidator(createValidator(SpeakerConfiguration.ATTR_NETWORK_INTERFACE));
		fieldGroup.bind(networkInterfaceSelection, SpeakerConfiguration.ATTR_NETWORK_INTERFACE);
		addComponent(networkInterfaceSelection);

		// add the audio interface selection
		audioInterfaceSelection.setCaption(i18n.get("label.soundcard"));
		audioInterfaceSelection.setWidth(100, Unit.PERCENTAGE);
		audioInterfaceSelection.setDescription(i18n.get("description.soundcard"));
		audioInterfaceSelection.addValidator(createValidator(SpeakerConfiguration.ATTR_MIXER));
		fieldGroup.bind(audioInterfaceSelection, SpeakerConfiguration.ATTR_MIXER);
		addComponent(audioInterfaceSelection);

		// add the volume slider
		Slider volume = new Slider(0, 15);
		volume.setWidth(100, Unit.PERCENTAGE);
		volume.setCaption(i18n.get("label.volume"));
		volume.setResolution(0);
		volume.addValidator(createValidator(SpeakerConfiguration.ATTR_VOLUME));
		fieldGroup.bind(volume, SpeakerConfiguration.ATTR_VOLUME);
		addComponent(volume);

		// add the autoplay switch
		CheckBox autoplay = new CheckBox(i18n.get("label.autoplay"));
		autoplay.setDescription(i18n.get("description.autoplay"));
		autoplay.addValidator(createValidator(SpeakerConfiguration.ATTR_AUTOPLAY));
		fieldGroup.bind(autoplay, SpeakerConfiguration.ATTR_AUTOPLAY);
		addComponent(autoplay);

	}

	/**
	 * Commits the changes in the form and fills the data into the
	 * SpeakerConfiguration.
	 * 
	 * @throws CommitException
	 */
	public void commit() throws CommitException {
		fieldGroup.commit();
	}

	/**
	 * Sets the given {@link SpeakerConfiguration} data into the form.
	 * 
	 * @param speakerConfiguration
	 */
	public void setSpeakerConfiguration(SpeakerConfiguration speakerConfiguration) {
		fieldGroup.setItemDataSource(new BeanItem<SpeakerConfiguration>(speakerConfiguration));
	}

	/**
	 * Gets the {@link SpeakerConfiguration} from the form. Do not forget to
	 * call commit() before getting the data!
	 * 
	 * @return
	 */
	public SpeakerConfiguration getSpeakerConfiguration() {

		@SuppressWarnings("unchecked")
		BeanItem<SpeakerConfiguration> beanItem = (BeanItem<SpeakerConfiguration>) fieldGroup.getItemDataSource();
		if (beanItem != null) {
			return beanItem.getBean();
		} else {
			return null;
		}

	}

}
