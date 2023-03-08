
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.components.datatypes.Money;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Offer extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Past
	protected Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	protected String			heading;

	@NotBlank
	@Length(max = 75)
	protected String			summary;

	protected Date				availabilityStart;

	protected Date				availabilityEnd;

	@NotNull
	protected Money				price;

	@URL
	protected String			link;


	@AssertTrue
	protected boolean isAvailabilityPeriodValid() {
		final boolean lastAWeek = this.availabilityEnd.getTime() - this.availabilityStart.getTime() >= 604800000l;
		final boolean dayAfterInstantiation = this.availabilityStart.getTime() - this.instantiationMoment.getTime() >= 86400000l;
		boolean result = true;

		if (!lastAWeek || !dayAfterInstantiation)
			result = false;

		return result;
	}

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
