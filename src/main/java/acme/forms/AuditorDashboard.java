
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							numberOfAuditsTheoryCourses;
	int							numberOfAuditsHandOnCourses;
	double						averageAuditingRecordsInAudits;
	double						deviationAuditingRecordsInAudits;
	double						minimumAuditingRecordsInAudits;
	double						maximumAuditingRecordsInAudits;
	Double						averageTimePeriodLengthsAuditingRecords;
	Double						deviationTimePeriodLengthsAuditingRecords;
	Double						minimumTimePeriodLengthsAuditingRecords;
	Double						maximumTimePeriodLengthsAuditingRecords;
}
