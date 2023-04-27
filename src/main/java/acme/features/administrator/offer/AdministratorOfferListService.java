
package acme.features.administrator.offer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import acme.components.ExchangeRate;
import acme.entities.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.StringHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferListService extends AbstractService<Administrator, Offer> {

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
		final Collection<Offer> objects = this.repository.findAllOffers();
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Money price;
		Money targetPrice;
		String targetCurrency;

		price = this.repository.findPriceByOfferId(object.getId());
		targetCurrency = this.repository.findSystemCurrencyBySystemConfiguration();
		targetPrice = this.moneyExchangeCalc(price, targetCurrency);
		super.state(targetPrice != null, "*", "administrator.offer.money-exchange.form.label.api-error");

		final Tuple tuple = super.unbind(object, "instantiationMoment", "heading", "summary", "availabilityStart", "availabilityEnd", "price", "link");
		tuple.put("targetPrice", targetPrice);

		super.getResponse().setData(tuple);
	}

	// Ancillary methods ------------------------------------------------------

	public Money moneyExchangeCalc(final Money price, final String targetCurrency) {
		assert price != null;
		assert !StringHelper.isBlank(targetCurrency);

		RestTemplate api;
		ExchangeRate record;
		String priceCurrency;
		Double priceAmount;
		Double targetPriceAmount;
		Double rate;
		Money targetPrice;

		try {
			api = new RestTemplate();

			priceCurrency = price.getCurrency();
			priceAmount = price.getAmount();

			record = api.getForObject( //
				"https://api.exchangerate.host/latest?base={0}&symbols={1}", //
				ExchangeRate.class, //
				priceCurrency, //
				targetCurrency //
			);

			assert record != null;
			rate = record.getRates().get(targetCurrency);
			targetPriceAmount = rate * priceAmount;

			targetPrice = new Money();
			targetPrice.setAmount(targetPriceAmount);
			targetPrice.setCurrency(targetCurrency);

		} catch (final Throwable oops) {
			targetPrice = null;
		}

		return targetPrice;
	}
}
