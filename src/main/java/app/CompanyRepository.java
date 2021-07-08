package app;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	
}
