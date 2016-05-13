package pieces;

public enum FigureColor {
	RED("r"), WOODEN("w");
	
	String color;
	
	FigureColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
}
