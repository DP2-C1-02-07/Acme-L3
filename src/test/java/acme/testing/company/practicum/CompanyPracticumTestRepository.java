
package acme.testing.company.practicum;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Practicum;
import acme.framework.repositories.AbstractRepository;

public interface CompanyPracticumTestRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.company.userAccount.username = :username")
	Collection<Practicum> findManyPracticumByCompanyUsername(String username);
}
