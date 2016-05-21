package figures;

import java.util.List;

import javax.swing.Icon;

import checkersBoard.Board;
import checkersBoard.Field;
import javafx.scene.image.Image;

public class QueenFigure extends Figure {

	public QueenFigure(Image icon, FigureColor color, Board board) {
		super(icon, color, board);
	}

	@Override
	public List<Field> getMoves(Field source) {	
		captured.clear();
		quietMoves.clear();

		quietMoves(source.getXX() - 1, source.getYY() - 1, -1, -1);
		quietMoves(source.getXX() - 1, source.getYY() + 1, -1, 1);
		quietMoves(source.getXX() + 1, source.getYY() + 1, 1, 1);
		quietMoves(source.getXX() + 1, source.getYY() - 1, 1, -1);
		return quietMoves;

	}

	private void quietMoves(int sourceX, int sourceY, int stepX, int stepY) {
		if (board.isFigureNull(sourceX, sourceY))
			quietMoves.add(board.getField(sourceX, sourceY));
		else
			return;

		quietMoves(sourceX + stepX, sourceY + stepY, stepX, stepY);
	}
}
