package agui;
/*package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings({"serial"})
public class Field extends JPanel {

	private JLabel lbl;
	private int x, y;
	private String color;
	private boolean selected;

	public Field(Color color, int x, int y) {
		this.x = x;
		this.y = y;
		this.selected = false;
		this.color = "";

		setBackground(color);
		setLayout(new BorderLayout());
		
		lbl = new JLabel();
		lbl.setHorizontalAlignment(SwingConstants.CENTER);

		add(lbl);
	}

	MouseAdapter adapter = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			highlight();
			possibleMoves();
		}
	};

	private void highlight() {
		if (!selected) {
			selected = true;
			setBorder(BorderFactory.createLineBorder(Color.magenta, 2));
		} else {
			selected = false;
			setBorder(null);
		}
	}
	
	private void possibleMoves() {
		if (color.equals("red")) {
			if (y > 1) {
				*//**
				 * Down left field is one row below and one column to the left but because this
				 * matrix indices starts from 0, x + 1 is actually x and y - 1 is actually y - 2. 
				 * e.g. (1, 2)'s down left field is (2, 1) which is (1, 0) in matrix. 
				 * (1, 2) -> (1, 0) i.e. (x, y) -> (x, y - 2)
				*//*
				Field fldLeft = CheckersBoard.board[x][y-2];
				if (fldLeft.color.equals(""))
					fldLeft.highlight();
			}
			if (y < 10) {
				Field fldRight = CheckersBoard.board[x][y];
				if (fldRight.color.equals(""))
					fldRight.highlight();
			}
		}
		
		if (color.equals("wooden")) {
			if (y > 1) {
				Field fldLeft = CheckersBoard.board[x-2][y-2];
				if (fldLeft.color.equals(""))
					fldLeft.highlight();
			}
			if (y < 10) {
				Field fldRight = CheckersBoard.board[x-2][y];
				if (fldRight.color.equals(""))
					fldRight.highlight();
			}
		}
	}
	
	public void setIcon(ImageIcon figure, String color) {
		lbl.setIcon(figure);
		addMouseListener(adapter);
		this.color = color;
	}

	public void removeIcon() {
		lbl.setIcon(null);
		removeMouseListener(adapter);
		this.color = "";
	}
	
}*/