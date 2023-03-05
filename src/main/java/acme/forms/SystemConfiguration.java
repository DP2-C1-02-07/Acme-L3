
package acme.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemConfiguration extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------

	@NotBlank
	public String				systemCurrency;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{3}(,[A-Z]{3})*$")
	public String				acceptedCurrencies;

}
