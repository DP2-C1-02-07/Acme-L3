
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialDeleteTest extends TestHarness {

	// Internal data ----------------------------------------------------------

	@Autowired
	protected AssistantTutorialTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int tutorialRecordIndex, final String code, final String nextCode) {
		// HINT:This test proves that a tutorial is deleted correctly

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, code);
		super.clickOnListingRecord(tutorialRecordIndex);

		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.clickOnMenu("Assistant", "My tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(tutorialRecordIndex, 0, nextCode);

		super.signOut();

	}

	@Test
	public void test200Negative() {
		// HINT: This test does not include a negative test because it does not 
		//receive data that we must verify
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a tutorial using inappropriate roles

		final Collection<Tutorial> tutorials;
		String id;

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials)
			if (t.isDraftMode()) {

				id = String.format("id=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/delete", id);
				super.checkPanicExists();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/delete", id);
				super.checkPanicExists();
				super.signOut();

			}
	}
	@Test
	public void test301Hacking() {
		// HINT: This test attempts to delete a tutorial that isn't in draft mode
		//HITN + regiestered by the principal
		Collection<Tutorial> tutorials;
		String id;

		super.signIn("assistant1", "assistant1");

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials)
			if (!t.isDraftMode()) {
				id = String.format("id=%d", t.getId());
				super.request("/assistant/tutorial/delete", id);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: This test attempts to delete a tutorial 
		//that does not belong to the authenticated principal

		Collection<Tutorial> tutorials;
		String id;

		super.signIn("assistant2", "assistant2");

		tutorials = this.repository.findManyTutorialsByAssistantUsername("assistant1");

		for (final Tutorial t : tutorials) {
			id = String.format("id=%d", t.getId());
			super.request("/assistant/tutorial/delete", id);
		}
		super.signOut();
	}

}
