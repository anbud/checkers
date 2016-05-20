package gui;

public enum Figure {
	WHITE("view/img/wooden_piece.png"),
	WHITE_QUEEN("view/img/queen_wooden_piece.png"),
	BLACK("view/img/red_piece.png"),
	BLACK_QUEEN("view/img/queen_red_piece.png"),
	NONE(null);
	
	public String file;
	
	Figure(String file) {
		this.file = file;
	}
}
