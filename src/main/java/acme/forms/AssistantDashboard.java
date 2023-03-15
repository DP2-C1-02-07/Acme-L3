
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistantDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						totalNumberOfTheorySessions;
	Integer						totalNumberOfHandsOnSessions;
	double						averageSessionsTime;
	double						deviationSessionsTime;
	double						minimumSessionsTime;
	double						maximumSessionsTime;
	double						averageTurorialsTime;
	double						deviationTutorialsTime;
	double						minimumTutorialsTime;
	double						maximumTutorialsTime;

}
