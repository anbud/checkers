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
import javafx.scene.Node;
import javafx.scene.media.AudioClip;

public class Client {
	
	private boolean first;
	
	private Gui gui;
	private static PrintWriter out;
	private BufferedReader in;
	private GameController c;
	private LobbyController l;
	
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
	
	private String username;
	private String whosOnMove;

	private AudioClip aYourTurn, aLobbyMsg, aGameMsg;
	
	public Client() {
		first = true;
		gui = Gui.getInstance();
		l = gui.loadLobbyView(true);
		
		aYourTurn = new AudioClip(Gui.class.getResource("view/sound/your-turn.mp3").toExternalForm());
        aGameMsg = new AudioClip(Gui.class.getResource("view/sound/message.mp3").toExternalForm());
        aLobbyMsg = new AudioClip(Gui.class.getResource("view/sound/message.mp3").toExternalForm());
        aYourTurn.setVolume(0.5);
        aGameMsg.setVolume(0.5);
        aLobbyMsg.setVolume(0.5);
		
		initialize();
	}
	
	private void initialize() {
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
				gui.showMessage("Server failure", "Andrej ugasio server (sad face)");
			}
			
		} catch (Exception e) {
			gui.showMessage("Server error", "Server's not up. \nTry from localhost.", true);
		}
	}

	private void process(String line, BufferedReader in) {
		try {
			if (line.startsWith("PING")) 
				out.println("PONG");
			
			else if (line.startsWith("E_LAG")) { }
			else if (line.startsWith("E_OK"))
				queue.add(line);
			
			else if (line.startsWith("E_MOVE")) {
				String[] moves = line.substring(line.indexOf(":") + 2).split(" ");
				
				c.getBoard().move(c.getBoard().getField(Integer.parseInt(moves[0]), Integer.parseInt(moves[1])));
			}
			else if (line.startsWith("E_EAT")) {
				String[] moves = line.substring(line.indexOf(":") + 2).split(" ");
				
				c.getBoard().captureOne(Integer.parseInt(moves[0]), Integer.parseInt(moves[1]));
			}
			else if (line.equals("E_USERS:")) {
				LinkedList<String> users = new LinkedList<>();
				while (!(line = in.readLine()).equals("E_END")) 
					if (!line.equals(username))
						users.add(line);
				
				l.setPlayers(users);
			}
			else if (line.startsWith("E_GAMES")) {
				LinkedList<String> games = new LinkedList<>();
				while (!(line = in.readLine()).equals("E_END")) 
					games.add(line);
				
				l.setGames(games);
			}
			else if (line.startsWith("E_LOBBY_MESSAGE")) {
				String msg = line.substring(line.indexOf(":") + 2);
				String userSent = msg.substring(0, msg.indexOf(":"));
				l.addChatMessage(userSent, msg.substring(msg.indexOf(":") + 2), userSent.equals(username));
                                if (c == null)
                                    aLobbyMsg.play();
			}
			else if (line.startsWith("E_LOBBY_INFO")) {
				String msg = line.substring(line.indexOf(":") + 2);
				l.addChatInfo(msg.substring(msg.indexOf(":") + 2));
			} 
			else if (line.startsWith("E_MESSAGE")) {
				String msg = line.substring(line.indexOf(":") + 2);
				String userSent = msg.substring(0, msg.indexOf(":"));
				c.addChatMessage(userSent, msg.substring(msg.indexOf(":") + 2), userSent.equals(username));
                                aGameMsg.play();
			} 
			else if (line.startsWith("E_INFO")) {
				String msg = line.substring(line.indexOf(":") + 2);
				c.addChatInfo(msg.substring(msg.indexOf(":") + 2));
			}
			else if (line.startsWith("E_REQUESTS")) {
				LinkedList<String> requests = new LinkedList<>();
				while (!(line = in.readLine()).equals("E_END")) 
					requests.add(line);
				
				l.setRequests(requests);
			}
			else if (line.startsWith("E_GAME_STARTED")) {
				c = gui.loadGameView();
				initGameCallbacks();
			}
			else if (line.startsWith("E_GAME_DECLINED"))
				l.addChatInfo(line.substring(line.indexOf(":") + 2) + " rejected challenge.");				
			
			else if (line.equals("E_GAME_OVER"))
				gui.showMessage("Game over", "");
				
			else if (line.startsWith("E_TURN")) {
				whosOnMove = line.substring(line.indexOf(":") + 2);
				if (first) {
					c.getBoard().whoAmI(whosOnMove.equals(username));
					if (!whosOnMove.equals(username)) {
						c.getBoard().getParent().setRotate(180);
						for(Node f : c.getBoard().getChildren()) {
							f.setRotate(180);
						}
					}
					first = false;
				}
				c.setGameInfo(whosOnMove);
				if (!whosOnMove.equals(username))
					c.setBlockBoard(true);
				else {
					c.setBlockBoard(false);
					aYourTurn.play();
				}
			}
			else if (line.equals("E_WON")) 
				gui.showMessage("We have a winner", "User " + line.substring(line.indexOf(":") + 2) + " won!");
			
			else if (line.equals("E_DRAW"))
				gui.showMessage("It's a draw", "No body wins.");
			
			else if (line.startsWith("E_GAME_ACCEPTED")) { }
			else if (line.startsWith("E_GAME_REQUEST")) { }
			else if (line.startsWith("E_MULTIPLE_REQUESTS")) {
				queue.add(line);
			}
			else {
				queue.add(line);
				System.out.println(line);
			}
		} catch (IOException e) { }
	}
	
	private void initLobbyCallbacks() {
        
        l.onMute(() -> {
            if (!l.isMuted()) {
                l.setMuted(true);
                aLobbyMsg.setVolume(0);
            } else {
                l.setMuted(false);
                aLobbyMsg.setVolume(0.5);
            }
        });
            
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
			try { queue.take(); } catch (Exception e) {}
		}); 
		
		l.onAcceptButton((uname) -> {
			out.println("GAME ACCEPT: " + uname);
			
			try { queue.take(); } catch (Exception e) {}
		});
		
		l.onRejectButton((uname) -> {
			out.println("GAME DECLINE: " + uname);
		});
		
		l.onRequestButton((uname) -> {
			out.println("GAME REQUEST: " + uname);
			l.addChatInfo("You've challenged " + uname);
			try { queue.take(); } catch (Exception e) {}
		});
		
	}
	private void initGameCallbacks() {
		first = true;
        
        c.onMute(() -> {
            if (!c.isMuted()) {
                c.setMuted(true);
                aGameMsg.setVolume(0);
            } else {
                c.setMuted(false);
                aGameMsg.setVolume(0.5);
            }
        });
                
		c.onLeaveButton(() -> {
			out.println("LEAVE GAME");
			try { queue.take(); } catch (Exception e) {}
			gui.loadLobbyView();
			c = null;
		}); 
		
		c.onChatButton(() -> {
			if ("".equals(c.getChatInput().trim()))
				return;
			
			out.println("GAMEMSG: " + c.getChatInput());
			
			c.setChatInput("");
			try { queue.take(); } catch (Exception e) {}
		});
		
		c.setGameInfo(whosOnMove);
		
		c.getBoard().onMove((src) -> {
			out.println(String.format("MOVE: %d %d", src.getXX(), src.getYY()));
		});
		
		c.getBoard().onTurn(() -> {
			out.println("END_TURN");
		});
		
		c.getBoard().onWin(() -> {
			out.println("WON");
		});
		
		c.getBoard().onDraw(() -> {
			out.println("DRAW");
		});
	}
	
	public static void main(String[] args) throws IOException {
		Thread thread = Thread.currentThread();
		new Thread(() -> {
			Application.launch(Gui.class, args);			
		}).start();
		
		Gui.onClose(() -> {
			thread.interrupt();
			try {
				out.println("PONG");
			} catch (Exception e) { }
		});
                
		new Client();
	}
}