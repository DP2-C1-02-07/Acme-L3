
package acme.features.administrator.offer;

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
public class AdministratorOfferShowService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final int id = super.getRequest().getData("id", int.class);
		final Offer object = this.repository.findOneOfferById(id);
		super.getBuffer().setData(object);
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
