
package acme.entities;

public enum Marks {

	A_PLUS("A+"), A("A"), B("B"), C("C"), F("F"), F_MINUS("F-");


	private final String mark;


	private Marks(final String mark) {
		this.mark = mark;
	}

	public String getMark() {
		return this.mark;
	}
}
