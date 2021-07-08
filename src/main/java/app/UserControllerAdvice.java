package app;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {

	@ModelAttribute("loggeduser")
	public LoggedUser getLoggedUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		} else {
			LoggedUser cu = (LoggedUser) authentication.getPrincipal();

			return cu;
		}
	}

}
