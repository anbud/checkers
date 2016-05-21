package gui;

import java.util.concurrent.CountDownLatch;

import gui.controller.GameController;
import gui.controller.LobbyController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Gui extends Application {
	
	private static Gui instance;
	
	private Stage stage;
	
	private Node lobbyView;
	private LobbyController lobbyController;
	
	private static Action onInitHandler;
	private static Action onStartHandler;
	private static Action onStopHandler;
	private static boolean onInitMissed = false;
	private static boolean onStartMissed = false;
	private static boolean onStopMissed = false;
	
	private static CountDownLatch cdl = new CountDownLatch(2);
	
	public Gui() {
		instance = this;
	}
	
	public static Gui getInstance() {
		while(instance == null) {
			try {
				cdl.await();
			}
			catch(Exception e) {}
		}
		return instance;
	}
	
	public static void onInit(Action a) {
		onInitHandler = a;
		
		if(onInitMissed)
			Platform.runLater(() -> onInitHandler.handle());
	}
	
	public static void onStart(Action a) {
		onStartHandler = a;
		
		if(onStartMissed)
			Platform.runLater(() -> onStartHandler.handle());
	}
	
	public static void onClose(Action a) {
		onStopHandler = a;
		
		if(onStopMissed)
			Platform.runLater(() -> onStopHandler.handle());
	}
	
	@Override
	public void init() {
		preloadViews();
		
		if(onInitHandler != null)
			Platform.runLater(() -> onInitHandler.handle());
		onInitMissed = true;
		
		cdl.countDown();
	}
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		//setUserAgentStylesheet(getClass().getResource("view/style.css").toExternalForm());
		
		stage.setTitle("");
		
		stage.setScene(new Scene(new AnchorPane(), 900, 600));
		
		stage.show();
		
		if(onStartHandler != null)
			Platform.runLater(() -> onStartHandler.handle());
		onStartMissed = true;
		
		cdl.countDown();
	}
	
	@Override
	public void stop() {
		if(onStopHandler != null)
			onStopHandler.handle();
		onStopMissed = true;
	}
	
	private void preloadViews() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LobbyView.fxml"));
			lobbyView = loader.load();
			lobbyController = (LobbyController) loader.getController();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadLobbyView() {
		loadLobbyView(false);
	}
	
	public LobbyController loadLobbyView(boolean login) {
		Platform.runLater(() -> {
			lobbyController.setLoginVisible(login);
			stage.getScene().setRoot((Parent) lobbyView);
		});
		return lobbyController;
	}
	
	public GameController loadGameView() {
		GameController ret = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/GameView.fxml"));
			Node gameView = loader.load();
			ret = (GameController) loader.getController();
			
			Platform.runLater(() -> {
				stage.getScene().setRoot((Parent) gameView);
			});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public static void main(String[] args) {
		
		// pokretanje GUI-a
		new Thread(() -> {
			Application.launch(Gui.class, args);
		}).start();
		
		// instanca Gui
		Gui gui = Gui.getInstance();
		
		LobbyController c = gui.loadLobbyView(true);
		
		c.onLoginButton(() -> {
			System.out.println("login");
		});
		
		/*
		// otvaranje GameView-a, vraca controller za taj view
		GameController c = gui.loadGameView();
		
		// stavi crnu stvarcicu u polje 1,1
		c.setFigure(1, 1, Figure.BLACK_QUEEN);
		
		// callback za "LEAVE GAME" dugme
		c.onLeaveButton(() -> {
			// animacija koraka, (1,1) -> (4,5), + callback (for fun)
			c.moveFigure(1, 1, 4, 5, () -> System.out.println("moved"));
		});
		
		// callback za poslatu poruku (dugme ili Enter)
		c.onChatButton(() -> {
			// it's self explanatory
			c.addChatMessage("vsakos", c.getChatInput());
			c.setChatInput("");
		});
		
		c.onFieldClick((x,y) -> {
			System.out.println(x + " " + y);
		});
		
		c.setGameInfo("Asd poi");

		*/
	}

}
