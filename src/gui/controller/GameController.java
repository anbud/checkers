package gui.controller;

import gui.Action;
import gui.ActionXY;
import gui.Gui;
import gui.GuiFigure;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import checkersBoard.Board;

public class GameController {

	@FXML
	private ListView<TextFlow> chatList;
	
	@FXML
	private TextField chatInput;
	
	@FXML
	private Board gameBoard;
	
	@FXML
	private FlowPane gameBoardHolder;
	
	@FXML
	private Label gameInfo;
	
	@FXML
	private AnchorPane animationHolder;
	
	private FlowPane[][] board = new FlowPane[10][10];
	
	private Action chatSendHandler;
	private Action leaveGameHandler;
	private ActionXY fieldClickHandler;
	
	@FXML
	private void initialize() {	
		chatList.widthProperty().addListener((observable, oldval, newval) -> {
			for(TextFlow flow : chatList.getItems()) {
				flow.setMaxWidth((double) newval - 40);
			}
		});
		
		ChangeListener<Number> boardSizeListener = (observer, oldval, newval) -> {
			double size = Math.min( gameBoardHolder.getWidth() , gameBoardHolder.getHeight() );
			double width = Math.floor( size / 10 ) - 1;
			
			gameBoard.setPrefHeight( width * 10 + 10 );
			gameBoard.setPrefWidth( width * 10 + 10 );
			
			for(Node node : gameBoard.getChildren()) {
				FlowPane pane = (FlowPane) node;
				
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
	
	@FXML
	private void chatSendAction(ActionEvent event) {
		if(chatSendHandler == null)
			return;
		
		Platform.runLater(() -> {
			chatSendHandler.handle();
		});
	}
	
	@FXML
	private void leaveGameAction(ActionEvent event) {
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
	
	public void onFieldClick(ActionXY action) {
		fieldClickHandler = action;
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
	
	public void setFigure(int x, int y, GuiFigure figure) {
		if(figure == GuiFigure.NONE) {
			board[x][y].getChildren().clear();
			return;
		}
		
		double size = Math.floor( gameBoard.getWidth()/10 ) * 0.8;
			
		ImageView image = new ImageView( Gui.class.getResource(figure.file).toExternalForm() );
		image.setFitHeight(size);
		image.setFitWidth(size);		
        board[x][y].getChildren().add(image);
	}
	
	public void moveFigure(int srcx, int srcy, int destx, int desty) {
		this.moveFigure(srcx, srcy, destx, desty, null);
	}
	
	public void moveFigure(int srcx, int srcy, int destx, int desty, Action callback) {
		if(board[srcx][srcy].getChildren().isEmpty())
			return;
		
		Node piece = board[srcx][srcy].getChildren().get(0);
		
		Bounds src = board[srcx][srcy].getBoundsInParent();
		Bounds dest = board[destx][desty].getBoundsInParent();
		Bounds imgb = piece.getBoundsInParent();
		
		board[srcx][srcy].getChildren().clear();
		animationHolder.getChildren().add(piece);
		piece.relocate(src.getMinX() + imgb.getMinX(), src.getMinY() + imgb.getMinY());
                
		double dur = 400;
		
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		timeline.setAutoReverse(false);
		timeline.getKeyFrames().add(
				new KeyFrame(
						Duration.millis(dur),
						new KeyValue(piece.layoutXProperty(), dest.getMinX() + imgb.getMinX())));
		timeline.getKeyFrames().add(
				new KeyFrame(
						Duration.millis(dur),
						new KeyValue(piece.layoutYProperty(), dest.getMinY() + imgb.getMinY())));
		
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
	
	public void setGameInfo(String info) {
		gameInfo.setText(info + "'s turn");
	}
	
}