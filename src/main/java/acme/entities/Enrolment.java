
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.framework.data.AbstractEntity;
import acme.roles.Student;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrolment extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}[0-9][0-9]{3}")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			motivation;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	//Debido al momento del desarrollo en el que nos encontramos, estimatedDate debería calcularse a raíz deç
	//las horas acumuladas en Course, pero debido a como esán diseñadas las entidades ahora mismo, decidimos
	//esperar y setear estimatedDate en un futuro servicio, en lugar de en una propiedad derivada.
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				estimatedDate;


	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	
	@NotNull
	@Valid
	@ManyToOne(optional=false)
	protected Student			student;
	
	
	@NotNull
	@Valid
	@OneToOne(optional=false)
	protected Course			course	;
	


}
