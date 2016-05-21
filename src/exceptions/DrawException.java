package exceptions;

public class DrawException extends Exception {

	private static final long serialVersionUID = -3407215116065705955L;

	public DrawException() {
		System.out.println("Partija je neresena");
	}
}