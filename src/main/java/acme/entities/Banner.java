
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Past
	protected Date				instantationMoment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				displayStartMoment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				displayEndMoment;

	@NotNull
	@URL
	protected String			pictureLink;

	@NotBlank
	@Length(max = 75)
	protected String			slogan;

	@NotNull
	@URL
	protected String			documentLink;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
