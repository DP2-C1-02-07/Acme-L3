
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Audit extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}[0-9]{3}")
	protected String			code;

	@NotBlank
	@Length(max = 100)
	protected String			conclusion;

	@NotBlank
	@Length(max = 100)
	protected String			strongPoints;

	@NotBlank
	@Length(max = 100)
	protected String			weakPoints;

	// Derived attributes -----------------------------------------------------

	// Implementada más adelante

	//	@NotNull
	//	protected String			mark;

	//	@NotNull
	//	public String mark() {
	//		String marks = "N/A";
	//		if (!this.auditingRecords.isEmpty())
	//			for (int i = 0; i < this.auditingRecords.size(); i++)
	//				if (i == 0)
	//					marks = this.auditingRecords.get(i).getMark().toString();
	//				else
	//					marks = marks + ", " + this.auditingRecords.get(i).getMark().toString();
	//		return marks;
	//
	//	}

	// Relationships ----------------------------------------------------------

	//	@OneToMany(mappedBy = "audit")
	//	private List<AuditingRecords>	auditingRecords;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Auditor			auditor;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course			course;
}
