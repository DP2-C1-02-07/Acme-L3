
package acme.features.lecturer.lecturerDashboard;

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

		LecturerDashboard lecturerDashboard;
		Integer totalNumberOfTheoryLectures;
		Integer totalNumberOfHandsOnLectures;
		Double averageLearningTimeOfLectures;
		Double deviationLearningTimeOfLectures;
		Double minimumLearningTimeOfLectures;
		Double maximumLearningTimeOfLectures;

		totalNumberOfTheoryLectures = this.repository.totalNumberOfTheoryLectures(lecturerId);
		totalNumberOfHandsOnLectures = this.repository.totalNumberOfHandsOnLectures(lecturerId);
		averageLearningTimeOfLectures = this.repository.averageLearningTimeOfLectures(lecturerId);
		deviationLearningTimeOfLectures = this.repository.deviationLearningTimeOfLectures(lecturerId);
		minimumLearningTimeOfLectures = this.repository.minimumLearningTimeOfLectures(lecturerId);
		maximumLearningTimeOfLectures = this.repository.maximumLearningTimeOfLectures(lecturerId);

		lecturerDashboard = new LecturerDashboard();
		lecturerDashboard.setTotalNumberOfTheoryLectures(totalNumberOfTheoryLectures);
		lecturerDashboard.setTotalNumberOfHandsOnLectures(totalNumberOfHandsOnLectures);
		lecturerDashboard.setAverageLearningTimeOfLectures(averageLearningTimeOfLectures);
		lecturerDashboard.setDeviationLearningTimeOfLectures(deviationLearningTimeOfLectures);
		lecturerDashboard.setMinimumLearningTimeOfLectures(minimumLearningTimeOfLectures);
		lecturerDashboard.setMaximumLearningTimeOfLectures(maximumLearningTimeOfLectures);

		super.getBuffer().setData(lecturerDashboard);
	}

	@Override
	public void unbind(final LecturerDashboard object) {
		Tuple tuple;

		tuple = super.unbind(object, "totalNumberOfTheoryLectures", "totalNumberOfHandsOnLectures", "averageLearningTimeOfLectures", // 
			"deviationLearningTimeOfLectures", "minimumLearningTimeOfLectures", "maximumLearningTimeOfLectures");

		super.getResponse().setData(tuple);
	}

}
