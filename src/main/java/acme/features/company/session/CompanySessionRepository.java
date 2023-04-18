
package acme.features.company.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Practicum;
import acme.entities.Session;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface CompanySessionRepository extends AbstractRepository {

	@Query("Select s from Session s Where s.id = :id")
	Session findOneSessionById(int id);

	@Query("Select s From Session s Where s.practicum.id = :id")
	Collection<Session> findManySessionsByPracticumId(int id);

	@Query("Select s From Session s Where s.practicum.company.id = :id")
	Collection<Session> findManySessionsByCompanyId(int id);

	@Query("Select s From Session s Where s.practicum.id = :id And s.addendum = true")
	Collection<Session> findAddendumSessionsByPracticumId(int id);

	@Query("Select p From Practicum p Where p.company.id = :id")
	Collection<Practicum> findManyPracticaByCompanyId(int id);

	@Query("Select p From Practicum p Where p.company.id = :id And p.draftMode = true")
	Collection<Practicum> findManyPrivatePracticaByCompanyId(int id);

	@Query("Select p From Practicum p Where p.company.id = :id And p.draftMode = false")
	Collection<Practicum> findManyPublishedPracticaByCompanyId(int id);

	@Query("Select p From Practicum p Where p.id = :id")
	Practicum findOnePracticaById(int id);

	@Query("Select s.practicum From Session s Where s.id = :id")
	Practicum findOnePracticumBySessionId(int id);

	@Query("Select p From Practicum p where p.draftMode = false")
	Collection<Practicum> findPublishedPractica();

	@Query("Select p From Practicum p where p.draftMode = true")
	Collection<Practicum> findPrivatePractica();

	@Query("Select c From Company c Where c.id = :id")
	Company findOneCompanyById(int id);

	@Query("Select p From Practicum p")
	Collection<Practicum> findAllPractica();
}
