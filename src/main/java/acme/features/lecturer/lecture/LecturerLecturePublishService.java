
package acme.features.lecturer.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Lecture;
import acme.entities.enums.Type;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLecturePublishService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repository;

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
		Lecture lecture;
		Lecturer lecturer;

		id = super.getRequest().getData("id", int.class);
		lecture = this.repository.findOneLectureById(id);
		lecturer = lecture == null ? null : lecture.getLecturer();
		status = lecture != null && lecture.isDraftMode() && super.getRequest().getPrincipal().hasRole(lecturer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Lecture object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneLectureById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;

		super.bind(object, "title", "anAbstract", "learningTime", "body", "type", "furtherInformation");
	}

	@Override
	public void validate(final Lecture object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("learningTime"))
			super.state(object.getLearningTime() >= 0.01, "learningTime", "lecturer.lecture.form.error.learning-time");

		final SpamDetector detector = new SpamDetector();

		final boolean titleHasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titleHasSpam, "title", "javax.validation.constraints.HasSpam.message");

		final boolean anAbstractHasSpam = !detector.scanString(super.getRequest().getData("anAbstract", String.class));
		super.state(anAbstractHasSpam, "anAbstract", "javax.validation.constraints.HasSpam.message");

		final boolean furtherInformationHasSpam = !detector.scanString(super.getRequest().getData("furtherInformation", String.class));
		super.state(furtherInformationHasSpam, "furtherInformation", "javax.validation.constraints.HasSpam.message");

		final boolean bodyHasSpam = !detector.scanString(super.getRequest().getData("body", String.class));
		super.state(bodyHasSpam, "body", "javax.validation.constraints.HasSpam.message");
	}

	@Override
	public void perform(final Lecture object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;

		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(Type.class, object.getType());

		tuple = super.unbind(object, "title", "anAbstract", "learningTime", "body", "type", "furtherInformation", "draftMode");
		tuple.put("types", choices);

		super.getResponse().setData(tuple);
	}

}
