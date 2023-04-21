
package acme.features.authenticated.company;

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
import acme.roles.Company;

@Service
public class AuthenticatedCompanyCreateService extends AbstractService<Authenticated, Company> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedCompanyRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Company.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Company object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		object = new Company();
		object.setUserAccount(userAccount);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Company object) {
		assert object != null;

		super.bind(object, "name", "vat", "summary", "info");
	}

	@Override
	public void validate(final Company object) {
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean nameHasSpam = !detector.scanString(super.getRequest().getData("name", String.class));
		super.state(nameHasSpam, "name", "javax.validation.constraints.HasSpam.message");

		final boolean vatHasSpam = !detector.scanString(super.getRequest().getData("vat", String.class));
		super.state(vatHasSpam, "vat", "javax.validation.constraints.HasSpam.message");

		final boolean summaryHasSpam = !detector.scanString(super.getRequest().getData("summary", String.class));
		super.state(summaryHasSpam, "summary", "javax.validation.constraints.HasSpam.message");

		final boolean infoHasSpam = !detector.scanString(super.getRequest().getData("info", String.class));
		super.state(infoHasSpam, "info", "javax.validation.constraints.HasSpam.message");
	}

	@Override
	public void perform(final Company object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Company object) {
		Tuple tuple;

		tuple = super.unbind(object, "name", "vat", "summary", "info");

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
