import gui.Gui;
import gui.controller.GameController;
import gui.controller.LobbyController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Application;
import javafx.application.Platform;

public class Client {
	
	private Gui gui;
	private static PrintWriter out;
	private static BufferedReader in;
	private GameController c;
	private LobbyController l;
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	private String gameId;
	private String username;
	private String whosOnMove;
	
	public Client() {
		gui = Gui.getInstance();
		l = gui.loadLobbyView(true);
		initialize();
	}
	
	private void initialize() {
		gameId = "";
		username = "default";
		whosOnMove = username;
		
		//try(Socket client = new Socket("localhost", 110)) {
		try(Socket client = new Socket("hekate.zx.rs", 110)) {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			
			initLobbyCallbacks();
			
			try {
				in.readLine();
				@SuppressWarnings("unused")
				String line;
				while (!Thread.interrupted())
					process(line = in.readLine(), in);
				
			} catch (Exception e) {
				System.out.println("io Exception");
			}
			
		} catch (Exception e) {
			gui.showMessage("Server error", "Server's not up.");
			Platform.exit();
		}
	}

	private void process(String line, BufferedReader in) {
		try {
			if (line.startsWith("PING")) 
				out.println("PONG");
			else if (line.startsWith("E_LAG")) { }
			else if (line.startsWith("E_OK"))
				queue.add(line);
			
			else if (line.equals("E_USERS:")) {
				LinkedList<String> users = new LinkedList<>();
				while (!(line = in.readLine()).equals("E_END")) 
					users.add(line);
				
				l.setPlayers(users);
			}
			else if (line.equals("E_GAMES:")) {
				LinkedList<String> games = new LinkedList<>();
				while (!(line = in.readLine()).equals("E_END")) 
					games.add(line);
				
				l.setGames(games);
			}
			else if (line.startsWith("E_TURN")) {
				whosOnMove = line.substring(line.indexOf(":") + 1);
				c.setGameInfo(whosOnMove);
			}

			else if (line.startsWith("E_LOBBY_MESSAGE")) {
				String msg = line.substring(line.indexOf(":") + 2);
				String userSent = msg.substring(0, msg.indexOf(":"));
				l.addChatMessage(userSent, msg.substring(msg.indexOf(":") + 2), userSent.equals(username));
			}
			else if (line.startsWith("E_LOBBY_INFO")) {
				String msg = line.substring(line.indexOf(":") + 2);
				l.addChatInfo(msg.substring(msg.indexOf(":") + 2));
			} 
			else if (line.startsWith("E_MESSAGE")) {
				String msg = line.substring(line.indexOf(":") + 2);
				String userSent = msg.substring(0, msg.indexOf(":"));
				c.addChatMessage(userSent, msg.substring(msg.indexOf(":") + 2), userSent.equals(username));
			} 
			else if (line.startsWith("E_INFO")) {
				String msg = line.substring(line.indexOf(":") + 2);
				l.addChatInfo(msg.substring(msg.indexOf(":") + 2));
			}
			else if (line.equals("E_REQUESTS")) {
				LinkedList<String> requests = new LinkedList<>();
				while (!(line = in.readLine()).equals("E_END")) 
					requests.add(line);
				
				l.setRequests(requests);
			}
			else if (line.startsWith("E_GAME_ACCEPTED")) {
				// FIX - treba lista u LobbyViewu
				// l.getRequestList().remove(line.substring(line.indexOf(":") + 2));
				c = gui.loadGameView();
				initGameCallbacks();
			}
			else if (line.startsWith("E_GAME_DECLINED")) {
				//						
			}
			else if (line.equals("E_INVALID_MOVE"))
				l.addChatInfo("It's opponenets move."); // da li može doći do ovoga (kako je logika impl)?
			
			else if (line.equals("E_GAME_OVER")) {
				// treba obraditi slučaj kada je korisnik prekinuo igru - onPlayerLeave()
				// i kada je igra završena pobjedom nekog od takmičara - checkState())
				// ili eventualno nerješeno
			}
			else {
				queue.add(line);
				System.out.println(line);
			}
		} catch (IOException e) { }
	}
	
	@SuppressWarnings("unused")
	private void toChatInfo(String line) {
		final String fin = line;
		l.addChatInfo(fin);
	}
	
	private void initLobbyCallbacks() {
		l.onLoginButton(() -> {
			l.setButtonEnabled(false);
			l.setLoginError("");
			username = l.getLoginUsername().trim();
			if (username.equals("")) {
				l.setButtonEnabled(true);
				return;
			}
			
			out.println("LOGIN: " + username);
			try {
				String line = queue.take();
				if (line.startsWith("E_OK"))
					l.setLoginVisible(false);
				else if (line.equals("E_USERNAME_TAKEN"))
					l.setLoginError("Username already taken. ");
				
			} catch (Exception e) { }
			l.setButtonEnabled(true);
		});
		
		l.onChatButton(() -> {
			
			if ("".equals(l.getChatInput().trim()))
				return;
				
			out.println("LOBBYMSG: " + l.getChatInput().trim());
			
			l.setChatInput("");
		}); 
		
	}
	private void initGameCallbacks() {
		c.onLeaveButton(() -> {
			out.println("LEAVE GAME: " + gameId);
		}); 
		
		c.onChatButton(() -> {
			out.println("GAMEMSG: " + c.getChatInput());
			c.setChatInput("");
		}); 
		
		c.setGameInfo(whosOnMove);
	}
	
	public static void main(String[] args) throws IOException {
		Thread thread = Thread.currentThread();
		new Thread(() -> {
			Application.launch(Gui.class, args);
		}).start();
		
		Gui.onClose(() -> {
			thread.interrupt();
			out.println("HELP");
		});
		new Client();
	}
}