
package acme.features.administrator.offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferCreateService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final Offer object = new Offer();
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;
		super.bind(object, "instantiationMoment", "heading", "summary", "availabilityStart", "availabilityEnd", "price", "link");
	}

	@Override
	public void validate(final Offer object) {
		assert object != null;
		final boolean dayAfterInstantiation = object.getAvailabilityStart().getTime() - object.getInstantiationMoment().getTime() >= 86400000;
		final boolean oneWeek = object.getAvailabilityEnd().getTime() - object.getAvailabilityStart().getTime() >= 604800000;
		final boolean pricePositive = object.getPrice().getAmount() > 0.0;
		super.state(dayAfterInstantiation, "*", "administrator.offer.post.day-after-instantiation");
		super.state(oneWeek, "*", "administrator.offer.post.one-week");
		super.state(pricePositive, "*", "administrator.offer.post.price-positive");
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;
		final Tuple tuple = super.unbind(object, "instantiationMoment", "heading", "summary", "availabilityStart", "availabilityEnd", "price", "link");
		super.getResponse().setData(tuple);
	}

}
