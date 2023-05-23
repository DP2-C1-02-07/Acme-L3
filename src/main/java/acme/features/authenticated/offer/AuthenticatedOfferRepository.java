
package acme.features.authenticated.offer;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Offer;
import acme.framework.components.datatypes.Money;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedOfferRepository extends AbstractRepository {

	@Query("select o from Offer o")
	Collection<Offer> findAllOffers();

	@Query("select o from Offer o where o.id = :id")
	Offer findOneOfferById(int id);

	@Query("select sc.systemCurrency from SystemConfiguration sc")
	String findSystemCurrencyBySystemConfiguration();

	@Query("select o.price from Offer o where o.id = :id")
	Money findPriceByOfferId(int id);

}
