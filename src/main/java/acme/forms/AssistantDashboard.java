
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

	int							totalNumberOfTheorySessions;
	int							totalNumberOfHandsOnSessions;
	Double						averageSessionsTime;
	double						deviationSessionsTime;
	double						minimumSessionsTime;
	double						maximumSessionsTime;
	Double						averageTurorialsTime;
	double						deviationTutorialsTime;
	double						minimumTutorialsTime;
	double						maximumTutorialsTime;

}
