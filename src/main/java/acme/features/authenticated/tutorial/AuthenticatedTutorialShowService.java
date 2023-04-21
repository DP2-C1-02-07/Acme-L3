
package acme.features.authenticated.tutorial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedTutorialShowService extends AbstractService<Authenticated, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedTutorialRepository repository;

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
		Tutorial tutorial;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		id = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialByTutorialId(id);
		status = tutorial != null && !tutorial.isDraftMode() && principal.isAuthenticated();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		int tutorialId;

		tutorialId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTutorialByTutorialId(tutorialId);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "course.title", "abstractTutorial", "goals", "estimatedTotalTime", "assistant.supervisor", "assistant.expertiseFields", "assistant.resume", "assistant.link");
		super.getResponse().setData(tuple);
	}

}
