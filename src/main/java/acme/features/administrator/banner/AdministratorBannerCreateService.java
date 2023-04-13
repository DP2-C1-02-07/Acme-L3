
package acme.features.administrator.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AdministratorBannerCreateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorBannerRepository repository;

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
		Banner object;
		object = new Banner();
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;

		super.bind(object, "instantationMoment", "displayStartMoment", "displayEndMoment", "pictureLink", "slogan", "documentLink");

	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		boolean validMoment;
		validMoment = object.getInstantationMoment().compareTo(object.getDisplayStartMoment()) < 0;
		super.state(validMoment, "*", "administrator.banner.post.after-instantiation");

		boolean validPeriod;
		validPeriod = object.getDisplayEndMoment().getTime() - object.getDisplayStartMoment().getTime() >= 604800000;
		super.state(validPeriod, "*", "administrator.banner.post.one-week");
	}

	@Override
	public void perform(final Banner object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "instantationMoment", "displayStartMoment", "displayEndMoment", "pictureLink", "slogan", "documentLink");
		super.getResponse().setData(tuple);
	}
}
