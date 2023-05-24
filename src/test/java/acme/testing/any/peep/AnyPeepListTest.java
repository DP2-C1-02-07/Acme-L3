package acme.testing.any.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepListTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "", encoding = "utf-8", numLinesToSkip = 1)
	public void testPositive(final int recordIndex, final String instantiationMoment, final String title, final String nick) {

		super.clickOnMenu("Anonymous", "All peeps");
		super.checkListingExists();
		super.sortListing(1, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, instantiationMoment);
		super.checkColumnHasValue(recordIndex, 2, nick);

	}
}