
package acme.features.student.course;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.Course;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentCourseController extends AbstractController<Student, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentCourseListService	listMineService;

	@Autowired
	protected StudentCourseShowService		showService;
	@Autowired
	protected StudentCourseListLecturesService		lecturesService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);

		super.addBasicCommand("list", this.listMineService);
		super.addCustomCommand("lectures", "list", this.lecturesService);
	}

}
