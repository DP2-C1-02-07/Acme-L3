
package acme.features.authenticated.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.BinderHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuthenticatedAuditorUpdateService extends AbstractService<Authenticated, Auditor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAuditorRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void load() {
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		final Auditor object = this.repository.findOneAuditorByUserAccountId(userAccountId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Auditor object) {
		assert object != null;

		super.bind(object, "firm", "professionalId", "certifications", "link");
	}

	@Override
	public void validate(final Auditor object) {
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean firmHasSpam = !detector.scanString(super.getRequest().getData("firm", String.class));
		super.state(firmHasSpam, "firm", "javax.validation.constraints.HasSpam.message");

		final boolean professionalIdHasSpam = !detector.scanString(super.getRequest().getData("professionalId", String.class));
		super.state(professionalIdHasSpam, "professionalId", "javax.validation.constraints.HasSpam.message");

		final boolean certificationsHasSpam = !detector.scanString(super.getRequest().getData("certifications", String.class));
		super.state(certificationsHasSpam, "certifications", "javax.validation.constraints.HasSpam.message");

		final boolean linkHasSpam = !detector.scanString(super.getRequest().getData("link", String.class));
		super.state(linkHasSpam, "link", "javax.validation.constraints.HasSpam.message");
	}

	@Override
	public void perform(final Auditor object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Auditor object) {
		assert object != null;
		final Tuple tuple = BinderHelper.unbind(object, "firm", "professionalId", "certifications", "link");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
