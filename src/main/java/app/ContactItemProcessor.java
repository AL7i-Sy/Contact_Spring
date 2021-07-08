package app;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
public class ContactItemProcessor implements ItemProcessor<Contact, Contact>{

	@Override
	public Contact process(Contact c) throws Exception {
		return c;
	}

}
