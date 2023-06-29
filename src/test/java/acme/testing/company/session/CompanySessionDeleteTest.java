
package acme.testing.company.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Session;
import acme.testing.TestHarness;

public class CompanySessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest

	@CsvFileSource(resources = "/company/session/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int sessionRecordIndex, final String title, final String nextTitle) {
		// HINT:This test proves that a session is deleted correctly and its data is deleted correctly

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(0);

		super.checkFormExists();
		super.clickOnButton("View sessions");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(0, 0, title);
		super.clickOnListingRecord(0);

		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkListingExists();

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("View sessions");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.checkColumnHasValue(0, 0, nextTitle);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete session using inappropriate roles
		final Collection<Session> sessions;
		String id;

		sessions = this.repository.findManySessionByCompanyUsername("company1");

		for (final Session t : sessions)
			if (t.isDraftMode()) {

				id = String.format("id=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/company/session/delete", id);
				super.checkPanicExists();

				super.signIn("assistant1", "assistant1");
				super.request("/company/session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/company/session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/session/delete", id);
				super.checkPanicExists();
				super.signOut();

			}
	}

}
