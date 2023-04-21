
package acme.features.authenticated.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Course;
import acme.entities.Practicum;
import acme.entities.Session;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedPracticaListService extends AbstractService<Authenticated, Practicum> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedPracticaRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("courseId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		final int courseId;
		final Course course;

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findOneCourseById(courseId);
		status = !course.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Practicum> object;
		int courseId;

		courseId = super.getRequest().getData("courseId", int.class);
		object = this.repository.findPracticaByCourseId(courseId);

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
