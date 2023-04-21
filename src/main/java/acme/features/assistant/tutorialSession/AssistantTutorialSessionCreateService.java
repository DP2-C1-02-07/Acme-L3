
package acme.features.assistant.tutorialSession;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Tutorial;
import acme.entities.TutorialSession;
import acme.entities.enums.TutorialSessionType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionCreateService extends AbstractService<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int tutorialId;
		Tutorial tutorial;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		tutorialId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);
		status = tutorial != null && (!tutorial.isDraftMode() || tutorial.getAssistant().getId() == principal.getActiveRoleId());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final TutorialSession object;
		int tutorialId;
		Tutorial tutorial;

		tutorialId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);

		object = new TutorialSession();
		object.setTutorial(tutorial);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final TutorialSession object) {
		assert object != null;

		super.bind(object, "title", "abstractSession", "sessionType", "startDate", "finishDate", "info");
	}

	@Override
	public void validate(final TutorialSession object) {
		assert object != null;
		Date actualDate;
		boolean validStartDate;
		boolean minDuration;
		final boolean maxDuration;
		actualDate = MomentHelper.getCurrentMoment();
		final SpamDetector detector = new SpamDetector();

		if (!super.getBuffer().getErrors().hasErrors("startDate")) {
			validStartDate = MomentHelper.isLongEnough(actualDate, object.getStartDate(), 1, ChronoUnit.DAYS);
			super.state(validStartDate, "startDate", "assistant.tutorial.session.form.error.startDate-before-actualDate");
		}

		if (!super.getBuffer().getErrors().hasErrors("startDate") && !super.getBuffer().getErrors().hasErrors("finishDate")) {
			minDuration = MomentHelper.isLongEnough(object.getStartDate(), object.getFinishDate(), 1, ChronoUnit.HOURS);
			super.state(minDuration, "startDate", "assistant.tutorial.session.form.error.durationMin");
			maxDuration = MomentHelper.computeDuration(object.getStartDate(), object.getFinishDate()).getSeconds() <= Duration.ofHours(5).getSeconds();
			super.state(maxDuration, "finishDate", "assistant.tutorial.session.form.error.durationMax");
		}

		if (!super.getBuffer().getErrors().hasErrors("finishDate") && !super.getBuffer().getErrors().hasErrors("startDate")) {
			validStartDate = MomentHelper.isAfter(object.getFinishDate(), object.getStartDate());
			super.state(validStartDate, "finishDate", "assistant.tutorial.session.form.error.finishDate-before-startDate");
		}

		final boolean titlehasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titlehasSpam, "title", "javax.validation.constraints.HasSpam.message");

		final boolean abstractSessionhasSpam = !detector.scanString(super.getRequest().getData("abstractSession", String.class));
		super.state(abstractSessionhasSpam, "abstractSession", "javax.validation.constraints.HasSpam.message");

	}

	@Override
	public void perform(final TutorialSession object) {
		assert object != null;

		Tutorial tutorial;
		double totalHours;
		double sessionHours;
		double validFormatSessionHours;
		Duration sessionDuration;
		String formattedSessionHours;

		tutorial = object.getTutorial();
		totalHours = 0.;
		sessionDuration = MomentHelper.computeDuration(object.getStartDate(), object.getFinishDate());
		sessionHours = sessionDuration.getSeconds() / 3600.;
		formattedSessionHours = String.format("%.2f", sessionHours);
		validFormatSessionHours = Double.parseDouble(formattedSessionHours);

		totalHours = validFormatSessionHours + tutorial.getEstimatedTotalTime();

		tutorial.setEstimatedTotalTime(totalHours);

		this.repository.save(tutorial);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;
		SelectChoices choices;
		Tutorial tutorial;
		boolean draftMode;

		tutorial = object.getTutorial();
		draftMode = tutorial.isDraftMode();
		choices = SelectChoices.from(TutorialSessionType.class, object.getSessionType());
		tuple = super.unbind(object, "title", "abstractSession", "sessionType", "startDate", "finishDate", "info");
		tuple.put("masterId", super.getRequest().getData("masterId", int.class));
		tuple.put("sessionType", choices);
		tuple.put("draftMode", draftMode);
		super.getResponse().setData(tuple);
	}

}
