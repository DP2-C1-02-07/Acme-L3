
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.enums.Marks;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditingRecords extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Length(max = 75)
	protected String			subject;

	@NotBlank
	@Length(max = 100)
	protected String			assessment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				subjectAuditedStartMoment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				subjectAuditedEndMoment;

	@NotNull
	protected Marks				mark;

	@URL
	protected String			link;


	@AssertTrue
	protected boolean isSubjectAuditedPeriodValid() {
		return this.subjectAuditedEndMoment.getTime() - this.subjectAuditedStartMoment.getTime() >= 3600000l;
	}

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Audit audit;
}
