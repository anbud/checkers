package gui;

public enum GuiFigure {
	WHITE("view/img/wooden_piece.png"),
	WHITE_QUEEN("view/img/queen_wooden_piece.png"),
	BLACK("view/img/red_piece.png"),
	BLACK_QUEEN("view/img/queen_red_piece.png"),
	NONE(null);
	
	public String file;
	
	GuiFigure(String file) {
		this.file = file;
	}
}
