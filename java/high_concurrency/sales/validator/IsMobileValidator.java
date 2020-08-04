package high_concurrency.sales.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import high_concurrency.sales.util.ValidatorUtil;
import high_concurrency.sales.validator.IsMobile;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	// allowed to be empty
	private boolean required = false;
	
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			// if required to have a phone number, check whether it is valid
			return ValidatorUtil.validNumber(value);
		}
		else {
			// if not required, check if it is empty
			if(StringUtils.isEmpty(value)) {
				return true;
			}
			// if not, return the validity of the phone number inputed
			else {
				return ValidatorUtil.validNumber(value);
			}
		}
	}

}
