package gui.controller;

import gui.Action;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LobbyController {

	@FXML
	private TextField loginUsername;
	
	@FXML
	private Label loginError;
	
	private Action loginButtonHandler;
	
	@FXML
	private void loginButtonAction(ActionEvent event) {
		if(loginButtonHandler == null)
			return;
		
		Platform.runLater(() -> {
			loginButtonHandler.handle();
		});
	}
	
	public void onLoginButton(Action action) {
		loginButtonHandler = action;
	}
	
	public String getLoginUsername() {
		return loginUsername.getText();
	}
	
	public void setLoginError(String error) {
		loginError.setText(error);
	}
	
}
