
package acme.features.lecturer.course;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Course;
import acme.entities.Lecture;
import acme.entities.enums.CourseType;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseUpdateService extends AbstractService<Lecturer, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repository;

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
		Lecturer lecturer;

		id = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseById(id);
		lecturer = course == null ? null : course.getLecturer();
		status = super.getRequest().getPrincipal().hasRole(lecturer) && course != null && course.isDraftMode();

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
	public void bind(final Course object) {
		assert object != null;

		super.bind(object, "code", "title", "anAbstract", "courseType", "retailPrice", "furtherInformation");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Course existing;

			existing = this.repository.findOneCourseByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "lecturer.course.form.error.duplicated");
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

		Collection<Lecture> lectures;

		lectures = this.repository.findManyLecturesByCourseId(object.getId());

		final CourseType courseType = object.courseType(lectures);

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "anAbstract", "retailPrice", "furtherInformation", "draftMode");
		tuple.put("courseType", courseType);

		super.getResponse().setData(tuple);
	}

}
