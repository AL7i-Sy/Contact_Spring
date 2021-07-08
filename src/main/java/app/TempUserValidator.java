package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TempUserValidator implements Validator {

	private final UserRepository repository;

	@Autowired
	public TempUserValidator(UserRepository userRepo) {
		this.repository = userRepo;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(TempUser.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		TempUser form = (TempUser) target;
		validateUser(errors, form);
	}

	private void validateUser(Errors errors, TempUser form) {

		if (form.getId() == 0) {
			if (!form.getPassword().equals(form.getPassword2())) {
				errors.reject("password.no_match", "Passwords do not match");
			}
		}

		if (form.getId() == 0) {

			if (repository.findByEmail(form.getEmail()).isPresent()) {
				errors.reject("email.exists", "User with this email already exists");
			}
		} else {

			User dbUser = repository.findOne(form.getId());
			if (!dbUser.getEmail().equals(form.getEmail())) {
				if (repository.findByEmail(form.getEmail()).isPresent()) {
					errors.reject("email.exists", "User with this email already exists");
				}
			}

		}

		if (form.getId() == 0) {
			if (repository.findByUsername(form.getUsername()).isPresent()) {
				errors.reject("username.exists", "User with this username already exists");
			}
		} else {
			User dbUser = repository.findOne(form.getId());
			if (!dbUser.getUsername().equals(form.getUsername())) {
				if (repository.findByUsername(form.getUsername()).isPresent()) {
					errors.reject("username.exists", "User with this username already exists");
				}
			}
		}
	}
}
