package figures;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;

import checkersBoard.Board;
import checkersBoard.Field;
import javafx.scene.image.Image;

public class SimpleFigure extends Figure {

	public SimpleFigure(Image icon, FigureColor color, Board board) {
		super(icon, color, board);
	}

	@Override
	public List<Field> getMoves(Field source) {
		quietMoves.clear();
		percussiveMoves.clear();
		captured.clear();
		List<Field> tempCaptured = new LinkedList<>();
		List<Field> tempPercussive = new LinkedList<>();

		longestRoadAboveLeft(source.getXX() - 2, source.getYY() - 2, source, tempPercussive, tempCaptured);
		longestRoadAboveRight(source.getXX() - 2, source.getYY() + 2, source, tempPercussive, tempCaptured);
		longestRoadBellowLeft(source.getXX() + 2, source.getYY() - 2, source, tempPercussive, tempCaptured);
		longestRoadBellowRight(source.getXX() + 2, source.getYY() + 2, source, tempPercussive, tempCaptured);

		if (percussiveMoves.isEmpty()) {
			percussive = false;
			quietMoves(source, color);			
			return quietMoves;
		} else {
			percussive = true;
			return percussiveMoves;
		}
	}

	private void quietMoves(Field source, FigureColor color) {
		if (color.equals(FigureColor.WOODEN)) {
			if (board.isFigureNull(source.getXX() - 1, source.getYY() - 1))
				quietMoves.add(board.getField(source.getXX() - 1, source.getYY() - 1));

			if (board.isFigureNull(source.getXX() - 1, source.getYY() + 1))
				quietMoves.add(board.getField(source.getXX() - 1, source.getYY() + 1));

		} else {
			if (board.isFigureNull(source.getXX() + 1, source.getYY() + 1))
				quietMoves.add(board.getField(source.getXX() + 1, source.getYY() + 1));

			if (board.isFigureNull(source.getXX() + 1, source.getYY() - 1))
				quietMoves.add(board.getField(source.getXX() + 1, source.getYY() - 1));
		}
	}

	private void longestRoadAboveLeft(int x, int y, Field source, List<Field> tempMoves, List<Field> tempCaptured) {
		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y + 1) && board.isFigureNull(x, y)
			&& !board.getField(x + 1, y + 1).isVisited()) {
			tempMoves.add(board.getField(x, y));
			tempCaptured.add(board.getField(x + 1, y + 1));
			board.getField(x + 1, y + 1).setVisited(true);			
		} else
			return;

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y - 1) && board.isFigureNull(x - 2, y - 2)) {
			longestRoadAboveLeft(x - 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y + 1) && board.isFigureNull(x - 2, y + 2)) {
			longestRoadAboveRight(x - 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y - 1) && board.isFigureNull(x + 2, y - 2)) {
			longestRoadBellowLeft(x + 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y + 1) && board.isFigureNull(x + 2, y + 2)) {
			longestRoadBellowRight(x + 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (tempMoves.size() > percussiveMoves.size()) {
			percussiveMoves.clear();
			percussiveMoves.addAll(tempMoves);
			captured.clear();
			captured.addAll(tempCaptured);
		}
		board.getField(x + 1, y + 1).setVisited(false);
		tempMoves.remove(tempMoves.size() - 1);
		tempCaptured.remove(tempCaptured.size() - 1);		
	}

	private void longestRoadAboveRight(int x, int y, Field source, List<Field> tempMoves, List<Field> tempCaptured) {		
		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y - 1) && board.isFigureNull(x, y)
				&& !board.getField(x + 1, y - 1).isVisited()) {
			tempMoves.add(board.getField(x, y));
			tempCaptured.add(board.getField(x + 1, y - 1));
			board.getField(x + 1, y - 1).setVisited(true);
		} else
			return;

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y - 1) && board.isFigureNull(x - 2, y - 2)) {
			longestRoadAboveLeft(x - 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y + 1) && board.isFigureNull(x - 2, y + 2)) {
			longestRoadAboveRight(x - 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y - 1) && board.isFigureNull(x + 2, y - 2)) {
			longestRoadBellowLeft(x + 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y + 1) && board.isFigureNull(x + 2, y + 2)) {
			longestRoadBellowRight(x + 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (tempMoves.size() > percussiveMoves.size()) {
			percussiveMoves.clear();
			percussiveMoves.addAll(tempMoves);
			captured.clear();
			captured.addAll(tempCaptured);
		}
		board.getField(x + 1, y - 1).setVisited(false);
		tempMoves.remove(tempMoves.size() - 1);
		tempCaptured.remove(tempCaptured.size() - 1);
	}

	private void longestRoadBellowLeft(int x, int y, Field source, List<Field> tempMoves, List<Field> tempCaptured) {	
		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y + 1) && board.isFigureNull(x, y)
				&& !board.getField(x - 1, y + 1).isVisited()) {
			tempMoves.add(board.getField(x, y));
			tempCaptured.add(board.getField(x - 1, y + 1));
			board.getField(x - 1, y + 1).setVisited(true);
		} else {
			return;
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y - 1) && board.isFigureNull(x - 2, y - 2)) {
			longestRoadAboveLeft(x - 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y + 1) && board.isFigureNull(x - 2, y + 2)) {
			longestRoadAboveRight(x - 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y - 1) && board.isFigureNull(x + 2, y - 2)) {
			longestRoadBellowLeft(x + 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y + 1) && board.isFigureNull(x + 2, y + 2)) {
			longestRoadBellowRight(x + 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (tempMoves.size() > percussiveMoves.size()) {
			percussiveMoves.clear();
			percussiveMoves.addAll(tempMoves);
			captured.clear();
			captured.addAll(tempCaptured);
		}
		board.getField(x - 1, y + 1).setVisited(false);
		tempMoves.remove(tempMoves.size() - 1);
		tempCaptured.remove(tempCaptured.size() - 1);
	}

	private void longestRoadBellowRight(int x, int y, Field source, List<Field> tempMoves, List<Field> tempCaptured) {		
		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y - 1) && board.isFigureNull(x, y)
				&& !board.getField(x - 1, y - 1).isVisited()) {
			tempMoves.add(board.getField(x, y));
			tempCaptured.add(board.getField(x - 1, y - 1));
			board.getField(x - 1, y - 1).setVisited(true);
		} else {
			return;
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y - 1) && board.isFigureNull(x - 2, y - 2)) {
			longestRoadAboveLeft(x - 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x - 1, y + 1) && board.isFigureNull(x - 2, y + 2)) {
			longestRoadAboveRight(x - 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y - 1) && board.isFigureNull(x + 2, y - 2)) {
			longestRoadBellowLeft(x + 2, y - 2, source, tempMoves, tempCaptured);
		}

		if (board.isEnemy(source.getXX(), source.getYY(), x + 1, y + 1) && board.isFigureNull(x + 2, y + 2)) {
			longestRoadBellowRight(x + 2, y + 2, source, tempMoves, tempCaptured);
		}

		if (tempMoves.size() > percussiveMoves.size()) {
			percussiveMoves.clear();
			percussiveMoves.addAll(tempMoves);
			captured.clear();
			captured.addAll(tempCaptured);
		}
		board.getField(x - 1, y - 1).setVisited(false);
		tempMoves.remove(tempMoves.size() - 1);
		tempCaptured.remove(tempCaptured.size() - 1);
	}
}