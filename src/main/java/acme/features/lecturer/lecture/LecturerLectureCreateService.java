
package acme.features.lecturer.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureCreateService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repository;

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
		Lecture object;
		Lecturer lecturer;

		lecturer = this.repository.findOneLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Lecture();
		object.setDraftMode(true);
		object.setLecturer(lecturer);

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

		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "anAbstract", "learningTime", "body", "type", "furtherInformation", "draftMode");

		super.getResponse().setData(tuple);
	}

}
