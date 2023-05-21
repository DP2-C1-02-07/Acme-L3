
package acme.features.lecturer.course;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Course;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseCreateService extends AbstractService<Lecturer, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repository;

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
		Course object;
		Lecturer lecturer;

		lecturer = this.repository.findOneLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Course();
		object.setDraftMode(true);
		object.setLecturer(lecturer);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;

		super.bind(object, "code", "title", "anAbstract", "retailPrice", "furtherInformation");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Course existing;

			existing = this.repository.findOneCourseByCode(object.getCode());
			super.state(existing == null, "code", "lecturer.course.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			super.state(object.getRetailPrice().getAmount() >= 0 && object.getRetailPrice().getAmount() <= 1000000, "retailPrice", "lecturer.course.form.error.retail-price");

			final Money retailPrice = super.getRequest().getData("retailPrice", Money.class);
			final String retailPriceCurrency = retailPrice.getCurrency();
			final String acceptedCurrencies = this.repository.findAcceptedCurrenciesBySystemConfiguration();
			final String[] valores = acceptedCurrencies.split(",");
			final List<String> lsValores = Arrays.asList(valores);
			super.state(lsValores.contains(retailPriceCurrency), "retailPrice", "lecturer.course.form.error.retail-price.currencies");
			super.state(lsValores.contains(retailPriceCurrency), "retailPrice", acceptedCurrencies);
		}

		final SpamDetector detector = new SpamDetector();

		final boolean titleHasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titleHasSpam, "title", "javax.validation.constraints.HasSpam.message");

		final boolean anAbstractHasSpam = !detector.scanString(super.getRequest().getData("anAbstract", String.class));
		super.state(anAbstractHasSpam, "anAbstract", "javax.validation.constraints.HasSpam.message");

		final boolean furtherInformationHasSpam = !detector.scanString(super.getRequest().getData("furtherInformation", String.class));
		super.state(furtherInformationHasSpam, "furtherInformation", "javax.validation.constraints.HasSpam.message");
	}

	@Override
	public void perform(final Course object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "anAbstract", "retailPrice", "furtherInformation", "draftMode");

		super.getResponse().setData(tuple);
	}

}
