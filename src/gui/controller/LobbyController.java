package gui.controller;

import gui.Action;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class LobbyController {

	@FXML
	private TextField loginUsername;
	
	@FXML
	private Label loginError;
	
	@FXML
	private Button loginButton;
	
	@FXML
	private FlowPane login;
	
	@FXML
	private ListView<TextFlow> chatList;
	
	@FXML
	private ListView<Node> playerList;
	
	@FXML
	private ListView<Node> requestList;
	
	@FXML
	private ListView<Node> gameList;
	
	@FXML
	private TextField chatInput;
	
	private Action loginButtonHandler;
	private Action chatSendHandler;
	
	@FXML
	private void chatSendAction(ActionEvent event) {
		if(chatSendHandler == null)
			return;
		
		Platform.runLater(() -> {
			chatSendHandler.handle();
		});
	}
	
	@FXML
	private void loginButtonAction(ActionEvent event) {
		if(loginButtonHandler == null)
			return;
		
		Platform.runLater(() -> {
			loginButtonHandler.handle();
		});
	}
	
	public void onChatButton(Action action) {
		chatSendHandler = action;
	}
	
	public void onLoginButton(Action action) {
		loginButtonHandler = action;
	}
	
	public String getLoginUsername() {
		return loginUsername.getText();
	}
	
	public void setLoginUsername(String username) {
		Platform.runLater(() -> loginUsername.setText(username));
	}
	
	public void setLoginError(String error) {
		Platform.runLater(() -> loginError.setText(error));
	}
	
	public void setButtonEnabled(boolean enabled) {
		Platform.runLater(() -> loginButton.setDisable(!enabled));
	}
	
	public void setLoginVisible(boolean visible) {
		Platform.runLater(() -> login.setVisible(visible));
	}
	
	public void setChatInput(String text) {
		Platform.runLater(() -> chatInput.setText(text));
	}
	
	public String getChatInput() {
		return chatInput.getText();
	}
	
	public void addChatMessage(String username, String message) {
		TextFlow flow = new TextFlow();
		
		Text text = new Text(username);
		text.setStyle("-fx-font-weight: bold;");
		flow.getChildren().add(text);
		
		text = new Text("\n"+message);
		flow.getChildren().add(text);
		
		flow.setMaxWidth(chatList.getWidth() - 40);
		
		
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
		
		flow.setMaxWidth(chatList.getWidth() - 40);
		flow.setTextAlignment(TextAlignment.CENTER);
		
		Platform.runLater(() -> {
			chatList.getItems().add(flow);
		
			chatList.scrollTo(chatList.getItems().size());
		});
	}
	
	public void setPlayers(List<String> players) {
		List<Node> temp = new ArrayList<>();
		
		for(String s : players) {
			Label name = new Label();
			name.setText(s);
			name.getStyleClass().add("request-label");
			
			Button button = new Button("request");
			button.getStyleClass().add("request-button");

			AnchorPane box = new AnchorPane();
			box.getChildren().add(name);
			box.getChildren().add(button);
			
			AnchorPane.setLeftAnchor(name, 0.0);
			AnchorPane.setTopAnchor(name, 0.0);
			AnchorPane.setBottomAnchor(name, 0.0);
			AnchorPane.setRightAnchor(button, 0.0);
			
			temp.add(box);
		}
		
		Platform.runLater(() -> {
			playerList.getItems().setAll(temp);
		});
		
	}
	
	public void setGames(List<String> games) {
		List<Node> nodes = new ArrayList<>();
		
		for(String g : games) {
			String[] players = g.split(" ");
			
			TextFlow flow = new TextFlow();
		
			Text text = new Text(players[0]);
			text.setStyle("-fx-font-weight: bold;");
			flow.getChildren().add(text);

			text = new Text(" vs ");
			flow.getChildren().add(text);
			
			text = new Text(players[1]);
			text.setStyle("-fx-font-weight: bold;");
			flow.getChildren().add(text);
			
			flow.setTextAlignment(TextAlignment.CENTER);
			
			flow.setStyle("-fx-font-size: 16px; -fx-padding: 8px 0 8px 0; -fx-border-width: 0 0 1px 0; -fx-border-color: #cccccc;");
			
			nodes.add(flow);
		}
		Platform.runLater(() -> {
			gameList.getItems().setAll(nodes);
		});
	}
	
}
