/**
 * 
 */
package de.flexguse.soundseeder.ui.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.spring.i18n.I18N;

import com.vaadin.data.validator.BeanValidator;

import lombok.Setter;

/**
 * In this special {@link BeanValidator} the {@link Validator} can be configured
 * which is used for validation.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class ConfigurableBeanValidator extends BeanValidator {

	private static final long serialVersionUID = 8791635134413635042L;

	private Class<?> beanClass;
	private String propertyName;

	@Setter
	private Validator validator;

	@Setter
	private I18N i18n;

	public ConfigurableBeanValidator(Class<?> beanClass, String propertyName) {
		super(beanClass, propertyName);
		this.beanClass = beanClass;
		this.propertyName = propertyName;
	}

	@Override
	protected Validator getJavaxBeanValidator() {
		return this.validator;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		Set<?> violations = getJavaxBeanValidator().validateValue(beanClass, propertyName, value);
		if (!violations.isEmpty()) {
			InvalidValueException[] causes = new InvalidValueException[violations.size()];
			int i = 0;
			for (Object v : violations) {
				final ConstraintViolation<?> violation = (ConstraintViolation<?>) v;

				String errorKey = violation.getMessageTemplate();

				errorKey = StringUtils.removeStart(errorKey, "{");
				errorKey = StringUtils.removeEnd(errorKey, "}");

				String msg = i18n.get(errorKey, value);
				causes[i] = new InvalidValueException(msg);
				++i;
			}

			throw new InvalidValueException(null, causes);
		}
	}

}
