
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

	Integer						numberOfAuditsTheoryCourses;
	Integer						numberOfAuditsHandOnCourses;
	double						averageAuditingRecordsInAudits;
	double						deviationAuditingRecordsInAudits;
	double						minimumAuditingRecordsInAudits;
	double						maximumAuditingRecordsInAudits;
	double						averageTimePeriodLengthsAuditingRecords;
	double						deviationTimePeriodLengthsAuditingRecords;
	double						minimumTimePeriodLengthsAuditingRecords;
	double						maximumTimePeriodLengthsAuditingRecords;
}
