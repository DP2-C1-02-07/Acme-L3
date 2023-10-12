
package acme.testing.company.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Session;
import acme.testing.TestHarness;

public class CompanySessionShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/session/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int sessionRecordIndex, final String title, final String startDate, final String finishDate, final String practicum) {

		//HINT: This test proves that a company can consult their practicum

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(0);
		super.checkFormExists();

		super.clickOnButton("View sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);
		super.checkInputBoxHasValue("practicum", practicum);

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
		final Collection<Session> sessions;
		String id;

		sessions = this.repository.findManySessionByCompanyUsername("company1");

		for (final Session t : sessions)
			if (t.isDraftMode()) {

				id = String.format("id=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/company/session/show", id);
				super.checkPanicExists();

				super.signIn("assistant1", "assistant1");
				super.request("/company/session/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/company/session/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/session/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/session/show", id);
				super.checkPanicExists();
				super.signOut();

			}
	}

}
