package figures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import checkersBoard.Board;
import checkersBoard.Field;
import javafx.scene.image.Image;

public class QueenFigure extends Figure {

	private class Path {
		List<Field> fields;
		List<Field> captures;
	}

	private List<Path> paths;

	public QueenFigure(Image icon, FigureColor color, Board board) {
		super(icon, color, board);
	}

	@Override
	public List<Field> getMoves(Field source) {
		paths = new ArrayList<>();

		quietMoves.clear();
		captured.clear();
		percussiveMoves.clear();

		List<Field> maxPercussive = new LinkedList<Field>();
		List<Field> maxCaptured = new LinkedList<Field>();

		reqStep(source.getXX(), source.getYY(), source.getFigure().getColor(), source, new LinkedList<>(),
				new LinkedList<>());

		maxPercussive = paths.get(0).fields;
		maxCaptured = paths.get(0).captures;

		for (Path p : paths) {
			if (maxCaptured.size() < p.captures.size()) {
				maxPercussive = p.fields;
				maxCaptured = p.captures;
			}
		}

		percussiveMoves.addAll(maxPercussive);
		captured.addAll(maxCaptured);		

		if (percussiveMoves.isEmpty()) {
			percussive = false;
			quietMoves(source.getXX() - 1, source.getYY() - 1, -1, -1);
			quietMoves(source.getXX() - 1, source.getYY() + 1, -1, 1);
			quietMoves(source.getXX() + 1, source.getYY() + 1, 1, 1);
			quietMoves(source.getXX() + 1, source.getYY() - 1, 1, -1);
			return quietMoves;
		} else {			
			addFreeFields(captured.get(captured.size() - 1), percussiveMoves.get(percussiveMoves.size() - 1));
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

	public void reqStep(int x, int y, FigureColor color, Field source, List<Field> captured, List<Field> steps) {
		if (!canEat(x, y, source, color, captured, steps)) {
			Path p = new Path();
			p.captures = new LinkedList<>();
			p.captures.addAll(captured);
			p.fields = new LinkedList<>();
			p.fields.addAll(steps);

			paths.add(p);

			return;
		}

		if (canEat(x, y, 1, 1, source, color, captured, steps)) {
			reqPaths(x, y, 1, 1, source, color, captured, steps);
		}

		if (canEat(x, y, -1, -1, source, color, captured, steps)) {
			reqPaths(x, y, -1, -1, source, color, captured, steps);
		}

		if (canEat(x, y, -1, 1, source, color, captured, steps)) {
			reqPaths(x, y, -1, 1, source, color, captured, steps);
		}

		if (canEat(x, y, 1, -1, source, color, captured, steps)) {
			reqPaths(x, y, 1, -1, source, color, captured, steps);
		}
	}

	public void reqPaths(int x, int y, int xdir, int ydir, Field source, FigureColor color, List<Field> captured,
			List<Field> steps) {
		boolean eat = false;
		Field cap = null;

		int n = 1;
		while (((x + n * xdir) >= 0) && ((x + n * xdir) <= 9) && ((y + n * ydir) >= 0) && ((y + n * ydir) <= 9)) {

			if ((board.getField(x + n * xdir, y + n * ydir).getFigure() == null)
					|| board.getField(x + n * xdir, y + n * ydir) == source) {

				if (eat) {
					List<Field> newCaptured = new LinkedList<>();
					newCaptured.addAll(captured);
					newCaptured.add(cap);
					List<Field> newSteps = new LinkedList<>();
					newSteps.addAll(steps);
					newSteps.add(board.getField(x + n * xdir, y + n * ydir));

					reqStep(x + n * xdir, y + n * ydir, color, source, newCaptured, newSteps);
				}

				n++;
				continue;
			} else {
				if (eat) {
					break;
				}
			}

			if (board.getField(x + n * xdir, y + n * ydir).getFigure().getColor() != color) {
				eat = true;
				cap = board.getField(x + n * xdir, y + n * ydir);
				n++;
			}
		}
	}

	public boolean canEat(int x, int y, Field source, FigureColor color, List<Field> captured, List<Field> steps) {
		return canEat(x, y, 1, 1, source, color, captured, steps)
				|| canEat(x, y, -1, -1, source, color, captured, steps)
				|| canEat(x, y, -1, 1, source, color, captured, steps)
				|| canEat(x, y, 1, -1, source, color, captured, steps);
	}

	public boolean canEat(int x, int y, int xdir, int ydir, Field source, FigureColor color, List<Field> captured,
			List<Field> steps) {

		boolean shouldBeEmpty = false;

		int n = 1;
		while (((x + n * xdir) >= 0) && ((x + n * xdir) <= 9) && ((y + n * ydir) >= 0) && ((y + n * ydir) <= 9)) {

			if (shouldBeEmpty) {
				if ((board.getField(x + n * xdir, y + n * ydir).getFigure() == null)
						|| board.getField(x + n * xdir, y + n * ydir) == source) {
					return true;
				} else {
					return false;
				}
			}

			if ((board.getField(x + n * xdir, y + n * ydir).getFigure() == null)
					|| board.getField(x + n * xdir, y + n * ydir) == source) {
				n++;
				continue;
			}

			if (board.getField(x + n * xdir, y + n * ydir).getFigure().getColor() == color) {
				return false;
			}

			if (!(board.getField(x + n * xdir, y + n * ydir).getFigure().getColor() == color)) {
				for (Field p : captured) {
					if (((int) p.getXX() == x + n * xdir) && ((int) p.getYY() == y + n * ydir)) {
						return false;
					}
				}
				shouldBeEmpty = true;
				n++;
			}

		}

		return false;
	}
	
	private void addFreeFields(Field lastCaptured, Field lastFree) {
		lastCaptured = captured.get(captured.size() - 1);
		lastFree = percussiveMoves.get(percussiveMoves.size() - 1);
		int xDir = lastFree.getXX() - lastCaptured.getXX();
		int yDir = lastFree.getYY() - lastCaptured.getYY();			
		freeFields(lastFree.getXX() + xDir, lastFree.getYY() + yDir, xDir, yDir);
	}
	
	private void freeFields(int x, int y, int xDir, int yDir) {
		if ((board.getField(x, y).getFigure() == null) && board.isIn(x, y)) {
			percussiveMoves.add(board.getField(x, y));
			freeFields(x + xDir, y + yDir, xDir, yDir);
		}
	}
}
