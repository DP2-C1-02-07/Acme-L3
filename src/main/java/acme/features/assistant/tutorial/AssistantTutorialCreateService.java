
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Course;
import acme.entities.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialCreateService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Assistant.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		Assistant assistant;

		assistant = this.repository.findOneAssistantById(super.getRequest().getPrincipal().getActiveRoleId());

		object = new Tutorial();
		object.setAssistant(assistant);
		object.setEstimatedTotalTime(0.00);
		object.setDraftMode(true);
		object.setCode("");
		object.setTitle("");
		object.setAbstractTutorial("");
		object.setGoals("");

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;

		int assistantId;
		Assistant assistant;
		int courseId;
		Course course;

		assistantId = super.getRequest().getPrincipal().getActiveRoleId();
		assistant = this.repository.findOneAssistantById(assistantId);
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "code", "title", "abstractTutorial", "goals");
		object.setAssistant(assistant);
		object.setCourse(course);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;
		boolean validTime;

		final SpamDetector detector = new SpamDetector();

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Tutorial existing;

			existing = this.repository.findOneTutorialByCode(object.getCode());
			super.state(existing == null, "code", "assistant.tutorial.form.error.duplicated");
		}
		if (!super.getBuffer().getErrors().hasErrors("estimatedTotalTime")) {
			validTime = object.getEstimatedTotalTime() >= 0.;
			super.state(validTime, "estimatedTotalTime", "assistant.tutorial.form.error.valid-time");
		}

		final boolean titleHasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titleHasSpam, "title", "javax.validation.constraints.HasSpam.message");

		final boolean abstractTutorialHasSpam = !detector.scanString(super.getRequest().getData("abstractTutorial", String.class));
		super.state(abstractTutorialHasSpam, "abstractTutorial", "javax.validation.constraints.HasSpam.message");

		final boolean goalsHasSpam = !detector.scanString(super.getRequest().getData("goals", String.class));
		super.state(goalsHasSpam, "goals", "javax.validation.constraints.HasSpam.message");
	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		SelectChoices choices;
		Collection<Course> courses;
		Tuple tuple;

		courses = this.repository.findNotDraftModeCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());
		tuple = super.unbind(object, "code", "title", "abstractTutorial", "estimatedTotalTime", "goals", "course");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
	}

}
