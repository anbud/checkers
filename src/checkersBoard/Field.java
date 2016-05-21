package checkersBoard;

import figures.Figure;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

@SuppressWarnings("serial")
public class Field extends FlowPane {
	
	private FieldColor color;
	private Figure figure;
	private ImageView label;
	private int x;
	private int y;
	private boolean visited;
	
	public Field(FieldColor color, Figure figure, int x, int y, double size) {
		this.color = color;
		this.figure = figure;
		this.x = x;
		this.y = y;
		this.visited = false;
		setStyle("-fx-background-color: "+color.getColor());
		
		label = new ImageView();
		label.setFitHeight(size);
		label.setFitWidth(size);
		
		getChildren().add(label);
		if (figure != null) {			
			label.setImage(figure.getIcon());
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
	
	public Image getIcon() {
		return label.getImage();
	}
	
	public void setIcon(Image icon) {
		label.setImage(icon);
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
			setStyle("-fx-border-width: 0; -fx-background-color: "+color.getColor());
		} else {
			setStyle("-fx-border-width: 2px; -fx-border-color: magenta;; -fx-background-color: "+color.getColor());
		}
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
