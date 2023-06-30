
package acme.testing.company.session;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class CompanySessionAddendumTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanySessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest

	@CsvFileSource(resources = "/company/session/addendum-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int sessionRecordIndex, final String title, final String abstractThing, final String startDate, final String finishDate, final String practicum, final String confirmation) {
		// HINT:This test proves that a session is created correctly and its data is saved correctly

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("View sessions");

		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnButton("Create an addendum session");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractThing", abstractThing);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("practicum", practicum);
		super.fillInputBoxIn("confirmation", confirmation);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.clickOnButton("View sessions");

		super.checkListingExists();
		super.checkNotListingEmpty();
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, startDate);
		super.checkColumnHasValue(sessionRecordIndex, 2, finishDate);

		super.clickOnListingRecord(0);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);
		super.checkNotSubmitExists("Update");
		super.checkNotSubmitExists("Delete");
		super.checkNotSubmitExists("Publish");

		super.clickOnMenu("Company", "List my practica");				//
		super.checkListingExists();										//

		super.sortListing(0, "desc");									//Setting up the next test
		super.clickOnListingRecord(1);									//
		super.checkFormExists();										//
		super.clickOnSubmit("Publish");									//

		super.signOut();
	}

	@ParameterizedTest

	@CsvFileSource(resources = "/company/session/addendum-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int sessionRecordIndex, final String title, final String abstractThing, final String startDate, final String finishDate, final String practicum, final String confirmation) {
		// HINT: this test attempts to create session with incorrect data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();

		super.sortListing(0, "desc");
		super.clickOnListingRecord(1);
		super.checkFormExists();
		super.clickOnButton("View sessions");

		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnButton("Create an addendum session");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractThing", abstractThing);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("practicum", practicum);
		super.fillInputBoxIn("confirmation", confirmation);
		super.clickOnSubmit("Create");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create an addendum session using inappropriate roles

		super.checkLinkExists("Sign in");
		super.request("/company/session/create-addendum");
		super.checkPanicExists();

		super.signIn("assistant1", "assistant1");
		super.request("/company/session/create-addendum");
		super.checkPanicExists();
		super.signOut();

		super.signIn("administrator", "administrator");
		super.request("/company/session/create-addendum");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/company/session/create-addendum");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/company/session/create-addendum");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/company/session/create-addendum");
		super.checkPanicExists();
		super.signOut();

	}

}
