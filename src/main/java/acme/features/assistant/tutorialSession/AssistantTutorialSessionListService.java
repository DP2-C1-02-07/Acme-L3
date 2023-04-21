
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.entities.TutorialSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionListService extends AbstractService<Assistant, TutorialSession> {

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
		final boolean status;
		int masterId;
		Tutorial tutorial;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		masterId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(masterId);
		status = tutorial != null && (!tutorial.isDraftMode() || tutorial.getAssistant().getId() == principal.getActiveRoleId());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TutorialSession> objects;
		int tutorialId;

		tutorialId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findManySessionsByTutorialId(tutorialId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "sessionType", "startDate", "finishDate");
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<TutorialSession> objects) {
		assert objects != null;

		final int tutorialId;
		Tutorial tutorial;
		final boolean showCreate;

		tutorialId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findOneTutorialById(tutorialId);
		showCreate = tutorial.isDraftMode() && super.getRequest().getPrincipal().hasRole(tutorial.getAssistant());

		super.getResponse().setGlobal("masterId", tutorialId);
		super.getResponse().setGlobal("showCreate", showCreate);
	}
}
