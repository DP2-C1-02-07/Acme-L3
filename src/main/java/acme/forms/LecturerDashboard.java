
package acme.forms;

import java.util.Collections;
import java.util.List;

import javax.persistence.Transient;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LecturerDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						totalNumberOfTheoryLectures;
	Integer						totalNumberOfHandsOnLectures;
	Double						averageLearningTimeOfLectures;
	Double						deviationLearningTimeOfLectures;
	Double						minimumLearningTimeOfLectures;
	Double						maximumLearningTimeOfLectures;
	Double						averageLearningTimeOfCourses;
	Double						deviationLearningTimeOfCourses;
	Double						minimumLearningTimeOfCourses;
	Double						maximumLearningTimeOfCourses;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Double averageLearningTimeOfCoursesCalc(final List<Double> ls) {
		if (ls != null && !ls.isEmpty()) {
			double result = 0.;

			for (final Double n : ls)
				result += n;

			return result / ls.size();
		} else
			return null;
	}

	@Transient
	public Double deviationLearningTimeOfCoursesCalc(final List<Double> ls) {
		if (ls != null && !ls.isEmpty()) {
			double media = 0.;

			for (final Double n : ls)
				media += n;
			media /= ls.size();

			double sumaDesviacionesCuadrado = 0.;
			for (final Double n : ls) {
				final double desviacion = n - media;
				sumaDesviacionesCuadrado += desviacion * desviacion;
			}

			return Math.sqrt(sumaDesviacionesCuadrado / ls.size());
		} else
			return null;

	}

	@Transient
	public Double minimumLearningTimeOfCoursesCalc(final List<Double> ls) {
		if (ls != null && !ls.isEmpty())
			return Collections.min(ls);
		else
			return null;
	}

	@Transient
	public Double maximumLearningTimeOfCoursesCalc(final List<Double> ls) {
		if (ls != null && !ls.isEmpty())
			return Collections.max(ls);
		else
			return null;
	}

}
