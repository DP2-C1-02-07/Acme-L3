
package acme.features.lecturer.lecturerDashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.forms.LecturerDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerDashboardShowService extends AbstractService<Lecturer, LecturerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerDashboardRepository repository;

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
		Principal principal;
		int lecturerId;

		principal = super.getRequest().getPrincipal();
		lecturerId = principal.getActiveRoleId();

		List<Double> learningTimeOfCourses;

		LecturerDashboard lecturerDashboard;
		Integer totalNumberOfTheoryLectures;
		Integer totalNumberOfHandsOnLectures;
		Double averageLearningTimeOfLectures;
		Double deviationLearningTimeOfLectures;
		Double minimumLearningTimeOfLectures;
		Double maximumLearningTimeOfLectures;
		Double averageLearningTimeOfCoursesCalc;
		Double deviationLearningTimeOfCoursesCalc;
		Double minimumLearningTimeOfCoursesCalc;
		Double maximumLearningTimeOfCoursesCalc;

		learningTimeOfCourses = this.repository.learningTimeOfCourses(lecturerId);

		totalNumberOfTheoryLectures = this.repository.totalNumberOfTheoryLectures(lecturerId);
		totalNumberOfHandsOnLectures = this.repository.totalNumberOfHandsOnLectures(lecturerId);
		averageLearningTimeOfLectures = this.repository.averageLearningTimeOfLectures(lecturerId);
		deviationLearningTimeOfLectures = this.repository.deviationLearningTimeOfLectures(lecturerId);
		minimumLearningTimeOfLectures = this.repository.minimumLearningTimeOfLectures(lecturerId);
		maximumLearningTimeOfLectures = this.repository.maximumLearningTimeOfLectures(lecturerId);

		lecturerDashboard = new LecturerDashboard();

		averageLearningTimeOfCoursesCalc = lecturerDashboard.averageLearningTimeOfCoursesCalc(learningTimeOfCourses);
		deviationLearningTimeOfCoursesCalc = lecturerDashboard.deviationLearningTimeOfCoursesCalc(learningTimeOfCourses);
		minimumLearningTimeOfCoursesCalc = lecturerDashboard.minimumLearningTimeOfCoursesCalc(learningTimeOfCourses);
		maximumLearningTimeOfCoursesCalc = lecturerDashboard.maximumLearningTimeOfCoursesCalc(learningTimeOfCourses);

		lecturerDashboard.setTotalNumberOfTheoryLectures(totalNumberOfTheoryLectures);
		lecturerDashboard.setTotalNumberOfHandsOnLectures(totalNumberOfHandsOnLectures);
		lecturerDashboard.setAverageLearningTimeOfLectures(averageLearningTimeOfLectures);
		lecturerDashboard.setDeviationLearningTimeOfLectures(deviationLearningTimeOfLectures);
		lecturerDashboard.setMinimumLearningTimeOfLectures(minimumLearningTimeOfLectures);
		lecturerDashboard.setMaximumLearningTimeOfLectures(maximumLearningTimeOfLectures);
		lecturerDashboard.setAverageLearningTimeOfCourses(averageLearningTimeOfCoursesCalc);
		lecturerDashboard.setDeviationLearningTimeOfCourses(deviationLearningTimeOfCoursesCalc);
		lecturerDashboard.setMinimumLearningTimeOfCourses(minimumLearningTimeOfCoursesCalc);
		lecturerDashboard.setMaximumLearningTimeOfCourses(maximumLearningTimeOfCoursesCalc);

		super.getBuffer().setData(lecturerDashboard);
	}

	@Override
	public void unbind(final LecturerDashboard object) {
		Tuple tuple;

		tuple = super.unbind(object, "totalNumberOfTheoryLectures", "totalNumberOfHandsOnLectures", "averageLearningTimeOfLectures", // 
			"deviationLearningTimeOfLectures", "minimumLearningTimeOfLectures", "maximumLearningTimeOfLectures", //
			"averageLearningTimeOfCourses", "deviationLearningTimeOfCourses", "minimumLearningTimeOfCourses", "maximumLearningTimeOfCourses");

		super.getResponse().setData(tuple);
	}

}
