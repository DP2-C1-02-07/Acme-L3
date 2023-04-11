
package acme.features.auditor;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditCreateService extends AbstractService<Auditor, Audit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuditorAuditRepository repository;

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
		final Auditor auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());
		final Audit object = new Audit();
		object.setAuditor(auditor);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;
		final int courseId = super.getRequest().getData("course", int.class);
		final Collection<Course> course = this.repository.findOneCourseById(courseId);
		final Optional<Course> firstCourse = course.stream().findFirst();
		assert firstCourse.isPresent();

		super.bind(object, "code", "conclusion", "strongPoints", "weakPoints");
		object.setCourse(firstCourse.get());
	}

	@Override
	public void validate(final Audit object) {
		assert object != null;
	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;
		final Collection<Course> courses = this.repository.findAllCourses();
		final SelectChoices choices = SelectChoices.from(courses, "code", object.getCourse());
		final Tuple tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
