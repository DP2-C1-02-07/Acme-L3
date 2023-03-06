
package acme.forms;

import java.util.Map;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Map<String, Integer>		totalNumberOfPrincipalsByRole;
	Double						ratioOfPeepsBothEmailLink;
	Double						ratioOfCriticalNonCriticalBulletins;
	Map<String, Double>			averageBudgetOfferByCurrency;
	Map<String, Double>			minimumBudgetOfferByCurrency;
	Map<String, Double>			maximumBudgetOfferByCurrency;
	Map<String, Double>			standardDeviationBudgetOfferByCurrency;
	Double						averageNumberOfNotesPostedLast10Weeks;
	Double						minimumNumberOfNotesPostedLast10Weeks;
	Double						maximumNumberOfNotesPostedLast10Weeks;
	Double						standardDeviationNumberOfNotesPostedLast10Weeks;

}
