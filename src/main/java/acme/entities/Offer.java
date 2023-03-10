
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
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

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	protected String			heading;

	@NotBlank
	@Length(max = 100)
	protected String			summary;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				availabilityStart;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				availabilityEnd;

	@Valid
	protected Money				price;

	@URL
	protected String			link;

	//	@AssertTrue
	//	protected boolean isAvailabilityPeriodValid() {
	//		final boolean lastAWeek = this.availabilityEnd.getTime() - this.availabilityStart.getTime() >= 604800000l;
	//		final boolean dayAfterInstantiation = this.availabilityStart.getTime() - this.instantiationMoment.getTime() >= 86400000l;
	//		boolean result = true;
	//
	//		if (!lastAWeek || !dayAfterInstantiation)
	//			result = false;
	//
	//		return result;
	//	}
	//
	//	@AssertTrue
	//	protected boolean isMoneyPositive() {
	//		return this.price.getAmount() >= 0.0;
	//	}

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
