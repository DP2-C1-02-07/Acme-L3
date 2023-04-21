
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.Session;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumListService extends AbstractService<Company, Practicum> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository repository;

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
		Collection<Practicum> object;
		int companyId;

		companyId = super.getRequest().getPrincipal().getActiveRoleId();
		object = this.repository.findPracticaByCompanyId(companyId);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		final Collection<Session> sessions;

		sessions = this.repository.findSessionsByPracticumId(object.getId());
		final Double estimatedTime = object.estimatedTime(sessions);

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "abstractThing", "goals");
		tuple.put("estimatedTime", estimatedTime);

		super.getResponse().setData(tuple);
	}
}
