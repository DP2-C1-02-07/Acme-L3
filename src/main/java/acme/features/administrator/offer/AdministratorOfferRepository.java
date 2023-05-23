
package acme.features.administrator.offer;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Offer;
import acme.framework.components.datatypes.Money;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorOfferRepository extends AbstractRepository {

	@Query("select o from Offer o")
	Collection<Offer> findAllOffers();

	@Query("select o from Offer o where o.id = :id")
	Offer findOneOfferById(int id);

	@Query("select sc.systemCurrency from SystemConfiguration sc")
	String findSystemCurrencyBySystemConfiguration();

	@Query("select sc.acceptedCurrencies from SystemConfiguration sc")
	String findAcceptedCurrenciesBySystemConfiguration();

	@Query("select o.price from Offer o where o.id = :id")
	Money findPriceByOfferId(int id);

}
