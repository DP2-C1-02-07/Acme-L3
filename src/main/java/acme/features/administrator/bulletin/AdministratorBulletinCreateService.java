
package acme.features.administrator.bulletin;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Bulletin;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service

public class AdministratorBulletinCreateService extends AbstractService<Administrator, Bulletin> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorBulletinRepository repository;

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
		 Bulletin object ;
		 object= new Bulletin();
		 assert object != null;
		 object.setInstantiationMoment(Date.valueOf(LocalDate.now().minusYears(1)));
		super.getBuffer().setData(object);
		
	}

	@Override
	public void bind(final Bulletin object) {
		
		super.bind(object, "title","instantiationMoment", "flag", "message", "link");
		;
	}

	@Override
	public void validate(final Bulletin object) {
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean titlehasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titlehasSpam, "title", "Error: Spam detected// Spam detectado");

		final boolean messagehasSpam = !detector.scanString(super.getRequest().getData("message", String.class));
		super.state(messagehasSpam, "message", "Error: Spam detected// Spam detectado");

		final boolean linkhasSpam = !detector.scanString(super.getRequest().getData("link", String.class));
		super.state(linkhasSpam, "link", "Error: Spam detected// Spam detectado");

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "javax.validation.constraints.AssertTrue.message");
	}

	@Override
	public void perform(final Bulletin object) {


		this.repository.save(object);	}

	@Override
	public void unbind(final Bulletin object) {
		assert object != null;
		
		final Tuple tuple = super.unbind(object, "title" , "instantiationMoment", "flag", "message" , "link");
;		tuple.put("confirmation", false);

		super.getResponse().setData(tuple);
	}

}
