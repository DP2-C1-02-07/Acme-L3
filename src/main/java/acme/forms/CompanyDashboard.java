
package acme.forms;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDashboard implements Serializable {

	// Serialisation identifier ---------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes

	Map<String, Integer>		totalNumberOfPracticaGroupedByMonth;

	Double						averagePeriodOfPracticum;
	Double						deviationPeriodOfPracticum;
	Double						minimumPeriodOfPracticum;
	Double						maximumPeriodOfPracticum;

	Double						averagePeriodOfSession;
	Double						deviationPeriodOfSession;
	Double						minimumPeriodOfSession;
	Double						maximumPeriodOfSession;

}
