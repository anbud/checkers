package gui.controller;

import gui.Action;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
		loginUsername.setText(username);
	}
	
	public void setLoginError(String error) {
		loginError.setText(error);
	}
	
	public void setButtonEnabled(boolean enabled) {
		loginButton.setDisable(!enabled);
	}
	
	public void setLoginVisible(boolean visible) {
		login.setVisible(visible);
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
	
}
