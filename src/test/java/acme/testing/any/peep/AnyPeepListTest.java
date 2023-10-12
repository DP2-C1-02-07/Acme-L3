
package acme.testing.any.peep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String instantiationMoment, final String nick) {

		super.clickOnMenu("Anonymous", "All peeps");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, instantiationMoment);
		super.checkColumnHasValue(recordIndex, 2, nick);

	}

	@Test
	public void test200Negative() {
		// No necessary
	}

	@Test
	public void test300Hacking() {
		// No necessary
	}
}
