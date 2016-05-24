package gui.controller;

import gui.Action;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import checkersBoard.Board;
import gui.Gui;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;

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
	private AnchorPane glass;
	
	@FXML
	private SplitPane divider;
	
	@FXML
	private AnchorPane animationHolder;
	
	@FXML
	private ImageView chatMute;
	
	@FXML
	private Tooltip muteTooltip;
	
	private FlowPane[][] board = new FlowPane[10][10];
	
	private Action chatSendHandler;
	private Action leaveGameHandler;
	private Action chatMuteHandler;
	
	@FXML
	private void initialize() {
		ChangeListener<Number> chatWidthListener = (observable, oldval, newval) -> {
			for(TextFlow flow : chatList.getItems()) {
				flow.setMaxWidth((double) newval - 30);
			}
		};
		
		chatWidthListener.changed(null, 0, chatList.getWidth());
		
		chatList.widthProperty().addListener(chatWidthListener);
		
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
	
	@FXML
	private void chatMuteAction(ActionEvent event) {
		if(chatMuteHandler == null)
			return;
		
		Platform.runLater(() -> {
			chatMuteHandler.handle();
		});
	}
	
	public void setChatInput(String text) {
		Platform.runLater(() -> chatInput.setText(text));
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
	
	public void onMute(Action action) {
		chatMuteHandler = action;
	}
	
	public void setMuted(boolean muted) {
		Platform.runLater(() -> {
			if(muted) {
				chatMute.setImage(Gui.chatUnmute);
				muteTooltip.setText("Enable chat sound");
			}
			else {
				chatMute.setImage(Gui.chatMute);
				muteTooltip.setText("Mute chat sound");
			}
		});
	}
	
	public boolean isMuted() {
		return chatMute.getImage() == Gui.chatUnmute;
	}
	
	public void addChatMessage(String username, String message, boolean out) {
		TextFlow flow = new TextFlow();
		
		Text text = new Text(username);
		text.setStyle("-fx-font-weight: bold;");
		flow.getChildren().add(text);
		
		text = new Text("\n"+message);
		flow.getChildren().add(text);
		
		flow.setMaxWidth(chatList.getWidth() - 30);
		
		if(out)
			flow.setTextAlignment(TextAlignment.RIGHT);
		
		Platform.runLater(() -> {
			chatList.getItems().add(flow);

			chatList.scrollTo(chatList.getItems().size());
		});
	}
	
	public void addChatInfo(String message) {
		TextFlow flow = new TextFlow();
		
		Text text = new Text(message);
		text.setStyle("-fx-font-style: italic;");
		flow.getChildren().add(text);
		
		flow.setMaxWidth(chatList.getWidth() - 30);
		flow.setTextAlignment(TextAlignment.CENTER);
		
		Platform.runLater(() -> {
			chatList.getItems().add(flow);

			chatList.scrollTo(chatList.getItems().size());
		});
	}
	
	public void setGameInfo(String info) {
		Platform.runLater(() -> gameInfo.setText(info + "'s turn"));
	}
	
	public Board getBoard() {
		return gameBoard;
	}
	
	public void setBlockBoard(boolean block) {
		Platform.runLater(() -> glass.setVisible(block));
	}
	
	public void setDivider() {
		divider.setDividerPositions(0.65);
	}
	
}