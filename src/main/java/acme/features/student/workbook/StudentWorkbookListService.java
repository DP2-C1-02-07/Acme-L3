
package acme.features.student.workbook;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.Workbook;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentWorkbookListService extends AbstractService<Student, Workbook> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentWorkbookRepository repository;

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
		Collection<Workbook> objects;
		Principal principal;
	principal = super.getRequest().getPrincipal();
		objects = this.repository.findWorkbookByStudentId(principal.getActiveRoleId());

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Workbook object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title");
		tuple.put("enrolment", object.getEnrolment().getCode());

		super.getResponse().setData(tuple);
	}
}