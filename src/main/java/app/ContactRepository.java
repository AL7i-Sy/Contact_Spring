package app;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface ContactRepository extends JpaRepository<Contact, Integer> {

	Optional<Contact> findByFirstnameAndLastnameAndPhoneAndAddressAndEmail(String firstname, String lastname,
			String phone, String address, String email);

	//t1:Address,t2:User
	@Query(nativeQuery = true, value = "select t1.* from contact as t1 left outer join user as t2 on t1.userid=t2.id where (t2.role='Administrator' or t2.companyid= :companyid or t1.userid is null) and t1.isapproved =1")
	List<Contact> findByCompany(@Param("companyid") int companyid);
}
