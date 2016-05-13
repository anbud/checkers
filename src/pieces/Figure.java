package pieces;
// commit test
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import checkersBoard.Field;

public abstract class Figure {

	protected Icon icon;
	protected FigureColor color;
	protected List<Field> moves;
	
	public Figure(Icon icon, FigureColor color) {
		this.icon = icon;
		this.color = color;		
		this.moves = new ArrayList<>();
	}
	
	public abstract List<Field> getMoves(Field source, Field[][] fields);

	public final Icon getIcon() {
		return icon;
	}

	public final void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public final FigureColor getColor() {
		return color;
	}

	public final void setColor(FigureColor color) {
		this.color = color;
	}
}
