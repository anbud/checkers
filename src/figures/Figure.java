package figures;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import checkersBoard.Board;
import checkersBoard.Field;

public abstract class Figure {

	protected Icon icon;
	protected FigureColor color;
	
	protected Board board;
	protected boolean percussive;	
	protected List<Field> captured;
	protected List<Field> quietMoves;	
	protected List<Field> percussiveMoves;
	
	public Figure(Icon icon, FigureColor color, Board board) {
		this.icon = icon;
		this.color = color;				
		this.quietMoves = new LinkedList<>();
		this.percussiveMoves = new LinkedList<>();
		this.captured = new LinkedList<>();
		this.board = board;
		this.percussive = false;	
	}		
	
	public abstract List<Field> getMoves(Field source);

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
	
	public final boolean isPercurssiveMove() {
		return percussive;
	}
	
	public final List<Field> getCaptured() {
		return captured;
	}
}
