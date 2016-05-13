package agui;
/*package gui;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import checkersBoard.Field;
import checkersBoard.FieldColor;

public class CheckersBoard {

	private static final int NUM_FIELDS = 10;
	private JFrame frame;
	public static Field[][] board = new Field[10][10];

	//private ImageIcon redFigure = new ImageIcon("src/gui/res/red_figure_60.png");
	//private ImageIcon woodenFigure = new ImageIcon("src/gui/res/wooden_figure_60.png");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				CheckersBoard window = new CheckersBoard();
				window.frame.setVisible(true);
			}
		});
	}

	public CheckersBoard() {			
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
		initFrame();

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10, 10, 0, 0));
		frame.getContentPane().add(panel);

		for (int i = 0; i < NUM_FIELDS; i++)
			for (int j = 0; j < NUM_FIELDS; j++) {
				Field pnl = null;

				if ((i + j) % 2 != 0) {
					pnl = new Field(FieldColor.BLACK, null);
					pnl.setXX(i);
					pnl.setYY(j);
				} else {
					pnl = new Field(FieldColor.WHITE, null);
					pnl.setXX(i);
					pnl.setYY(j);
				}

				if ((i + j) % 2 != 0 && (i <= 4)) {
					//pnl.setIcon(redFigure, "red");
				}
				if ((i + j) % 2 != 0 && (i >= 7)) {
					//pnl.setIcon(woodenFigure, "wooden");
				}

				board[i][j] = pnl;

				panel.add(pnl);
			}
	}

}*/