package acme.testing.any.peep;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepShowTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "", encoding = "utf-8", numLinesToSkip = 1)
	public void testPositive(final int recordIndex, final String instantiationMoment, final String title, final String nick, final String message, final String email, final String link) {

		super.clickOnMenu("Anonymous", "All peeps");
		super.sortListing(1, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("instantiationMoment", instantiationMoment);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("email", email);
		super.checkInputBoxHasValue("link", link);

	}

}