
package acme.testing.company.session;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Session;
import acme.testing.TestHarness;

public class CompanySessionPublishTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest

	@CsvFileSource(resources = "/company/session/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int sessionRecordIndex, final String title, final String abstractThing, final String startDate, final String finishDate, final String practicum) {
		// HINT:This test proves that a session is published correctly and its data is saved correctly

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

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractThing", abstractThing);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("practicum", practicum);
		super.clickOnSubmit("Publish");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("View sessions");

		super.checkListingExists();
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, startDate);
		super.checkColumnHasValue(sessionRecordIndex, 2, finishDate);
		super.clickOnListingRecord(sessionRecordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractThing", abstractThing);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);

		super.signOut();
	}

	@ParameterizedTest

	@CsvFileSource(resources = "/company/session/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int sessionRecordIndex, final String title, final String abstractThing, final String startDate, final String finishDate, final String practicum) {
		// HINT: this test attempts to publish session with incorrect data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("View sessions");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(2);
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractThing", abstractThing);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("practicum", practicum);
		super.clickOnSubmit("Publish");
		super.checkErrorsExist();

		super.signOut();
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
				super.request("/company/session/publish", id);
				super.checkPanicExists();

				super.signIn("assistant1", "assistant1");
				super.request("/company/session/publish", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/company/session/publish", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/session/publish", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session/publish", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/session/publish", id);
				super.checkPanicExists();
				super.signOut();

			}
	}

}
