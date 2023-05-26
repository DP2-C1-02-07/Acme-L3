
package acme.testing.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class LecturerLectureCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String anAbstract, final String learningTime, final String body, final String type, final String furtherInformation) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("anAbstract", anAbstract);
		super.fillInputBoxIn("learningTime", learningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, anAbstract);
		super.checkColumnHasValue(recordIndex, 2, learningTime);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("anAbstract", anAbstract);
		super.checkInputBoxHasValue("learningTime", learningTime);
		super.checkInputBoxHasValue("body", body);
		super.checkInputBoxHasValue("type", type);
		super.checkInputBoxHasValue("furtherInformation", furtherInformation);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String anAbstract, final String learningTime, final String body, final String type, final String furtherInformation) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.clickOnButton("Create");
		super.checkFormExists();

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("anAbstract", anAbstract);
		super.fillInputBoxIn("learningTime", learningTime);
		super.fillInputBoxIn("body", body);
		super.fillInputBoxIn("type", type);
		super.fillInputBoxIn("furtherInformation", furtherInformation);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();
	}

}
