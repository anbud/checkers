package checkersBoard;


public enum FieldColor {
	WHITE("#f4f4f4"), BLACK("#272625");
	
	String color;
	
	FieldColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return this.color;
	}
}
