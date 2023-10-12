
package acme.testing.student.enrolment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class StudentEnrolmentDeleteTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String oldCode, final String newCode) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "My enrolments!");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, oldCode);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// Don't need negative test
	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/student/enrolment/delete");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/enrolment/delete");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/student/enrolment/delete");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/enrolment/delete");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/student/enrolment/delete");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/student/enrolment/delete");
		super.checkPanicExists();
		super.signOut();
	}
}