
package acme.features.student.workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Enrolment;
import acme.entities.Workbook;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentWorkbookDeleteService extends AbstractService<Student, Workbook> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentWorkbookRepository repository;

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
		boolean finalised;
		int masterId;
		Enrolment enrolment;
		Student student;

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentByWorkbookId(masterId);

		if (enrolment != null) {
			student = enrolment.getStudent();
			finalised = enrolment.getCardHolder() != null && enrolment.getCardEnd() != null ? true : false;
			status = super.getRequest().getPrincipal().hasRole(student);
			super.getResponse().setAuthorised(status && finalised);
		} else
			super.getResponse().setAuthorised(false);

	}

	@Override
	public void load() {
		Workbook object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneWorkbookById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Workbook object) {
		assert object != null;

		super.bind(object, "title", "abstractElement", "type", "periodStart", "periodEnd", "link");
	}

	@Override
	public void validate(final Workbook object) {
		assert object != null;
	}

	@Override
	public void perform(final Workbook object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Workbook object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractElement", "type", "periodStart", "periodEnd", "link");

		super.getResponse().setData(tuple);
	}

}