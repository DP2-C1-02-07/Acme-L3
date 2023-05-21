
package acme.features.administrator.offer;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.datatypes.Money;
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

		final SpamDetector detector = new SpamDetector();

		if (!super.getBuffer().getErrors().hasErrors("availabilityStart") && !super.getBuffer().getErrors().hasErrors("instantiationMoment")) {
			final boolean dayAfterInstantiation = object.getAvailabilityStart().getTime() - object.getInstantiationMoment().getTime() >= 86400000;
			super.state(dayAfterInstantiation, "*", "administrator.offer.post.day-after-instantiation");
		}

		if (!super.getBuffer().getErrors().hasErrors("availabilityStart") && !super.getBuffer().getErrors().hasErrors("availabilityEnd")) {
			final boolean oneWeek = object.getAvailabilityEnd().getTime() - object.getAvailabilityStart().getTime() >= 604800000;
			super.state(oneWeek, "*", "administrator.offer.post.one-week");
		}

		if (!super.getBuffer().getErrors().hasErrors("price")) {
			final boolean pricePositive = object.getPrice().getAmount() > 0.0;
			super.state(pricePositive, "*", "administrator.offer.post.price-positive");

			final Money price = super.getRequest().getData("price", Money.class);
			final String priceCurrency = price.getCurrency();
			final String acceptedCurrencies = this.repository.findAcceptedCurrenciesBySystemConfiguration();
			final String[] valores = acceptedCurrencies.split(",");
			final List<String> lsValores = Arrays.asList(valores);
			super.state(lsValores.contains(priceCurrency), "price", "administrator.offer.form.error.price.currencies");
			super.state(lsValores.contains(priceCurrency), "price", acceptedCurrencies);
		}

		final boolean headingHasSpam = !detector.scanString(super.getRequest().getData("heading", String.class));
		super.state(headingHasSpam, "heading", "javax.validation.constraints.HasSpam.message");

		final boolean summaryHasSpam = !detector.scanString(super.getRequest().getData("summary", String.class));
		super.state(summaryHasSpam, "summary", "javax.validation.constraints.HasSpam.message");

		final boolean linkHasSpam = !detector.scanString(super.getRequest().getData("link", String.class));
		super.state(linkHasSpam, "link", "javax.validation.constraints.HasSpam.message");
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
