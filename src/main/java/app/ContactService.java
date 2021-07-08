package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactService {

	@Autowired
	private ContactRepository repoContact;

	public void approveContact(int id) {
		Contact c = repoContact.findOne(id);
		c.setIsapproved(true);
		repoContact.save(c);
	}

	public void deleteContact(int id) {
		repoContact.delete(id);
	}
}
