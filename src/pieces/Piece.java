package pieces;

import java.util.List;

import javax.swing.Icon;

import checkersBoard.Board;
import checkersBoard.Field;

public class Piece extends Figure {

	public Piece(Icon icon, FigureColor color) {
		super(icon, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Field> getMoves(Field source, Field[][] fields) {
		// TODO Auto-generated method stub
		moves.clear();
		int x = source.getXX();
		int y = source.getYY();
		if (color.equals(FigureColor.WOODEN)) {
			if (x > Board.TOP) {
				if (y > Board.TOP) {
					Field aboveLeft = fields[x - 1][y - 1];
					if (aboveLeft.getFigure() == null)
						moves.add(aboveLeft);
				}

				if (y + 1 < Board.NUM_FIELDS) {
					Field aboveRight = fields[x - 1][y + 1];
					if (aboveRight.getFigure() == null)
						moves.add(aboveRight);
				}
			}
		} else {
			if (x + 1 < Board.NUM_FIELDS) {
				if (y > Board.TOP) {
					Field bellowLeft = fields[x + 1][y - 1];
					if (bellowLeft.getFigure() == null) {
						moves.add(bellowLeft);
					}
				}

				if (y + 1 < Board.NUM_FIELDS) {
					Field bellowRight = fields[x + 1][y + 1];
					if (bellowRight.getFigure() == null) {
						moves.add(bellowRight);
					}
				}
			}
		}

		return moves;
	}
}
