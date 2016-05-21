package exceptions;

public class LostException extends Exception {

	private static final long serialVersionUID = 2770149698123263314L;

	public LostException(String message) {
		System.out.println(message);
	}
}