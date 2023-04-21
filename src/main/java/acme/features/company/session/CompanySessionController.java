
package acme.features.company.session;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Session;
import acme.framework.controllers.AbstractController;
import acme.roles.Company;

@Controller
public class CompanySessionController extends AbstractController<Company, Session> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanySessionListService				listService;

	@Autowired
	protected CompanySessionShowService				showService;

	@Autowired
	protected CompanySessionCreateService			createService;

	@Autowired
	protected CompanySessionCreateAddendumService	createAddendumService;

	@Autowired
	protected CompanySessionDeleteService			deleteService;

	@Autowired
	protected CompanySessionUpdateService			updateService;

	@Autowired
	protected CompanySessionPublishService			publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addCustomCommand("create-addendum", "create", this.createAddendumService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
