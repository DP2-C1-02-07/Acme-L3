
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String sessionType, final String startDate, final String finishDate, final String abstractSession, final String info) {
		// HINT:This test proves that a session is created correctly and its data is saved correctly

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.clickOnButton("Create");
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("info", info);
		super.clickOnSubmit("Create");

		super.checkListingExists();
		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("sessionType", sessionType);
		super.checkInputBoxHasValue("info", info);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final String title, final String sessionType, final String startDate, final String finishDate, final String abstractSession, final String info) {
		// HINT: This test tests erroneous data when creating sessions to verify 
		// HINT+ that the validations work

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.clickOnButton("Create");
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("finishDate", finishDate);
		super.fillInputBoxIn("info", info);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a session using inappropriate roles

		final Collection<Tutorial> tutorials;
		String id;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials)
			if (t.isDraftMode()) {

				id = String.format("id=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/create", id);
				super.checkPanicExists();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial-session/create", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/create", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial-session/create", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial-session/create", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/create", id);
				super.checkPanicExists();
				super.signOut();

			}
	}
	@Test
	public void test301Hacking() {
		// HINT: This test attempts to create a session for a published tutorial created
		// HINT+ by the principal.
		Collection<Tutorial> tutorials;
		String id;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials)
			if (!t.isDraftMode()) {
				id = String.format("id=%d", t.getId());
				super.request("/assistant/tutorial-session/create", id);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: This test try to create sessions for tutorials
		//HINT+ that does not belong to the principal

		Collection<Tutorial> tutorials;
		String id;

		super.checkLinkExists("Sign in");
		super.signIn("assistant2", "assistant2");

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials) {
			id = String.format("id=%d", t.getId());
			super.request("/assistant/tutorial-session/create", id);
			super.checkPanicExists();
		}
		super.signOut();
	}

}
