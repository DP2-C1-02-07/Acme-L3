
package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Course;
import acme.entities.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentUpdateService extends AbstractService<Student, Enrolment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

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
		boolean finalised;
		int masterId;
		Enrolment enrolment;
		Student student;

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentById(masterId);

		if (enrolment != null) {
			student = enrolment.getStudent();
			finalised = enrolment.getCardHolder() != null && enrolment.getCardEnd() != null ? true : false;
			status = super.getRequest().getPrincipal().hasRole(student);
			super.getResponse().setAuthorised(status && !finalised);
		} else
			super.getResponse().setAuthorised(false);

	}

	@Override
	public void load() {
		Enrolment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneEnrolmentById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);
		//System.out.println(this.repository.findAllEnrolmentsCodes());

		super.bind(object, "code", "motivation", "goals", "workTime", "cardHolder", "cardEnd");
		object.setCourse(course);
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			String code;
			String codeBd;
			code = this.repository.findCode(object.getCode());
			codeBd = this.repository.findOneEnrolmentById(object.getId()).getCode();
			super.state(code == null || code == codeBd, "code", "student.enrolment.form.error.code");
		}

		if (!super.getBuffer().getErrors().hasErrors("cardHolder"))
			super.state(object.getCardHolder() == "" || object.getCardHolder().trim().length() > 0, "cardHolder", "student.enrolment.form.error.cardHolder");

		if (!super.getBuffer().getErrors().hasErrors("cardEnd"))
		
			super.state(object.getCardEnd() == "" || object.getCardEnd().matches("^[0-9]{4}$"), "cardEnd", "student.enrolment.form.error.cardNibble");
	
	
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean goalshasSpam = !detector.scanString(super.getRequest().getData("goals", String.class));
		super.state(goalshasSpam, "goals", "Error: Spam detected// Spam detectado");

		final boolean motivationehasSpam = !detector.scanString(super.getRequest().getData("motivation", String.class));
		super.state(motivationehasSpam, "motivation", "Error: Spam detected// Spam detectado");


	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;

		if (object.getCardHolder().trim() == "")
			object.setCardHolder(null);

		if (object.getCardEnd().trim() == "")
			object.setCardEnd(null);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;

		boolean finalised;
		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		finalised = object.getCardHolder().trim() != "" && object.getCardEnd().trim() != "" ? true : false;

		tuple = super.unbind(object, "code", "motivation", "goals", "workTime", "cardHolder", "cardEnd");
		tuple.put("finalised", finalised);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);

	}
}