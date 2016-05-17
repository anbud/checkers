package checkersBoard;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import figures.Figure;
import figures.FigureColor;
import figures.QueenFigure;
import figures.SimpleFigure;

public class Board {

	private JFrame frame;
	private final int NUM_FIELDS = 10;
	private final int TOP = 0;
	private Field[][] board = new Field[10][10];
	private final Icon redFigure = new ImageIcon("src/res/red_piece_60.png");
	private final Icon woodenFigure = new ImageIcon("src/res/wooden_piece_60.png");
	private Field myPosition;
	private List<Field> possibleMoves;
	private boolean percussive;
	private FigureColor onMove;	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Board window = new Board();
				window.frame.setVisible(true);
			}
		});
	}

	public Board() {
		initFrame();
		initialize();
	}

	private void initFrame() {
		frame = new JFrame("Checkers");
		frame.setSize(700, 700);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initialize() {
		myPosition = null;
		possibleMoves = new LinkedList<>();
		percussive = false;
		onMove = FigureColor.WOODEN;

		JPanel pnlBoard = setFigures();
		frame.getContentPane().add(pnlBoard);
	}

	private JPanel setFigures() {
		JPanel pnlBoard = new JPanel();
		pnlBoard.setLayout(new GridLayout(10, 10, 0, 0));
		for (int i = 0; i < NUM_FIELDS; i++) {
			for (int j = 0; j < NUM_FIELDS; j++) {
				Figure figure = null;
				if ((i + j) % 2 != 0 && (i < 4)) {
					figure = new SimpleFigure(redFigure, FigureColor.RED, this);
				}
				if ((i + j) % 2 != 0 && (i > 5)) {
					figure = new SimpleFigure(woodenFigure, FigureColor.WOODEN, this);
				}
				if ((i + j) % 2 != 0) {
					board[i][j] = new Field(FieldColor.BLACK, figure, i, j);
					board[i][j].addMouseListener(listener);
				} else {
					board[i][j] = new Field(FieldColor.WHITE, null, i, j);
				}
				pnlBoard.add(board[i][j]);
			}
		}
		return pnlBoard;
	}

	private void doPromotion() {
		Figure promoted = new QueenFigure(myPosition.getFigure().getIcon(), myPosition.getFigure().getColor(), this);
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

	private void capture() {
		for (Field f : myPosition.getFigure().getCaptured()) {
			f.setFigure(null);
			f.setIcon(null);
		}
	}

	private void changePosition(Field dest) {
		Figure temp = myPosition.getFigure();
		Icon tempIcon = myPosition.getIcon();
		myPosition.setFigure(null);
		myPosition.setIcon(null);
		dest.setFigure(temp);
		dest.setIcon(tempIcon);
		myPosition = dest;
	}

	private boolean isPromotion(Field dest) {
		if (dest.getXX() == TOP || dest.getXX() == NUM_FIELDS - 1) {
			return true;
		}
		return false;
	}

	private void doPercussiveMove(Field dest) {
		if (possibleMoves.indexOf(dest) > 0) {
			return;
		}
		changePosition(dest);		
		dest.highlight(true);
		possibleMoves.remove(dest);
		if (possibleMoves.isEmpty()) {
			percussive = false;
			capture();
			nextOnMove();
			if (isPromotion(myPosition))
				doPromotion();
		}
	}

	private void doQuietMove(Field dest) {
		changePosition(dest);		
		highlight(false);
		possibleMoves.clear();
		nextOnMove();
		if (isPromotion(myPosition))
			doPromotion();
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
		percussive = myPosition.getFigure().isPercurssiveMove();
	}

	private void move(Field dest) {
		Figure temp = dest.getFigure();
		if ((temp == null)) {
			if (possibleMoves.contains(dest)) {
				doMove(dest);
			}
		} else if (!percussive && isOnMove(dest)) {
			highlight(false);
			fetchInfo(dest);
			highlight(true);
		}
	}

	private Figure getFigure(int x, int y) {
		if (isIn(x, y))
			return board[x][y].getFigure();

		return null;
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

	public boolean isEnemy(int x, int y, int x1, int y1) {
		if (isIn(x, y) && isIn(x1, y1)) {
			if (!isFigureNull(x, y) && !isFigureNull(x1, y1))
				return getFigure(x, y).getColor() != getFigure(x1, y1).getColor();
		}

		return false;
	}

	MouseAdapter listener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			move((Field) e.getSource());
		}
	};
}
