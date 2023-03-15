
package acme.forms;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDashboard implements Serializable {

	// Serialisation identifier ---------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes

	//Esta montado así porque tiene que guardar el número máximo de horas de teoría y
	//de prácticas. La alternativa es crear 2 Double distintas. Como veais.
	Map<String, Double>		totalNumberOfTheoryAndHandsOnActivities;

	Double						averagePeriodOfActivities;
	Double						deviationPeriodOfActivities;
	Double						minimumPeriodOfActivities;
	Double						maximumPeriodOfActivities;

	Double						averageLearningTimeOfLessons;
	Double						deviationLearningTimeOfLessons;
	Double						minimumLearningTimeOfLessons;
	Double						maximumLearningTimeOfLessons;

}
