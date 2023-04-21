
package acme.features.authenticated.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class AuthenticatedLecturerCreateService extends AbstractService<Authenticated, Lecturer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedLecturerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Lecturer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Lecturer object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Lecturer();
		object.setUserAccount(userAccount);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecturer object) {
		assert object != null;

		super.bind(object, "almaMater", "resume", "qualifications", "furtherInformation");
	}

	@Override
	public void validate(final Lecturer object) {
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean almaMaterHasSpam = !detector.scanString(super.getRequest().getData("almaMater", String.class));
		super.state(almaMaterHasSpam, "almaMater", "javax.validation.constraints.HasSpam.message");

		final boolean resumeHasSpam = !detector.scanString(super.getRequest().getData("resume", String.class));
		super.state(resumeHasSpam, "resume", "javax.validation.constraints.HasSpam.message");

		final boolean qualificationsHasSpam = !detector.scanString(super.getRequest().getData("qualifications", String.class));
		super.state(qualificationsHasSpam, "qualifications", "javax.validation.constraints.HasSpam.message");

		final boolean furtherInformationHasSpam = !detector.scanString(super.getRequest().getData("furtherInformation", String.class));
		super.state(furtherInformationHasSpam, "furtherInformation", "javax.validation.constraints.HasSpam.message");
	}

	@Override
	public void perform(final Lecturer object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecturer object) {
		Tuple tuple;

		tuple = super.unbind(object, "almaMater", "resume", "qualifications", "furtherInformation");

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
