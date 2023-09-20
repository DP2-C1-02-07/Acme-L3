
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
public class CompanySessionCreateService extends AbstractService<Company, Session> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanySessionRepository repository;

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
		Session object;

		object = new Session();
		object.setDraftMode(true);
		object.setAddendum(false);

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
		if (!super.getBuffer().getErrors().hasErrors("startDate") && !super.getBuffer().getErrors().hasErrors("finishDate")) {
			final Date startDate = super.getRequest().getData("startDate", Date.class);
			final boolean badStartDate = super.getResponse().getErrors().hasErrors("startDate");
			super.state(badStartDate, "startDate", "company.session.validation.startDate.error.WeekAhead");

			final Date finishDate = super.getRequest().getData("finishDate", Date.class);
			if (startDate != null && finishDate != null) {
				final Date availableStart = MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.DAYS);
				final Date availableEnd = MomentHelper.deltaFromMoment(startDate, 7, ChronoUnit.DAYS);

				final boolean validStart = startDate.getTime() >= availableStart.getTime();
				super.state(validStart, "startDate", "company.session.validation.startDate.error.WeekAhead");

				final boolean validEnd = finishDate.getTime() >= availableEnd.getTime();
				super.state(validEnd, "finishDate", "company.session.validation.finishDate.error.WeekLong");

			}
		}

		//Spam validations

		final SpamDetector detector = new SpamDetector();

		final boolean titlehasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titlehasSpam, "title", "company.session.validation.spam");

		final boolean abstractThingHasSpam = !detector.scanString(super.getRequest().getData("abstractThing", String.class));
		super.state(abstractThingHasSpam, "abstractThing", "company.session.validation.spam");

		final boolean infoHasSpam = !detector.scanString(super.getRequest().getData("info", String.class));
		super.state(infoHasSpam, "info", "company.session.validation.spam");

	}

	@Override
	public void perform(final Session object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Session object) {
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
