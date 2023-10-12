
package acme.testing.company.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.Practicum;
import acme.entities.Session;
import acme.framework.repositories.AbstractRepository;

public interface CompanySessionTestRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.company.userAccount.username = :username")
	Collection<Practicum> findManyPracticumByCompanyUsername(String username);

	@Query("select s from Session s where s.practicum.company.userAccount.username = :username")
	Collection<Session> findManySessionByCompanyUsername(String username);
}
