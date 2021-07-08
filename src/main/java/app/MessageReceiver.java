package app;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

	private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

	@Autowired
	private ContactRepository contactRepo;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RuntimeService runtimeService;

	@RabbitListener(queues = MessageConfig.queueName)
	public void receiveMessage(final CustomMessage customMessage) {

		log.info("Received message as specific class: {}", customMessage.toString());
		Contact c = new Contact();
		c.setAddress(customMessage.getAddress());
		c.setEmail(customMessage.getEmail());
		c.setFirstname(customMessage.getFirstname());
		c.setLastname(customMessage.getLastname());
		c.setPhone(customMessage.getPhone());
		c.setSource(customMessage.source);
		contactRepo.save(c);

		String[] mails = userRepo.findAdministratorsEmails();

		Map<String, Object> vars = new HashMap<String, Object>();

		vars.put("role", "User");
		vars.put("contact", c);
		vars.put("adminEmails", String.join(",", mails));

		runtimeService.startProcessInstanceByKey("myProcess", vars);

	}

}
