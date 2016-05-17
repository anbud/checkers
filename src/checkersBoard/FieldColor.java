package checkersBoard;

import java.awt.Color;

public enum FieldColor {
	WHITE("#f4f4f4"), BLACK("#272625");
	
	Color color;
	
	FieldColor(String color) {
		this.color = Color.decode(color);
	}
	
	public Color getColor() {
		return this.color;
	}
}
