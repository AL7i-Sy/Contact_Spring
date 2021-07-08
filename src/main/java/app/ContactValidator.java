package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ContactValidator implements Validator {

	private final ContactRepository repository;

	@Autowired
	public ContactValidator(ContactRepository repo) {
		this.repository = repo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(Contact.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Contact form = (Contact) target;
		validateContact(errors, form);
	}

	boolean isEmptyString(String string) {
		return string == null || string.isEmpty();
	}

	private void validateContact(Errors errors, Contact form) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "firstname.required", "firstname is required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "lastname.required", "lastname is required");

		if (isEmptyString(form.getPhone()) && isEmptyString(form.getAddress()) && isEmptyString(form.getEmail())) {
			errors.reject("contact.empty", "please enter phone and/or address and/or email");
		}

		if (form.getId() == 0) {
			if (repository.findByFirstnameAndLastnameAndPhoneAndAddressAndEmail(form.getFirstname(),
					form.getLastname(), form.getPhone(), form.getAddress(), form.getEmail()).isPresent()) {
				errors.reject("contact.exists", "same contact already exists");
			}
		} else {

			Contact obj = repository.findOne(form.getId());
			if (!obj.equals(form)) {
				if (repository.findByFirstnameAndLastnameAndPhoneAndAddressAndEmail(form.getFirstname(),
						form.getLastname(), form.getPhone(), form.getAddress(), form.getEmail()).isPresent()) {
					errors.reject("contact.exists", "same contact already exists");
				}
			}

		}
	}

}
