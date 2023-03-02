
package acme.entities;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@Past
	protected LocalDateTime		instationMoment;

	@NotNull
	protected LocalDateTime		displayStartMoment;

	@NotNull
	protected LocalDateTime		displayEndMoment;

	@NotNull
	@URL
	protected String			pictureLink;

	@NotBlank
	@Length(max = 75)
	protected String			slogan;

	@NotNull
	@URL
	protected String			targetWebDocumentLink;

	@NotNull
	protected Advertiser		advertised;


	@AssertTrue
	protected boolean isDisplayPeriodValid() {
		if (this.displayStartMoment == null || this.displayEndMoment == null)
			return false;
		final Duration duration = Duration.between(this.displayStartMoment, this.displayEndMoment);
		return duration.toDays() >= 7;
	}

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
