
package acme.features.assistant.tutorialSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.TutorialSession;
import acme.framework.controllers.AbstractController;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionController extends AbstractController<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionListService	listService;

	@Autowired
	protected AssistantTutorialSessionShowService	showService;

	/*
	 * @Autowired
	 * protected AssistantSessionTutorialCreateService createService;
	 * 
	 * @Autowired
	 * protected AssistantSessionTutorialUpdateService updateService;
	 * 
	 * @Autowired
	 * protected AssistantSessionTutorialDeleteService deleteService;
	 * 
	 */

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		//super.addBasicCommand("create", this.createService);
		//super.addBasicCommand("update", this.updateService);
		//super.addBasicCommand("delete", this.deleteService);
	}
}
