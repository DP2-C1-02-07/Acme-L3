
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Course;
import acme.testing.TestHarness;

public class LecturerLectureListTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseRecordIndex, final String code, final int lectureRecordIndex, final String title, final String anAbstract) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(courseRecordIndex, 0, code);
		super.clickOnListingRecord(courseRecordIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Lectures");

		super.checkListingExists();
		super.checkColumnHasValue(lectureRecordIndex, 0, title);
		super.checkColumnHasValue(lectureRecordIndex, 1, anAbstract);
		super.clickOnListingRecord(lectureRecordIndex);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// there's no negative test case for this listing
	}

	@Test
	public void test300Hacking() {
		Collection<Course> courses;
		String param;

		courses = this.repository.findManyCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (course.isDraftMode()) {
				param = String.format("masterId=%d", course.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/lecture/list", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/lecture/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer2", "lecturer2");
				super.request("/lecturer/lecture/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/lecturer/lecture/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/lecturer/lecture/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/lecture/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/lecturer/lecture/list", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}
