
package acme.features.any.peep;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Peep;
import acme.framework.components.accounts.Any;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service

public class AnyPeepCreateService extends AbstractService<Any, Peep> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyPeepRepository repository;

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
		final Peep object = new Peep();
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		//userAccountId = principal.getAccountId();
		//	userAccount = this.repository.findOneUserAccountById(userAccountId);
		assert object != null;

		object.setInstantiationMoment(moment);
		object.setTitle("");
		object.setMessage("");
		object.setEmail("");
		object.setLink("");
		boolean status;
		status = super.getRequest().getPrincipal().isAnonymous();
		if (status) {
			object.setNick("");
		} else {
			object.setNick(super.getRequest().getPrincipal().getUsername());
		}
		super.getBuffer().setData(object);

	}

	@Override
	public void bind(final Peep object) {

		super.bind(object, "title", "instantiationMoment", "nick", "message", "link", "email");
		;
	}

	@Override
	public void validate(final Peep object) {
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean titlehasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titlehasSpam, "title", "Error: Spam detected// Spam detectado");

		final boolean messagehasSpam = !detector.scanString(super.getRequest().getData("message", String.class));
		super.state(messagehasSpam, "message", "Error: Spam detected// Spam detectado");

		final boolean linkhasSpam = !detector.scanString(super.getRequest().getData("link", String.class));
		super.state(linkhasSpam, "link", "Error: Spam detected// Spam detectado");

	}

	@Override
	public void perform(final Peep object) {

		this.repository.save(object);
	}

	@Override
	public void unbind(final Peep object) {
		assert object != null;

		final Tuple tuple = super.unbind(object, "title", "instantiationMoment", "nick", "message", "link", "email");

		super.getResponse().setData(tuple);
	}

}
