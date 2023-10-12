
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Course;
import acme.entities.Practicum;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumCreateService extends AbstractService<Company, Practicum> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

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
		Practicum object;
		Company company;

		company = this.repository.findOneCompanyById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Practicum();
		object.setDraftMode(true);
		object.setCompany(company);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "code", "title", "abstractThing", "goals");

		object.setCourse(course);

	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		//Spam validation
		final SpamDetector detector = new SpamDetector();

		final boolean titleHasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titleHasSpam, "title", "javax.validation.constraints.HasSpam.message");

		final boolean abstractThingHasSpam = !detector.scanString(super.getRequest().getData("abstractThing", String.class));
		super.state(abstractThingHasSpam, "abstractThing", "javax.validation.constraints.HasSpam.message");

		final boolean goalsHasSpam = !detector.scanString(super.getRequest().getData("goals", String.class));
		super.state(goalsHasSpam, "goals", "javax.validation.constraints.HasSpam.message");

		//Duplicate code validation

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Practicum existing;

			existing = this.repository.findOnePracticaByCode(object.getCode());
			super.state(existing == null || object.equals(existing), "code", "company.practicum.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;
		final Collection<Course> courses;
		final SelectChoices choices;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "abstractThing", "goals", "draftMode");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}
}
