
package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.TutorialSession;
import acme.testing.TestHarness;

public class AssistantTutorialSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String nextTitle) {
		//HINT: This test proves that when deleting a session it is deleted correctly

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialRecordIndex);

		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(sessionRecordIndex);
		super.clickOnSubmit("Delete");

		super.sortListing(0, "asc");
		super.checkColumnHasValue(sessionRecordIndex, 0, nextTitle);

		super.checkNotPanicExists();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: This test does not include a negative test because it does not 
		//receive data that we must verify
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete a session using inappropriate roles

		final Collection<TutorialSession> sessions;
		String id;

		sessions = this.repository.findManyTutorialSessionsByAssistantUsername("assistant1");

		for (final TutorialSession s : sessions)
			if (s.getTutorial().isDraftMode()) {

				id = String.format("id=%d", s.getId());
				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/delete", id);
				super.checkPanicExists();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial-session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial-session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial-session/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/delete", id);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: This test try to delete a session for a published tutorial registeres
		// HINT+ by the principal.
		Collection<TutorialSession> sessions;
		String id;

		super.checkLinkExists("Sign in");
		super.signIn("assistant1", "assistant1");

		sessions = this.repository.findManyTutorialSessionsByAssistantUsername("assistant1");

		for (final TutorialSession s : sessions)
			if (!s.getTutorial().isDraftMode()) {
				id = String.format("id=%d", s.getId());
				super.request("/assistant/tutorial-session/delete", id);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		//HINT: This test attempts to delete tutorial sessions
		//HINT+ from an attendee through another attendee profile.

		final Collection<TutorialSession> sessions;
		String id;

		super.signIn("assistant2", "assistant2");
		sessions = this.repository.findManyTutorialSessionsByAssistantUsername("assistant1");

		for (final TutorialSession s : sessions) {
			id = String.format("id=%d", s.getId());
			super.request("/assistant/tutorial-session/delete", id);
			super.checkPanicExists();
		}
		super.signOut();

	}

}
