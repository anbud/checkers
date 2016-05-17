package checkersBoard;

public class Move {

	public static final String QUIET = "-";
	public static final String PERCUSSIVE = ":";
	private static int order;
	private String type;
	private int from;
	private int to;	

	static {
		order = 0;
	}
	
	public Move(String type, int from, int to) {
		this.type = type;
		this.from = from;
		this.to = to;
		order++;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}
	
	@Override
	public String toString() {
		return String.format("%d. %d %s %d ", order, from, type, to);
	}
}
