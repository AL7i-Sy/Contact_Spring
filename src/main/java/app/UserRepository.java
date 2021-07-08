package app;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	
	@Query(nativeQuery = true,value =  "select distinct u.email from user as u where u.role = 'Administrator'")
	String[] findAdministratorsEmails();
	
	
}
