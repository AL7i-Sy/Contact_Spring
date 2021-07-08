package app;

import org.springframework.security.core.authority.AuthorityUtils;

public class LoggedUser extends org.springframework.security.core.userdetails.User {

	private User user;

	public LoggedUser(User user) {
		super(user.getUsername(), user.getPasswordHash(),
				AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;

	}

	public User getUser() {
		return user;
	}

	public Integer getId() {
		return user.getId();
	}

	public String getRole() {
		return user.getRole();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public String getFullname() {
		return user.getFullname();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public Company getCompany() {
		return user.getCompany();
	}

}
