package figures;

import java.util.LinkedList;
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
		quietMoves.clear();
		captured.clear();
		percussiveMoves.clear();

		List<Field> tempPercussive = new LinkedList<Field>();
		List<Field> tempCaptured = new LinkedList<Field>();

		longestRoadAboveLeft(source.getXX() - 1, source.getYY() - 1, false, source, tempPercussive, tempCaptured);
		if (tempPercussive.size() > percussiveMoves.size()) {
			percussiveMoves.addAll(tempPercussive);
			captured.addAll(tempCaptured);
		}
		tempPercussive.clear();
		tempCaptured.clear();
		longestRoadAboveRight(source.getXX() - 1, source.getYY() + 1, false, source, tempPercussive, tempCaptured);
		if (tempPercussive.size() > percussiveMoves.size()) {
			captured.clear();
			percussiveMoves.clear();
			percussiveMoves.addAll(tempPercussive);
			captured.addAll(tempCaptured);
		}
		tempPercussive.clear();
		tempCaptured.clear();
		longestRoadBellowRight(source.getXX() + 1, source.getYY() + 1, false, source, tempPercussive, tempCaptured);
		if (tempPercussive.size() > percussiveMoves.size()) {
			captured.clear();
			percussiveMoves.clear();
			percussiveMoves.addAll(tempPercussive);
			captured.addAll(tempCaptured);
		}
		tempPercussive.clear();
		tempCaptured.clear();
		longestRoadBellowLeft(source.getXX() + 1, source.getYY() - 1, false, source, tempPercussive, tempCaptured);
		if (tempPercussive.size() > percussiveMoves.size()) {
			captured.clear();
			percussiveMoves.clear();
			percussiveMoves.addAll(tempPercussive);
			captured.addAll(tempCaptured);
		}
		tempPercussive.clear();
		tempCaptured.clear();

		if (percussiveMoves.isEmpty()) {
			percussive = false;
			quietMoves(source.getXX() - 1, source.getYY() - 1, -1, -1);
			quietMoves(source.getXX() - 1, source.getYY() + 1, -1, 1);
			quietMoves(source.getXX() + 1, source.getYY() + 1, 1, 1);
			quietMoves(source.getXX() + 1, source.getYY() - 1, 1, -1);
			return quietMoves;
		} else {
			percussive = true;
			return percussiveMoves;
		}
	}

	private void quietMoves(int sourceX, int sourceY, int stepX, int stepY) {
		if (board.isFigureNull(sourceX, sourceY))
			quietMoves.add(board.getField(sourceX, sourceY));
		else
			return;

		quietMoves(sourceX + stepX, sourceY + stepY, stepX, stepY);
	}

	private void longestRoadAboveLeft(int x, int y, boolean turn, Field source, List<Field> tempMoves,
			List<Field> tempCaptured) {
		if (board.isEnemy(source.getXX(), source.getYY(), x, y)
				&& (board.isFigureNull(x - 1, y - 1) || board.getField(x - 1, y - 1) == source)
				&& !board.getField(x, y).isVisited()) {
			tempCaptured.add(board.getField(x, y));
			tempMoves.add(board.getField(x - 1, y - 1));
			board.getField(x, y).setVisited(true);
			turn = true;
		} else if (!board.isFigureNull(x - 1, y - 1) && !board.isFigureNull(x - 2, y - 2) || !board.isFigureNull(x, y))
			return;

		if (turn && board.isFigureNull(x - 1, y - 1)) {
			List<Field> lMoves = new LinkedList<Field>();
			List<Field> lCaptured = new LinkedList<Field>();
			// lMoves.add(board.getField(x - 1, y - 1));
			lMoves.addAll(tempMoves);
			lCaptured.addAll(tempCaptured);
			longestRoadBellowLeft(x - 1, y - 1, false, source, lMoves, lCaptured);

			List<Field> rMoves = new LinkedList<Field>();
			List<Field> rCaptured = new LinkedList<Field>();
			// rMoves.add(board.getField(x - 1, y - 1));
			rMoves.addAll(tempMoves);
			rCaptured.addAll(tempCaptured);
			longestRoadAboveRight(x - 1, y - 1, false, source, rMoves, rCaptured);

			if (rCaptured.size() > lCaptured.size()) {
				tempMoves.clear();
				tempMoves.addAll(rMoves);
				tempCaptured.clear();
				tempCaptured.addAll(rCaptured);
			} else {
				tempMoves.clear();
				tempMoves.addAll(lMoves);
				tempCaptured.clear();
				tempCaptured.addAll(lCaptured);
			}
		}

		longestRoadAboveLeft(x - 1, y - 1, turn, source, tempMoves, tempCaptured);
		board.getField(x, y).setVisited(false);
	}

	private void longestRoadAboveRight(int x, int y, boolean turn, Field source, List<Field> tempMoves,
			List<Field> tempCaptured) {
		if (board.isEnemy(source.getXX(), source.getYY(), x, y)
				&& (board.isFigureNull(x - 1, y + 1) || board.getField(x - 1, y + 1) == source)
				&& !board.getField(x, y).isVisited()) {
			tempCaptured.add(board.getField(x, y));
			tempMoves.add(board.getField(x - 1, y + 1));
			board.getField(x, y).setVisited(true);
			turn = true;
		} else if (!board.isFigureNull(x - 1, y + 1) && !board.isFigureNull(x - 2, y + 2) || !board.isFigureNull(x, y))
			return;

		if (turn && board.isFigureNull(x - 1, y + 1)) {
			List<Field> lMoves = new LinkedList<Field>();
			List<Field> lCaptured = new LinkedList<Field>();
			// lMoves.add(board.getField(x - 1, y + 1));
			lMoves.addAll(tempMoves);
			lCaptured.addAll(tempCaptured);
			longestRoadAboveLeft(x - 1, y + 1, false, source, lMoves, lCaptured);

			List<Field> rMoves = new LinkedList<Field>();
			List<Field> rCaptured = new LinkedList<Field>();
			// rMoves.add(board.getField(x - 1, y + 1));
			rMoves.addAll(tempMoves);
			rCaptured.addAll(tempCaptured);
			longestRoadBellowRight(x - 1, y + 1, false, source, rMoves, rCaptured);

			if (rCaptured.size() > lCaptured.size()) {
				tempMoves.clear();
				tempMoves.addAll(rMoves);
				tempCaptured.clear();
				tempCaptured.addAll(rCaptured);
			} else {
				tempMoves.clear();
				tempMoves.addAll(lMoves);
				tempCaptured.clear();
				tempCaptured.addAll(lCaptured);
			}
		}

		longestRoadAboveRight(x - 1, y + 1, turn, source, tempMoves, tempCaptured);
		board.getField(x, y).setVisited(false);
	}

	private void longestRoadBellowLeft(int x, int y, boolean turn, Field source, List<Field> tempMoves,
			List<Field> tempCaptured) {
		if (board.isEnemy(source.getXX(), source.getYY(), x, y)
				&& (board.isFigureNull(x + 1, y - 1) || board.getField(x + 1, y - 1) == source)
				&& !board.getField(x, y).isVisited()) {
			tempCaptured.add(board.getField(x, y));
			tempMoves.add(board.getField(x + 1, y - 1));
			board.getField(x, y).setVisited(true);
			turn = true;
		} else if (!board.isFigureNull(x + 1, y - 1) && !board.isFigureNull(x + 2, y - 2) || !board.isFigureNull(x, y))
			return;

		if (turn && board.isFigureNull(x + 1, y - 1)) {
			List<Field> lMoves = new LinkedList<Field>();
			List<Field> lCaptured = new LinkedList<Field>();
			// lMoves.add(board.getField(x + 1, y - 1));
			lMoves.addAll(tempMoves);
			lCaptured.addAll(tempCaptured);
			longestRoadAboveLeft(x + 1, y - 1, false, source, lMoves, lCaptured);

			List<Field> rMoves = new LinkedList<Field>();
			List<Field> rCaptured = new LinkedList<Field>();
			// rMoves.add(board.getField(x + 1, y - 1));
			rMoves.addAll(tempMoves);
			rCaptured.addAll(tempCaptured);
			longestRoadBellowRight(x + 1, y - 1, false, source, rMoves, rCaptured);

			if (rCaptured.size() > lCaptured.size()) {
				tempMoves.clear();
				tempMoves.addAll(rMoves);
				tempCaptured.clear();
				tempCaptured.addAll(rCaptured);
			} else {
				tempMoves.clear();
				tempMoves.addAll(lMoves);
				tempCaptured.clear();
				tempCaptured.addAll(lCaptured);
			}
		}

		longestRoadBellowLeft(x + 1, y - 1, turn, source, tempMoves, tempCaptured);
		board.getField(x, y).setVisited(false);
	}

	private void longestRoadBellowRight(int x, int y, boolean turn, Field source, List<Field> tempMoves,
			List<Field> tempCaptured) {
		if (board.isEnemy(source.getXX(), source.getYY(), x, y)
				&& (board.isFigureNull(x + 1, y + 1) || board.getField(x + 1, y + 1) == source)
				&& !board.getField(x, y).isVisited()) {
			tempCaptured.add(board.getField(x, y));
			tempMoves.add(board.getField(x + 1, y + 1));
			board.getField(x, y).setVisited(true);
			turn = true;
		} else if (!board.isFigureNull(x + 1, y + 1) && !board.isFigureNull(x + 2, y + 2) || !board.isFigureNull(x, y))
			return;

		if (turn && board.isFigureNull(x + 1, y + 1)) {
			List<Field> lMoves = new LinkedList<Field>();
			List<Field> lCaptured = new LinkedList<Field>();
			// lMoves.add(board.getField(x + 1, y + 1));
			lMoves.addAll(tempMoves);
			lCaptured.addAll(tempCaptured);
			longestRoadBellowLeft(x + 1, y + 1, false, source, lMoves, lCaptured);

			List<Field> rMoves = new LinkedList<Field>();
			List<Field> rCaptured = new LinkedList<Field>();
			// rMoves.add(board.getField(x + 1, y + 1));
			rMoves.addAll(tempMoves);
			rCaptured.addAll(tempCaptured);
			longestRoadAboveRight(x + 1, y + 1, false, source, rMoves, rCaptured);

			if (rCaptured.size() > lCaptured.size()) {
				tempMoves.clear();
				tempMoves.addAll(rMoves);
				tempCaptured.clear();
				tempCaptured.addAll(rCaptured);
			} else {
				tempMoves.clear();
				tempMoves.addAll(lMoves);
				tempCaptured.clear();
				tempCaptured.addAll(lCaptured);
			}
		}

		longestRoadBellowRight(x + 1, y + 1, turn, source, tempMoves, tempCaptured);
		board.getField(x, y).setVisited(false);
	}
}
