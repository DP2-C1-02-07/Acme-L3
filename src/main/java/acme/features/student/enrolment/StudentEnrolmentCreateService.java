
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
public class StudentEnrolmentCreateService extends AbstractService<Student, Enrolment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

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
		Enrolment object;

		int studentId;
		final Student student;

		object = new Enrolment();

		studentId = super.getRequest().getPrincipal().getActiveRoleId();
		student = this.repository.findOneStudentById(studentId);

		object.setStudent(student);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "code", "motivation", "goals", "workTime", "cardHolder", "cardEnd");
		object.setCourse(course);
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.repository.findCode(object.getCode()) == null, "code", "student.enrolment.form.error.code");

		if (!super.getBuffer().getErrors().hasErrors("cardHolder"))
			super.state(object.getCardHolder() == "" || object.getCardHolder().trim().length() > 0, "cardHolder", "student.enrolment.form.error.cardHolder");

		if (!super.getBuffer().getErrors().hasErrors("cardEnd"))
			super.state(object.getCardEnd() == "" || object.getCardEnd().matches("^[0-9]{4}$"), "cardEnd", "student.enrolment.form.error.cardEnd");

		
		

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

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "motivation", "goals", "workTime", "cardHolder", "cardEnd");
		tuple.put("finalised", false);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}