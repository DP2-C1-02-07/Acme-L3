
package acme.features.company.session;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Practicum;
import acme.entities.Session;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanySessionShowService extends AbstractService<Company, Session> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanySessionRepository repository;

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
		int sessionId;
		final Session session;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);
		status = session != null && super.getRequest().getPrincipal().getActiveRoleId() == session.getPracticum().getCompany().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Session object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSessionById(id);

		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final Session object) {
		assert object != null;
		final Collection<Practicum> practica;
		final SelectChoices choices;
		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();

		practica = this.repository.findManyPracticaByCompanyId(companyId);
		choices = SelectChoices.from(practica, "code", object.getPracticum());
		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractThing", "startDate", "finishDate", "draftMode", "addendum");
		tuple.put("practicum", choices.getSelected().getKey());
		tuple.put("practica", choices);

		final int selectedId = Integer.parseInt(choices.getSelected().getKey());
		final Practicum selectedPracticum = this.repository.findOnePracticaById(selectedId);

		tuple.put("practicum.code", selectedPracticum.getCode());

		super.getResponse().setData(tuple);
	}
}
