
package acme.entities;

import java.util.Collection;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import acme.roles.Company;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "code"), @Index(columnList = "draftMode")
})

public class Practicum extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}[0-9][0-9]{3}")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			abstractThing;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	protected boolean			draftMode;


	// Derived attributes -----------------------------------------------------
	public Double estimatedTime(final Collection<Session> sessions) {
		double estimatedTime = 0.;
		if (sessions.size() > 0)
			for (final Session element : sessions) {
				final long durationInMilliseconds = element.getFinishDate().getTime() - element.getStartDate().getTime();
				final double durationInHours = durationInMilliseconds / (1000.0 * 60 * 60);
				estimatedTime = estimatedTime + durationInHours;
			}

		return estimatedTime;

	}


	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Company	company;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course	course;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.abstractThing, this.code, this.company, this.course, this.draftMode, this.goals, this.title);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Practicum other = (Practicum) obj;
		return Objects.equals(this.abstractThing, other.abstractThing) && Objects.equals(this.code, other.code) && Objects.equals(this.company, other.company) && Objects.equals(this.course, other.course) && this.draftMode == other.draftMode
			&& Objects.equals(this.goals, other.goals) && Objects.equals(this.title, other.title);
	}

}
