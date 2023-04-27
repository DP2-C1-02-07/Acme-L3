
package acme.features.any.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import acme.components.ExchangeRate;
import acme.entities.Course;
import acme.entities.Lecture;
import acme.entities.enums.CourseType;
import acme.framework.components.accounts.Any;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.StringHelper;
import acme.framework.services.AbstractService;

@Service
public class AnyCourseShowService extends AbstractService<Any, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyCourseRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int id;
		Course course;

		id = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseById(id);
		status = course != null && !course.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(id);

		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		Collection<Lecture> lectures;

		lectures = this.repository.findManyLecturesByCourseId(object.getId());

		final CourseType courseType = object.courseType(lectures);

		Money retailPrice;
		Money targetRetailPrice;
		String targetCurrency;

		retailPrice = this.repository.findRetailPriceByCourseId(object.getId());
		targetCurrency = this.repository.findSystemCurrencyBySystemConfiguration();
		targetRetailPrice = this.moneyExchangeCalc(retailPrice, targetCurrency);
		super.state(targetRetailPrice != null, "*", "any.course.money-exchange.form.label.api-error");

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "anAbstract", "retailPrice", "furtherInformation", "draftMode");
		tuple.put("courseType", courseType);
		tuple.put("targetRetailPrice", targetRetailPrice);

		super.getResponse().setData(tuple);
	}

	// Ancillary methods ------------------------------------------------------

	public Money moneyExchangeCalc(final Money retailPrice, final String targetCurrency) {
		assert retailPrice != null;
		assert !StringHelper.isBlank(targetCurrency);

		RestTemplate api;
		ExchangeRate record;
		String retailPriceCurrency;
		Double retailPriceAmount;
		Double targetRetailPriceAmount;
		Double rate;
		Money targetRetailPrice;

		try {
			api = new RestTemplate();

			retailPriceCurrency = retailPrice.getCurrency();
			retailPriceAmount = retailPrice.getAmount();

			record = api.getForObject( //
				"https://api.exchangerate.host/latest?base={0}&symbols={1}", //
				ExchangeRate.class, //
				retailPriceCurrency, //
				targetCurrency //
			);

			assert record != null;
			rate = record.getRates().get(targetCurrency);
			targetRetailPriceAmount = rate * retailPriceAmount;

			targetRetailPrice = new Money();
			targetRetailPrice.setAmount(targetRetailPriceAmount);
			targetRetailPrice.setCurrency(targetCurrency);

		} catch (final Throwable oops) {
			targetRetailPrice = null;
		}

		return targetRetailPrice;
	}

}
