package checkersBoard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import figures.Figure;
import figures.FigureColor;
import figures.QueenFigure;
import figures.SimpleFigure;
import gui.Action;
import gui.Gui;
import gui.GuiFigure;
import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

public class Board extends TilePane {

	private final int NUM_FIELDS = 10;
	private final int TOP = 0;
	private Field[][] board = new Field[10][10];
	private final Image redFigure = new Image(Gui.class.getResource(GuiFigure.BLACK.file).toExternalForm());
	private final Image woodenFigure = new Image(Gui.class.getResource(GuiFigure.WHITE.file).toExternalForm());
	private Field myPosition;
	private List<Field> possibleMoves;
	private List<Field> captured;
	private List<Field> whoCanDoPercussiveMove;
	private boolean percussive;
	private boolean check;
	private FigureColor onMove;
	private FigureColor me;
	private int queenNumMoves;
	private List<Move> moves;

	private Consumer<Field> moveHandler;
	private Action turnHandler;
	private Action drawHandler;
	private Action winHandler;

	public Board() {
		initialize();
		setFigures();

		setStyle("-fx-border-width: 5px; -fx-border-color: #106CC8");
	}

	public void onMove(Consumer<Field> action) {
		moveHandler = action;
	}

	public void onWin(Action action) {
		winHandler = action;
	}

	public void onDraw(Action action) {
		drawHandler = action;
	}

	public void onTurn(Action action) {
		turnHandler = action;
	}

	public void whoAmI(boolean a) {
		if (a)
			me = FigureColor.WOODEN;
		else
			me = FigureColor.RED;
	}

	private void initialize() {
		queenNumMoves = 0;
		myPosition = null;
		percussive = false;
		check = false;
		onMove = FigureColor.WOODEN;
		possibleMoves = new LinkedList<Field>();
		captured = new LinkedList<Field>();
		whoCanDoPercussiveMove = new LinkedList<Field>();
		moves = new ArrayList<Move>();
	}

	private void setFigures() {
		for (int i = 0; i < NUM_FIELDS; i++) {
			for (int j = 0; j < NUM_FIELDS; j++) {
				Figure figure = null;
				if ((i + j) % 2 != 0 && (i < 4)) {
					figure = new SimpleFigure(redFigure, FigureColor.RED, this);
				}
				if ((i + j) % 2 != 0 && (i > 5)) {
					figure = new SimpleFigure(woodenFigure, FigureColor.WOODEN, this);
				}

				double size = getWidth() / 10;

				if ((i + j) % 2 != 0) {
					board[i][j] = new Field(FieldColor.BLACK, figure, i, j, size * 0.8);
					board[i][j].setOnMouseClicked((e) -> {
						move((Field) e.getSource());
						if (moveHandler != null)
							moveHandler.accept((Field) e.getSource());
					});
				} else {
					board[i][j] = new Field(FieldColor.WHITE, null, i, j, size * 0.8);
				}

				board[i][j].setMinSize(0, 0);
				board[i][j].setPrefHeight(size);
				board[i][j].setPrefWidth(size);

				board[i][j].setAlignment(Pos.CENTER);

				getChildren().add(board[i][j]);
			}
		}
	}

	private void doPromotion() {
		Image icon;
		if (myPosition.getFigure().getColor().equals(FigureColor.WOODEN))
			icon = new Image(Gui.class.getResource(GuiFigure.WHITE_QUEEN.file).toExternalForm());
		else
			icon = new Image(Gui.class.getResource(GuiFigure.BLACK_QUEEN.file).toExternalForm());

		Figure promoted = new QueenFigure(icon, myPosition.getFigure().getColor(), this);

		myPosition.setFigure(null);
		myPosition.setIcon(null);
		myPosition.setFigure(promoted);
		myPosition.setIcon(promoted.getIcon());
	}

	private void nextOnMove() {
		if (onMove.equals(FigureColor.WOODEN))
			onMove = FigureColor.RED;
		else
			onMove = FigureColor.WOODEN;
	}

	private boolean isOnMove(Field source) {
		if (source == null) {
			return false;
		}

		if (source.getFigure() == null)
			return false;

		return onMove == source.getFigure().getColor();
	}

	private void highlight(boolean highlighted) {
		for (Field f : possibleMoves)
			f.highlight(!highlighted);
	}

	public void captureOne(int x, int y) {
		board[x][y].setFigure(null);
		board[x][y].setIcon(null);
	}

	private void capture() {
		for (Field f : myPosition.getFigure().getCaptured()) {
			f.setFigure(null);
			f.setIcon(null);
		}
	}

	public void setMyPosition(int x, int y) {
		myPosition = board[x][y];
	}

	public void changePosition(Field dest) {
		/*
		 * Figure temp = myPosition.getFigure(); Image tempIcon =
		 * myPosition.getIcon(); myPosition.setFigure(null);
		 * myPosition.setIcon(null); dest.setFigure(temp);
		 * dest.setIcon(tempIcon); myPosition = dest;
		 */

		Figure temp = myPosition.getFigure();
		Image tempIcon = myPosition.getIcon();

		Bounds srcb = myPosition.getBoundsInParent();
		Bounds destb = dest.getBoundsInParent();
		Bounds imgb = myPosition.getImage().getBoundsInParent();

		ImageView animImg = new ImageView(myPosition.getIcon());
		animImg.setFitHeight(myPosition.getImage().getFitHeight());
		animImg.setFitWidth(myPosition.getImage().getFitWidth());

		myPosition.setFigure(null);
		myPosition.setIcon(null);

		myPosition = dest;

		dest.setFigure(temp);
		dest.getImage().setVisible(false);
		dest.setIcon(tempIcon);
		
		AnchorPane parent = (AnchorPane) getParent();

		double dur = 300;

		Platform.runLater(() -> {

			parent.getChildren().add(animImg);
			animImg.relocate(srcb.getMinX() + imgb.getMinX(), srcb.getMinY() + imgb.getMinY());

			Timeline timeline = new Timeline();
			timeline.setCycleCount(1);
			timeline.setAutoReverse(false);
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(dur),
					new KeyValue(animImg.layoutXProperty(), destb.getMinX() + imgb.getMinX())));
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(dur),
					new KeyValue(animImg.layoutYProperty(), destb.getMinY() + imgb.getMinY())));

			timeline.setOnFinished((e) -> {
				parent.getChildren().remove(animImg);

				dest.getImage().setVisible(true);
			});

			timeline.play();

		});
		
	}

	private void addMove(String type, Field source, Field dest) {
		Move move = new Move(type, hash(source.getXX(), source.getYY()), hash(dest.getXX(), dest.getYY()));
		moves.add(move);
	}

	private int hash(int x, int y) {
		int realNumber = x * 10 + y + 1;
		if (x % 2 == 0) {
			return realNumber / 2;
		} else {
			return realNumber / 2 + 1;
		}
	}

	private boolean isPromotion(Field dest) {
		if (myPosition.getFigure().getClass() != QueenFigure.class) {
			if (((dest.getXX() == TOP) && myPosition.getFigure().getColor().equals(FigureColor.WOODEN))
					|| ((dest.getXX() == NUM_FIELDS - 1)
							&& myPosition.getFigure().getColor().equals(FigureColor.RED))) {
				return true;
			}
		}
		return false;
	}

	private void doPercussiveMove(Field dest) {
		if (myPosition.getFigure().getClass() == QueenFigure.class) {
			if (possibleMoves.indexOf(dest) > 0 && possibleMoves.indexOf(dest) < captured.size()) {
				return;
			}
			addMove(Move.PERCUSSIVE, myPosition, dest);
			queenNumMoves++;
			changePosition(dest);
			if (queenNumMoves >= captured.size()) {
				queenNumMoves = 0;
				highlight(false);
				possibleMoves.clear();
				percussive = false;
				capture();
				nextOnMove();
				if (isPromotion(myPosition))
					doPromotion();
				checkGame();
				check = true;
			} else {
				dest.highlight(true);
				possibleMoves.add(possibleMoves.remove(0));
			}
		} else {
			if (possibleMoves.indexOf(dest) > 0) {
				return;
			}

			addMove(Move.PERCUSSIVE, myPosition, dest);
			changePosition(dest);
			dest.highlight(true);
			possibleMoves.remove(dest);
			if (possibleMoves.isEmpty()) {
				percussive = false;
				capture();
				nextOnMove();
				if (isPromotion(myPosition))
					doPromotion();
				checkGame();
				check = true;
			}
		}
	}

	private void doQuietMove(Field dest) {
		addMove(Move.QUIET, myPosition, dest);
		changePosition(dest);
		highlight(false);
		possibleMoves.clear();
		nextOnMove();
		if (isPromotion(myPosition))
			doPromotion();
		checkGame();
		check = true;
	}

	private void doMove(Field dest) {
		if (percussive) {
			doPercussiveMove(dest);
		} else {
			doQuietMove(dest);
		}
	}

	private void fetchInfo(Field dest) {
		myPosition = dest;
		possibleMoves = myPosition.getFigure().getMoves(myPosition);
		captured = myPosition.getFigure().getCaptured();
		percussive = myPosition.getFigure().isPercurssiveMove();
	}

	public void move(Field dest) {
		Figure temp = dest.getFigure();
		if (temp == null) {
			if (possibleMoves.contains(dest)) {
				doMove(dest);
			}
		} else if (isOnMove(dest)) {
			if (check) {
				findLongestMove();
			}

			if ((whoCanDoPercussiveMove.contains(dest) || whoCanDoPercussiveMove.isEmpty())) {
				highlight(false);
				fetchInfo(dest);
				highlight(true);
			}
		}
	}

	private void findLongestMove() {
		List<Field> currentRoad = null;
		List<Field> tempRoad = possibleMoves;
		whoCanDoPercussiveMove.clear();
		check = false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (isOnMove(board[i][j])) {
					currentRoad = board[i][j].getFigure().getMoves(board[i][j]);
					currentRoad = board[i][j].getFigure().getCaptured();
					if (board[i][j].getFigure().isPercurssiveMove()) {
						if (currentRoad.size() > tempRoad.size()) {
							tempRoad = currentRoad;
							whoCanDoPercussiveMove.clear();
							whoCanDoPercussiveMove.add(board[i][j]);
						} else if (currentRoad.size() == tempRoad.size()) {
							whoCanDoPercussiveMove.add(board[i][j]);
						}
					}
				}
			}
		}
		currentRoad = null;
		tempRoad = null;
	}

	private boolean isLost() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Figure f = board[i][j].getFigure();
				if (isOnMove(board[i][j])) {
					if (f.getMoves(board[i][j]).size() > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean isDraw() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Figure figure = board[i][j].getFigure();
				if (figure != null) {
					if (figure.getMoves(board[i][j]).size() > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private Figure getFigure(int x, int y) {
		if (isIn(x, y))
			return board[x][y].getFigure();

		return null;
	}

	public String getLastMove() {
		if (!moves.isEmpty())
			return moves.get(moves.size() - 1).toString();
		return " ";
	}

	public boolean isIn(int x, int y) {
		return (x >= TOP) && (x < NUM_FIELDS) && (y >= TOP) && (y < NUM_FIELDS);
	}

	public Field getField(int x, int y) {
		if (isIn(x, y))
			return board[x][y];
		else
			return board[0][0];
	}

	public boolean isFigureNull(int x, int y) {
		if (isIn(x, y))
			return board[x][y].getFigure() == null;

		return false;
	}

	private void checkGame() {
		if (me == onMove)
			return;

		if (isDraw()) {
			if (drawHandler != null)
				drawHandler.handle();
		} else if (isLost()) {
			if (winHandler != null)
				winHandler.handle();
		} else if (turnHandler != null) {
			turnHandler.handle();
		}
	}

	public boolean isEnemy(int x, int y, int x1, int y1) {
		if (isIn(x, y) && isIn(x1, y1)) {
			if (!isFigureNull(x, y) && !isFigureNull(x1, y1))
				return getFigure(x, y).getColor() != getFigure(x1, y1).getColor();
		}

		return false;
	}

}
