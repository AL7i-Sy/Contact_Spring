package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Override
	public LoggedUser loadUserByUsername(String username) throws UsernameNotFoundException {
		
		 User user = repository.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found", username)));
		 
		return new LoggedUser(user);
	}

}
