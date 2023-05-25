
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialSessionShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String code, final String title, final String abstractSession, final String sessionType, final String startDate, final String finishDate,
		final String info) {
		//HINT: This test tries to show the sessions details belonging to 
		//HINT+ the tutorial of a specific assistant

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, code);
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkInputBoxHasValue("code", code);

		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("startDate", startDate);
		super.checkInputBoxHasValue("finishDate", finishDate);
		super.checkInputBoxHasValue("info", info);
		super.checkInputBoxHasValue("sessionType", sessionType);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// HINT+ doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a session using inappropriate roles

		final Collection<Tutorial> tutorials;
		String id;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials) {

			id = String.format("id=%d", t.getId());
			super.checkLinkExists("Sign in");
			super.request("/assistant/tutorial-session/show", id);
			super.checkPanicExists();

			super.signIn("company1", "company1");
			super.request("/assistant/tutorial-session/show", id);
			super.checkPanicExists();
			super.signOut();

			super.signIn("administrator", "administrator");
			super.request("/assistant/tutorial-session/show", id);
			super.checkPanicExists();
			super.signOut();

			super.signIn("auditor1", "auditor1");
			super.request("/assistant/tutorial-session/show", id);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/assistant/tutorial-session/show", id);
			super.checkPanicExists();
			super.signOut();

			super.signIn("lecturer1", "lecturer1");
			super.request("/assistant/tutorial-session/show", id);
			super.checkPanicExists();
			super.signOut();

		}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to list sessions using inappropriate roles

		final Collection<Tutorial> tutorials;
		String id;
		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		super.checkLinkExists("Sign in");
		super.signIn("assistant2", "assistant2");

		for (final Tutorial t : tutorials) {
			id = String.format("id=%d", t.getId());
			super.request("/assistant/tutorial-session/show", id);
			super.checkPanicExists();
		}
		super.signOut();
	}

}
