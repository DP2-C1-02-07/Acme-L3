
package acme.testing.student.enrolment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class StudentEnrolmentShowTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String motivation, final String goals, final String workTime) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "My enrolments!");
		super.checkListingExists();
		super.clickOnListingRecord(recordIndex);

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("workTime", workTime);


		super.signOut();
	}

	@Test
	public void test200Negative() {
		//Don't need negative tests
	}

	@Test
	public void test300Hacking() {

		super.checkLinkExists("Sign in");
		super.request("/student/enrolment/show");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/enrolment/show");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/student/enrolment/show");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/enrolment/show");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/student/enrolment/show");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/student/enrolment/show");
		super.checkPanicExists();
		super.signOut();
	}
}