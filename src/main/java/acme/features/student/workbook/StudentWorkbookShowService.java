
package acme.features.student.workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Enrolment;
import acme.entities.Workbook;
import acme.entities.enums.Type;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentWorkbookShowService extends AbstractService<Student, Workbook> {

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
		int masterId;
		Enrolment enrolment;
		Student student;

		masterId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findOneEnrolmentByWorkbookId(masterId);
		student = enrolment == null ? null : enrolment.getStudent();
		status = super.getRequest().getPrincipal().hasRole(student);

		super.getResponse().setAuthorised(status);

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
	public void unbind(final Workbook object) {
		assert object != null;

		boolean finalised;

		SelectChoices indicators;
		Tuple tuple;

		finalised = object.getEnrolment().getCardHolder() != null && object.getEnrolment().getCardEnd() != null ? true : false;
		indicators = SelectChoices.from(Type.class, object.getType());

		tuple = super.unbind(object, "title", "abstractElement", "type", "periodStart", "periodEnd", "link");
		if (!finalised)
			tuple.put("readonly", true);
		tuple.put("enrolment", object.getEnrolment().getCode());
		tuple.put("indicators", indicators);
		tuple.put("finalised", finalised);

		super.getResponse().setData(tuple);

	}
}