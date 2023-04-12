
package acme.features.authenticated.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Practicum;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedPracticaShowService extends AbstractService<Authenticated, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedPracticaRepository repository;

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
		int practicumId;
		final Course course;

		practicumId = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseByPracticumId(practicumId);
		status = course != null && !course.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOnePracticaById(id);

		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		//final String companyName = object.getCompany().getName();

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "abstractThing", "goals", "estimatedTime", "company.name");

		super.getResponse().setData(tuple);
	}
}
