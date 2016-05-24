package gui.controller;

import gui.Action;
import gui.Gui;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
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
	
	@FXML
	private ImageView chatMute;
	
	@FXML
	private Tooltip muteTooltip;
	
	private Action loginButtonHandler;
	private Action chatSendHandler;
	private Consumer<String> requestHandler;
	private Consumer<String> acceptHandler;
	private Consumer<String> rejectHandler;
	private Action chatMuteHandler;

	@FXML
	private void initialize() {
		Label msgGames = new Label("Currently no games");
		msgGames.getStyleClass().add("empty-view-msg");
		gameList.setPlaceholder(msgGames);

		Label msgPlayers = new Label("Currently no players");
		msgPlayers.getStyleClass().add("empty-view-msg");
		playerList.setPlaceholder(msgPlayers);
		
		Label msgRequests = new Label("Currently no requests");
		msgRequests.getStyleClass().add("empty-view-msg");
		requestList.setPlaceholder(msgRequests);
		
		setMuted(false);
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
	private void loginButtonAction(ActionEvent event) {
		if(loginButtonHandler == null)
			return;
		
		Platform.runLater(() -> {
			loginButtonHandler.handle();
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
	
	public void onChatButton(Action action) {
		chatSendHandler = action;
	}
	
	public void onLoginButton(Action action) {
		loginButtonHandler = action;
	}
	
	public void onRequestButton(Consumer<String> action) {
		requestHandler = action;
	}
	
	public void onAcceptButton(Consumer<String> action) {
		acceptHandler = action;
	}
	
	public void onRejectButton(Consumer<String> action) {
		rejectHandler = action;
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
	
	public void addChatMessage(String username, String message, boolean out) {
		TextFlow flow = new TextFlow();
		
		Text text = new Text(username);
		text.setStyle("-fx-font-weight: bold;");
		flow.getChildren().add(text);
		
		text = new Text("\n"+message);
		flow.getChildren().add(text);
		
		flow.setMaxWidth(chatList.getWidth() - 40);
		
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
			
			final String ss = s;
			button.setOnMouseClicked((e) -> {
				if(requestHandler != null) {
					requestHandler.accept(ss);
				}
			});

			AnchorPane box = new AnchorPane();
			box.getChildren().add(name);
			box.getChildren().add(button);

			box.getStyleClass().add("list-items");
			
			AnchorPane.setLeftAnchor(name, 0.0);
			AnchorPane.setTopAnchor(name, 0.0);
			AnchorPane.setBottomAnchor(name, 0.0);
			AnchorPane.setRightAnchor(button, 0.0);
			AnchorPane.setTopAnchor(button, 0.0);
			AnchorPane.setBottomAnchor(button, 0.0);
			
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
			
			flow.setStyle("-fx-font-size: 16px;");
			flow.getStyleClass().add("list-items");
			
			nodes.add(flow);
		}
		Platform.runLater(() -> {
			gameList.getItems().setAll(nodes);
		});
	}
	
	public void setRequests(List<String> requests) {
		List<Node> temp = new ArrayList<>();
		
		for(String s : requests) {
			Label name = new Label();
			name.setText(s);
			name.getStyleClass().add("request-label");
			
			ImageView acceptImg = new ImageView(Gui.class.getResource("view/img/accept-mark.png").toExternalForm());
			acceptImg.setFitHeight(20);
			acceptImg.setFitWidth(20);
			
			Button accept = new Button("", acceptImg);
			accept.getStyleClass().add("request-accept");
			
			accept.setOnMouseEntered((e) -> {
				accept.setScaleX(1.3);
				accept.setScaleY(1.3);
			});
			
			final String ss = s;
			accept.setOnMouseClicked((e) -> {
				if(acceptHandler != null) {
					acceptHandler.accept(ss);
				}
			});
			
			accept.setOnMouseExited((e) -> {
				accept.setScaleX(1.0);
				accept.setScaleY(1.0);
			});
			
			ImageView rejectImg = new ImageView(Gui.class.getResource("view/img/reject-mark.png").toExternalForm());
			rejectImg.setFitHeight(20);
			rejectImg.setFitWidth(20);
			
			Button reject = new Button("", rejectImg);
			reject.getStyleClass().add("request-accept");
			
			reject.setOnMouseEntered((e) -> {
				reject.setScaleX(1.25);
				reject.setScaleY(1.25);
			});
			
			reject.setOnMouseExited((e) -> {
				reject.setScaleX(1.0);
				reject.setScaleY(1.0);
			});
			
			reject.setOnMouseClicked((e) -> {
				if(rejectHandler != null) {
					rejectHandler.accept(ss);
				}
			});

			AnchorPane box = new AnchorPane();
			box.getChildren().add(name);
			box.getChildren().add(accept);
			box.getChildren().add(reject);
                        
			AnchorPane.setLeftAnchor(name, 0.0);
			AnchorPane.setTopAnchor(name, 0.0);
			AnchorPane.setBottomAnchor(name, 0.0);
			
			AnchorPane.setRightAnchor(accept, 30.0);
			AnchorPane.setRightAnchor(reject, 0.0);
			
			temp.add(box);
		}
		
		Platform.runLater(() -> {
			requestList.getItems().setAll(temp);
		});
	}
	
}
