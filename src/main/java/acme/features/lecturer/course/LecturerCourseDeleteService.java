
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.entities.AuditingRecords;
import acme.entities.Course;
import acme.entities.CourseLecture;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseDeleteService extends AbstractService<Lecturer, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repository;

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
		int id;
		Course course;
		Lecturer lecturer;

		id = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseById(id);
		lecturer = course == null ? null : course.getLecturer();
		status = super.getRequest().getPrincipal().hasRole(lecturer) && course != null && course.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;

		super.bind(object, "code", "title", "anAbstract", "courseType", "retailPrice", "furtherInformation");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
	}

	@Override
	public void perform(final Course object) {
		assert object != null;

		Collection<CourseLecture> courseLecture;
		courseLecture = this.repository.findManyCourseLectureByCourseId(object.getId());
		this.repository.deleteAll(courseLecture);

		Collection<AuditingRecords> auditingRecords;
		auditingRecords = this.repository.findManyAuditingRecordsByAuditId(object.getId());
		this.repository.deleteAll(auditingRecords);

		Collection<Audit> audits;
		audits = this.repository.findManyAuditsByCourseId(object.getId());
		this.repository.deleteAll(audits);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "anAbstract", "courseType", "retailPrice", "furtherInformation", "draftMode");

		super.getResponse().setData(tuple);
	}

}
