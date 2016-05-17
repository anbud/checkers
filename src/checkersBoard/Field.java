package checkersBoard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import figures.Figure;

@SuppressWarnings("serial")
public class Field extends JPanel {
	
	private FieldColor color;
	private Figure figure;
	private JLabel label;
	private int x;
	private int y;
	private boolean visited;
	
	public Field(FieldColor color, Figure figure, int x, int y) {
		this.color = color;
		this.figure = figure;
		this.x = x;
		this.y = y;
		this.visited = false;
		setLayout(new BorderLayout());
		setBackground(color.getColor());
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label);
		if (figure != null) {			
			label.setIcon(figure.getIcon());						
		}		
	}

	public FieldColor getColor() {
		return color;
	}

	public void setColor(FieldColor color) {
		this.color = color;
	}

	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {		
		this.figure = figure;
	}	
	
	public Icon getIcon() {
		return label.getIcon();
	}
	
	public void setIcon(Icon icon) {
		label.setIcon(icon);
	}
	
	public int getXX() {
		return x;
	}
	
	public void setXX(int x) {
		this.x = x;
	}
	
	public int getYY() {
		return y;
	}
	
	public void setYY(int y) {
		this.y = y;
	}	
	
	public void highlight(boolean selected) {
		if (selected) {			
			setBorder(null);						
		} else {
			setBorder(BorderFactory.createLineBorder(Color.magenta, 2));
		}
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
