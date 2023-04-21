
package acme.features.student.workbook;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.SpamDetector;
import acme.entities.Enrolment;
import acme.entities.Workbook;
import acme.entities.enums.Type;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentWorkbookCreateService extends AbstractService<Student, Workbook> {

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
		Workbook object;

		object = new Workbook();
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Workbook object) {
		assert object != null;

		Integer enrolmentId;
		Enrolment enrolment;

		enrolmentId = super.getRequest().getData("enrolment", Integer.class);
		enrolment = enrolmentId != null ? this.repository.findOneEnrolmentById(enrolmentId) : null;
		object.setEnrolment(enrolment);

		super.bind(object, "title", "abstractElement", "type", "periodStart", "periodEnd", "link");

	}

	@Override
	public void validate(final Workbook object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("periodEnd"))
			super.state(MomentHelper.isAfter(object.getPeriodEnd(), object.getPeriodStart()), "periodEnd", "student.activity.form.error.periodEnd");
	

		assert object != null;

		final SpamDetector detector = new SpamDetector();

		final boolean abshasSpam = !detector.scanString(super.getRequest().getData("abstractElement", String.class));
		super.state(abshasSpam, "abstractElement", "Error: Spam detected// Spam detectado");

		final boolean titlehasSpam = !detector.scanString(super.getRequest().getData("title", String.class));
		super.state(titlehasSpam, "title", "Error: Spam detected// Spam detectado");

	
	}

	@Override
	public void perform(final Workbook object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Workbook object) {
		assert object != null;

		int studentId;
		Collection<Enrolment> enrolments;
		SelectChoices types;
		SelectChoices choices;
		Tuple tuple;

		studentId = super.getRequest().getPrincipal().getActiveRoleId();

		enrolments = this.repository.findFinalisedEnrolmentsByStudentId(studentId);
		choices = SelectChoices.from(enrolments, "code", object.getEnrolment());
		types = SelectChoices.from(Type.class, object.getType());

		tuple = super.unbind(object, "title", "abstractElement", "type", "periodStart", "periodEnd", "link");
		tuple.put("enrolment", choices.getSelected().getKey());
		tuple.put("enrolments", choices);
		tuple.put("types", types);
		tuple.put("finalised", true);

		super.getResponse().setData(tuple);
	}

}