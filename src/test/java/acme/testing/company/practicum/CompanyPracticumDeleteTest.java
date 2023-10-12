
package acme.testing.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest

	@CsvFileSource(resources = "/company/practicum/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final String code, final String nextCode) {
		// HINT:This test proves that a practicum is deleted correctly and its data is deleted correctly

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(0, 0, code);
		super.clickOnListingRecord(0);

		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.clickOnMenu("Company", "List my practica");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(0, 0, nextCode);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		final Collection<Practicum> practicum;
		String id;

		practicum = this.repository.findManyPracticumByCompanyUsername("company1");

		for (final Practicum t : practicum)
			if (t.isDraftMode()) {

				id = String.format("id=%d", t.getId());
				super.checkLinkExists("Sign in");
				super.request("/company/practicum/delete", id);
				super.checkPanicExists();

				super.signIn("assistant1", "assistant1");
				super.request("/company/practicum/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/practicum/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum/delete", id);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/practicum/delete", id);
				super.checkPanicExists();
				super.signOut();

			}
	}

}
