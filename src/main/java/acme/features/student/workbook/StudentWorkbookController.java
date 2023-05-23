
package acme.features.student.workbook;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Workbook;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentWorkbookController extends AbstractController<Student, Workbook> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentWorkbookListService	listService;

	@Autowired
	protected StudentWorkbookShowService		showService;

	@Autowired
	protected StudentWorkbookCreateService		createService;

	@Autowired
	protected StudentWorkbookUpdateService		updateService;

	@Autowired
	protected StudentWorkbookDeleteService		deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addBasicCommand( "list", this.listService);

	}
}