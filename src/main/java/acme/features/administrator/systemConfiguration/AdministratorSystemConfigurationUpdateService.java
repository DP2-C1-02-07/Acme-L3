
package acme.features.administrator.systemConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.SystemConfiguration;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorSystemConfigurationUpdateService extends AbstractService<Administrator, SystemConfiguration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorSystemConfigurationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SystemConfiguration object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findSystemConfigurationById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final SystemConfiguration object) {
		assert object != null;

		super.bind(object, "systemCurrency", "acceptedCurrencies");
	}

	@Override
	public void validate(final SystemConfiguration object) {
		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean systemCurrencyHasSpam = !detector.scanString(super.getRequest().getData("systemCurrency", String.class));
		super.state(systemCurrencyHasSpam, "systemCurrency", "javax.validation.constraints.HasSpam.message");

		final boolean acceptedCurrenciesHasSpam = !detector.scanString(super.getRequest().getData("acceptedCurrencies", String.class));
		super.state(acceptedCurrenciesHasSpam, "acceptedCurrencies", "javax.validation.constraints.HasSpam.message");
	}

	@Override
	public void perform(final SystemConfiguration object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final SystemConfiguration object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "systemCurrency", "acceptedCurrencies");

		super.getResponse().setData(tuple);
	}

}
