
package acme.features.company.session;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Practicum;
import acme.entities.Session;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanySessionUpdateService extends AbstractService<Company, Session> {

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
		Session session;
		Company company;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);
		company = session == null ? null : session.getPracticum().getCompany();
		status = session != null && session.isDraftMode() && super.getRequest().getPrincipal().hasRole(company);

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
	public void bind(final Session object) {
		assert object != null;

		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("practicum", int.class);
		practicum = this.repository.findOnePracticaById(practicumId);

		super.bind(object, "title", "abstractThing", "info", "startDate", "finishDate");

		object.setPracticum(practicum);
	}

	@Override
	public void validate(final Session object) {
		assert object != null;

		//Date Validations

		final Date startDate = super.getRequest().getData("startDate", Date.class);
		final Date finishDate = super.getRequest().getData("finishDate", Date.class);
		final Date availableStart = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
		final Date availableEnd = MomentHelper.deltaFromMoment(startDate, 7, ChronoUnit.DAYS);

		final boolean validStart = startDate.getTime() >= availableStart.getTime();
		super.state(validStart, "startDate", "company.session.validation.startDate.error.WeekAhead");

		final boolean validEnd = finishDate.getTime() >= availableEnd.getTime();
		super.state(validEnd, "finishDate", "company.session.validation.finishDate.error.WeekLong");

		//Spam validations

		final SpamDetector detector = new SpamDetector();

		final boolean titlehasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titlehasSpam, "title", "company.session.validation.spam");

		final boolean abstractThingHasSpam = !detector.scanString(super.getRequest().getData("abstractThing", String.class));
		super.state(abstractThingHasSpam, "abstractThing", "company.session.validation.spam");

		final boolean infoHasSpam = !detector.scanString(super.getRequest().getData("info", String.class));
		super.state(infoHasSpam, "info", "company.session.validation.spam");

		//Practicum Validation
		final Collection<Practicum> practica;
		final SelectChoices choices;
		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();

		practica = this.repository.findManyPrivatePracticaByCompanyId(companyId);
		choices = SelectChoices.from(practica, "code", object.getPracticum());

		final int selectedId = Integer.parseInt(choices.getSelected().getKey());
		final Practicum selectedPracticum = this.repository.findOnePracticaById(selectedId);

		final boolean valid = selectedPracticum.isDraftMode();
		super.state(valid, "practicum", "company.session.validation.practicum.error.Published");

	}

	@Override
	public void perform(final Session object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;
		assert object != null;
		final Collection<Practicum> practica;
		final SelectChoices choices;
		final int companyId = super.getRequest().getPrincipal().getActiveRoleId();

		practica = this.repository.findManyPrivatePracticaByCompanyId(companyId);
		choices = SelectChoices.from(practica, "code", object.getPracticum());
		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractThing", "startDate", "finishDate", "draftMode", "addendum", "info");
		tuple.put("practicum", choices.getSelected().getKey());
		tuple.put("practica", choices);

		super.getResponse().setData(tuple);
	}
}
