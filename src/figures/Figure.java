package figures;

import java.util.LinkedList;
import java.util.List;

import checkersBoard.Board;
import checkersBoard.Field;
import javafx.scene.image.Image;

public abstract class Figure {

	protected Image icon;
	protected FigureColor color;
	
	protected Board board;
	protected boolean percussive;	
	protected List<Field> captured;
	protected List<Field> quietMoves;	
	protected List<Field> percussiveMoves;
	
	public Figure(Image icon, FigureColor color, Board board) {
		this.icon = icon;
		this.color = color;				
		this.quietMoves = new LinkedList<>();
		this.percussiveMoves = new LinkedList<>();
		this.captured = new LinkedList<>();
		this.board = board;
		this.percussive = false;	
	}		
	
	public abstract List<Field> getMoves(Field source);

	public final Image getIcon() {
		return icon;
	}

	public final void setIcon(Image icon) {
		this.icon = icon;
	}

	public final FigureColor getColor() {
		return color;
	}

	public final void setColor(FigureColor color) {
		this.color = color;
	}
	
	public final boolean isPercurssiveMove() {
		return percussive;
	}
	
	public final List<Field> getCaptured() {
		return captured;
	}
}
