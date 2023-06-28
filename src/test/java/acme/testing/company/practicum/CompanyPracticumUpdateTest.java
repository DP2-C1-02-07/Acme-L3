
package acme.testing.company.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class CompanyPracticumUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest

	@CsvFileSource(resources = "/company/practicum/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final String code, final String title, final String abstractThing, final String goals, final String course) {
		// HINT:This test proves that a tutorial is created correctly and its data is saved correctly

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractThing", abstractThing);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(practicumRecordIndex, 0, code);
		super.checkColumnHasValue(practicumRecordIndex, 1, title);
		super.clickOnListingRecord(practicumRecordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractThing", abstractThing);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("course", course);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumRecordIndex, final String code, final String title, final String abstractThing, final String goals, final String course) {
		// HINT: this test attempts to create tutorials with incorrect data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractThing", abstractThing);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("course", course);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show a tutorial using inappropriate roles

		super.checkLinkExists("Sign in");
		super.request("/company/practicum/create");
		super.checkPanicExists();

		super.signIn("assistant1", "assistant1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("administrator", "administrator");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

	}

}
