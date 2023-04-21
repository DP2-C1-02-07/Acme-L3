
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Course;
import acme.entities.Tutorial;
import acme.entities.TutorialSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialPublishService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository repository;

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
		int tutorialId;
		Tutorial tutorial;
		Assistant assistant;

		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);

		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = tutorial != null && tutorial.isDraftMode() && super.getRequest().getPrincipal().hasRole(assistant);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTutorialById(id);

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
		final boolean validPublish;
		int tutorialId;
		Collection<TutorialSession> tutorialSessions;
		final SpamDetector detector = new SpamDetector();

		tutorialId = super.getRequest().getData("id", int.class);
		tutorialSessions = this.repository.findManyTutorialSessionByTutorialId(tutorialId);
		validPublish = !tutorialSessions.isEmpty();
		super.state(validPublish, "*", "assistant.tutorial.form.error.no-sessions");
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Tutorial existing;

			existing = this.repository.findOneTutorialByCode(object.getCode());
			super.state(existing == null || existing.getCode().equals(object.getCode()) && existing.getId() == object.getId(), "code", "assistant.tutorial.form.error.duplicated");
		}

		final boolean codehasSpam = !detector.scanString(super.getRequest().getData("code", String.class));
		super.state(codehasSpam, "code", "javax.validation.constraints.HasSpam.message");

		final boolean abstractTutorialhasSpam = !detector.scanString(super.getRequest().getData("abstractTutorial", String.class));
		super.state(abstractTutorialhasSpam, "abstractTutorial", "javax.validation.constraints.HasSpam.message");

		final boolean goalshasSpam = !detector.scanString(super.getRequest().getData("goals", String.class));
		super.state(goalshasSpam, "goals", "javax.validation.constraints.HasSpam.message");

	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;

		object.setDraftMode(false);
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
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}

}
