
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String course, final String code, final String title, final String goals, final String abstractTutorial) {

		//HINT: This test proves that an assistant can consult their tutorials and publish one

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractTutorial", abstractTutorial);

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show a tutorial using inappropriate roles

		final Collection<Tutorial> tutorials;
		String id;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials)
			if (t.isDraftMode()) {

				id = String.format("id=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/show", id);
				super.checkPanicExists();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/show", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/show", id);
				super.checkPanicExists();
				super.signOut();

			}
	}
}
