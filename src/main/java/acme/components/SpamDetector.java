
package acme.components;

public class SpamDetector {

	protected String spamTuple = "sex,viagra,cialis,onemillion,youvewon,youhavewon,nigeria";


	public SpamDetector() {

	}

	public String getSpamTuple() {
		return this.spamTuple;
	}

	public void setSpamTuple(final String spamTuple) {
		this.spamTuple = spamTuple;
	}

	public boolean scanString(final String stringScanned) {
		boolean result = false;
		final String[] spamArray = this.spamTuple.split(",");
		final String checkString = stringScanned.replaceAll("[^a-zA-Z0-9]", "");
		final String checkStringLower = checkString.toLowerCase();
		for (final String spamTerm : spamArray)
			if (checkStringLower.contains(spamTerm))
				result = true;
		return result;
	}
}
