
package acme.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.enums.Marks;
import acme.framework.data.AbstractEntity;
import acme.roles.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Audit extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1-3}[0-9]{3}")
	protected String				code;

	@NotBlank
	@Length(max = 100)
	protected String				conclusion;

	@NotBlank
	@Length(max = 100)
	protected String				strongPoints;

	@NotBlank
	@Length(max = 100)
	protected String				weakPoints;

	@OneToMany(mappedBy = "audit")
	private List<AuditingRecords>	auditingRecords;

	// Derived attributes -----------------------------------------------------


	@NotNull
	@Transient
	public Marks mark() {
		final Map<Marks, Integer> marks = new HashMap<>();
		Marks result;

		for (final AuditingRecords auditingRecord : this.auditingRecords)
			if (marks.containsKey(auditingRecord.getMark())) {
				final Integer count = marks.get(auditingRecord.getMark()) + 1;
				marks.put(auditingRecord.getMark(), count);
			} else
				marks.put(auditingRecord.getMark(), 1);

		final int max = Collections.max(marks.values());
		final List<Marks> moda = new ArrayList<>();
		for (final Map.Entry<Marks, Integer> entry : marks.entrySet())
			if (entry.getValue() == max)
				moda.add(entry.getKey());

		if (moda.size() == 1)
			result = moda.get(0);
		else {
			final Random random = new Random();
			final int randomInt = random.nextInt(moda.size());
			result = moda.get(randomInt);
		}
		return result;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = true)
	protected Auditor auditor;
}
