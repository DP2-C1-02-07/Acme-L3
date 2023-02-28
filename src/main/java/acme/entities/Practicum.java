
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Practicum extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{3}[0-9]{4}")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotNull
	@Length(max = 100)
	protected String			abstractThing;

	@NotNull
	@Length(max = 100)
	protected String			goals;

	@NotNull
	protected Double			estimatedTime;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
