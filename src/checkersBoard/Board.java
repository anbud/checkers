package checkersBoard;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pieces.Figure;
import pieces.FigureColor;
import pieces.Piece;

public class Board {

	public static final int NUM_FIELDS = 10;
	public static final int TOP = 0;
	private JFrame frame;
	private Field[][] board = new Field[10][10];
	private final Icon redFigure = new ImageIcon("src/res/red_piece_60.png");
	private final Icon woodenFigure = new ImageIcon("src/res/wooden_piece_60.png");
	private Field source;
	private List<Field> possibleMoves;		

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
		source = null;
		possibleMoves = new ArrayList<>();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10, 10, 0, 0));
		frame.getContentPane().add(panel);
		
		for (int i = 0; i < NUM_FIELDS; i++) {
			for (int j = 0; j < NUM_FIELDS; j++) {							
				
				Figure figure = null;

				if ((i + j) % 2 != 0 && (i < 4)) {
					figure = new Piece(redFigure, FigureColor.RED);
				}
				if ((i + j) % 2 != 0 && (i > 5)) {
					figure = new Piece(woodenFigure, FigureColor.WOODEN);
				}

				if ((i + j) % 2 != 0) {
					board[i][j] = new Field(FieldColor.BLACK, figure, i, j);
					board[i][j].addMouseListener(listener);
				} else {
					board[i][j] = new Field(FieldColor.WHITE, null, i, j);					
				}
				
				panel.add(board[i][j]);
			}	
		}
	}
	
	private void doMove(Field source, Field dest) {
		Figure temp = source.getFigure();
		Icon tempIcon = source.getIcon();		
		source.setFigure(null);
		source.setIcon(null);
		dest.setFigure(temp);			
		dest.setIcon(tempIcon);
	}
	
	private void move(Field dest) {
		if (dest.getFigure() == null) {
			if (possibleMoves.contains(dest)) {
				doMove(source, dest);
			}
		} else {
			source = dest;
			possibleMoves = this.source.getFigure().getMoves(this.source, board);
		}		
	}
	
	MouseAdapter listener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {							
			move((Field)e.getSource());			
		}
	};
}
