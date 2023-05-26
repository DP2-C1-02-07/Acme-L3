
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String course, final String code, final String title, final String goals, final String abstractTutorial) {

		//HINT: This test proves that an assistant can consult their tutorials and update correctly

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("goals", goals);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractTutorial", abstractTutorial);
		super.clickOnSubmit("Update");

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

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final String course, final String code, final String title, final String goals, final String abstractTutorial) {
		//HINT: this test attempts to publish a tutorial that cannot be published(without sessions).

		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(tutorialRecordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractTutorial", abstractTutorial);
		super.fillInputBoxIn("goals", goals);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a tutorial using inappropriate roles

		final Collection<Tutorial> tutorials;
		String id;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials)
			if (t.isDraftMode()) {

				id = String.format("id=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/update", id);
				super.checkPanicExists();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial/update", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/update", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial/update", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/update", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/update", id);
				super.checkPanicExists();
				super.signOut();

			}
	}

	@Test
	public void test301Hacking() {
		// HINT: This test attempts to update a tutorial registered by the principal.
		Collection<Tutorial> tutorials;
		String id;

		super.signIn("assistant1", "assistant1");

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials)
			if (!t.isDraftMode()) {
				id = String.format("id=%d", t.getId());
				super.request("/assistant/tutorial/update", id);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: This test attempts to update a tutorial 
		//that does not belong to the authenticated principal

		Collection<Tutorial> tutorials;
		String id;

		super.signIn("assistant2", "assistant2");

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials) {
			id = String.format("id=%d", t.getId());
			super.request("/assistant/tutorial/update", id);
			super.checkPanicExists();
		}
		super.signOut();
	}

}
