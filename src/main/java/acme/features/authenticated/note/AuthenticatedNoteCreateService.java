
package acme.features.authenticated.note;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Note;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedNoteCreateService extends AbstractService<Authenticated, Note> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedNoteRepository repository;

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
		Note object;
		Date moment;
		UserAccount user;

		moment = MomentHelper.getCurrentMoment();
		final int userId = super.getRequest().getPrincipal().getAccountId();

		user = this.repository.findOneUserAccountById(userId);

		final String author = user.getUsername() + " - " + user.getIdentity().getSurname() + ", " + user.getIdentity().getName();

		object = new Note();
		object.setInstantiationMoment(moment);
		object.setTitle("");
		object.setAuthor(author);
		object.setMessage("");
		object.setEmail("");
		object.setLink("");

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Note object) {
		assert object != null;

		super.bind(object, "instantiationMoment", "title", "author", "message", "email", "link");
	}

	@Override
	public void validate(final Note object) {
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean titlehasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titlehasSpam, "title", "Error: Spam detected// Spam detectado");

		final boolean messagehasSpam = !detector.scanString(super.getRequest().getData("message", String.class));
		super.state(messagehasSpam, "message", "Error: Spam detected// Spam detectado");

		final boolean emailhasSpam = !detector.scanString(super.getRequest().getData("email", String.class));
		super.state(emailhasSpam, "email", "Error: Spam detected// Spam detectado");

		final boolean linkhasSpam = !detector.scanString(super.getRequest().getData("link", String.class));
		super.state(linkhasSpam, "link", "Error: Spam detected// Spam detectado");

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
	}

	@Override
	public void perform(final Note object) {
		assert object != null;

		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Note object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "instantiationMoment", "title", "author", "message", "email", "link");
		tuple.put("confirmation", false);
		tuple.put("readonly", false);

		super.getResponse().setData(tuple);
	}

}
