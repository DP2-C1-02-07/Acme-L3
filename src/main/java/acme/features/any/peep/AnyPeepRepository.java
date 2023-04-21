
package acme.features.any.peep;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Peep;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnyPeepRepository extends AbstractRepository {

	@Query("select c from Peep c")
	Collection<Peep> findAllPeeps();

	@Query("select c from Peep c where c.id = :id")
	Peep findOnePeepById(int id);

	
}
