
package acme.testing.company.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Practicum;
import acme.testing.TestHarness;

public class CompanySessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/session/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int sessionRecordIndex, final String title) {

		//HINT: This test proves that a company can consult their session

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(0);
		super.checkFormExists();

		super.clickOnButton("View sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(sessionRecordIndex, 0, title);

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list session using inappropriate roles
		final Collection<Practicum> practicum;
		String id;

		practicum = this.repository.findManyPracticumByCompanyUsername("company1");

		for (final Practicum t : practicum)
			if (t.isDraftMode()) {

				id = String.format("practicumId=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/company/session/list", id);
				super.checkPanicExists();

				super.signIn("assistant1", "assistant1");
				super.request("/company/session/list", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/company/session/list", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/session/list", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session/list", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/session/list", id);
				super.checkPanicExists();
				super.signOut();

			}

	}

}
