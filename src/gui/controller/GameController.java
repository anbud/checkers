package gui.controller;

import gui.Action;
import gui.Gui;
import gui.Piece;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class GameController {

	@FXML
	private ListView<TextFlow> chatList;
	
	@FXML
	private TextField chatInput;
	
	@FXML
	private TilePane gameBoard;
	
	@FXML
	private FlowPane gameBoardHolder;
	
	@FXML
	private AnchorPane animationHolder;
	
	private FlowPane[][] board = new FlowPane[10][10];
	
	private String blackPiece = Gui.class.getResource("view/img/red_piece_65.png").toExternalForm();
	private String whitePiece = Gui.class.getResource("view/img/wooden_piece_65.png").toExternalForm();
	
	private Action chatSendHandler;
	private Action leaveGameHandler;
	
	@FXML
	private void initialize() {	
		chatList.widthProperty().addListener((observable, oldval, newval) -> {
			for(TextFlow flow : chatList.getItems()) {
				flow.setMaxWidth((double) newval - 40);
			}
		});
		
		double fieldSize = Math.floor( gameBoard.getWidth() / 10 );
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				board[i][j] = new FlowPane();
				
				if((i + j) % 2 == 0)
					board[i][j].setStyle("-fx-background-color: #f4f4f4;");
				else
					board[i][j].setStyle("-fx-background-color: #272625;");
				
				board[i][j].setPrefHeight( fieldSize );
				board[i][j].setPrefWidth( fieldSize );
				
				board[i][j].setAlignment(Pos.CENTER);
				
				gameBoard.getChildren().add(board[i][j]);
			}
		}
		
		ChangeListener<Number> boardSizeListener = (observer, oldval, newval) -> {
			double size = Math.min( gameBoardHolder.getWidth() , gameBoardHolder.getHeight() );
			
			gameBoard.setPrefHeight( size );
			gameBoard.setPrefWidth( size );
			
			for(Node node : gameBoard.getChildren()) {
				FlowPane pane = (FlowPane) node;
				double width = Math.floor( size / 10 );
				
				pane.setPrefWidth( width );
				pane.setPrefHeight( width );
				
				if(pane.getChildren().size() > 0) {
					ImageView image = (ImageView) pane.getChildren().get(0);
					image.setFitHeight(width*0.8);
					image.setFitWidth(width*0.8);
				}
			}
		};
		
		gameBoardHolder.widthProperty().addListener(boardSizeListener);
		gameBoardHolder.heightProperty().addListener(boardSizeListener);
	}
	
	public void chatSendAction(ActionEvent event) {
		if(chatSendHandler == null)
			return;
		
		Platform.runLater(() -> {
			chatSendHandler.handle();
		});
	}
	
	public void leaveGameAction(ActionEvent event) {
		if(leaveGameHandler == null)
			return;
		
		Platform.runLater(() -> {
			leaveGameHandler.handle();
		});
	}
	
	public void setChatInput(String text) {
		chatInput.setText(text);
	}
	
	public String getChatInput() {
		return chatInput.getText();
	}
	
	public void onChatButton(Action action) {
		chatSendHandler = action;
	}
	
	public void onLeaveButton(Action action) {
		leaveGameHandler = action;
	}
	
	public void addChatMessage(String username, String message) {
		TextFlow flow = new TextFlow();
		
		Text text = new Text(username);
		text.setStyle("-fx-font-weight: bold;");
		flow.getChildren().add(text);
		
		text = new Text(":\n"+message);
		flow.getChildren().add(text);
		
		flow.setMaxWidth(chatList.getWidth() - 40);
		
		chatList.getItems().add(flow);
		
		chatList.scrollTo(chatList.getItems().size());
	}
	
	public void addChatInfo(String message) {
		TextFlow flow = new TextFlow();
		
		Text text = new Text(message);
		text.setStyle("-fx-font-style: italic;");
		flow.getChildren().add(text);
		
		flow.setMaxWidth(chatList.getWidth() - 40);
		flow.setTextAlignment(TextAlignment.CENTER);
		
		chatList.getItems().add(flow);
		
		chatList.scrollTo(chatList.getItems().size());
	}
	
	public void setPiece(int x, int y, Piece piece) {
		board[x][y].getChildren().clear();
		
		if(piece != Piece.NONE) {
			String loc = "";
			if(piece == Piece.WHITE)
				loc = whitePiece;
			else if(piece == Piece.BLACK)
				loc = blackPiece;
			
			double size = Math.floor( gameBoard.getWidth()/10 ) * 0.8;
			
			ImageView image = new ImageView(loc);
			image.setFitHeight(size);
			image.setFitWidth(size);
			
			board[x][y].getChildren().add(image);
		}
	}
	
	public void movePiece(int srcx, int srcy, int destx, int desty) {
		this.movePiece(srcx, srcy, destx, desty, null);
	}
	
	public void movePiece(int srcx, int srcy, int destx, int desty, Action callback) {
		if(board[srcx][srcy].getChildren().isEmpty())
			return;
		
		Node piece = board[srcx][srcy].getChildren().get(0);
		
		Bounds src = board[srcx][srcy].getBoundsInParent();
		Bounds dest = board[destx][desty].getBoundsInParent();
		Bounds imgb = piece.getBoundsInParent();
		
		board[srcx][srcy].getChildren().clear();
		animationHolder.getChildren().add(piece);
		piece.relocate(src.getMinX() + imgb.getMinX(), src.getMinY() + imgb.getMinY());
		
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		timeline.setAutoReverse(false);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue(piece.layoutXProperty(), dest.getMinX() + imgb.getMinX())));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue(piece.layoutYProperty(), dest.getMinY() + imgb.getMinY())));
		
		timeline.setOnFinished((e) -> {
			animationHolder.getChildren().remove(piece);
			board[destx][desty].getChildren().add(piece);
			
			if(callback != null)
				Platform.runLater(() -> {
					callback.handle();
				});
		});
		
		timeline.play();
	}
	
}


