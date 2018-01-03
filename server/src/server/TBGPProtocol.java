package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import protocol.AsyncServerProtocol;

public class TBGPProtocol implements AsyncServerProtocol<String> {

	private boolean connectionTerminated;
	private boolean shouldClose;
	private boolean quit = false;

	@Override
	public void processMessage(String msg, ProtocolCallback<String> callback) {

		Database database = Database.getInstance();
		String callBackMess = "";
		String command = "";
		String param = "";

		if (msg.contains(" ")) {
			command = msg.substring(0, msg.indexOf(" "));
			param = msg.substring(msg.indexOf(" ") + 1);
		} else
			command = msg;
		if (command.equals("NICK")) {
			System.out.println("NICK: " + param);
			if (database.containsNick(param)) {
				try {
					callback.sendMessage("SYSMSG NICK REJECTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Player player = new Player(callback, param, null);
				database.insertPlayer(callback, player);
				try {
					callback.sendMessage("SYSMSG NICK ACCEPTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (command.equals("JOIN")) {
			if (database.getPlayer(callback)==null || database.getPlayer(callback).isPlaying() || (database.containsRoom(param) && database.getRoom(param).isActiveGame()))
				try {
					callback.sendMessage("SYSMSG JOIN REJECTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
			else{
				if (database.containsRoom(param)) {
					database.getRoom(param).addPlayer(database.getPlayer(callback));
					database.getPlayer(callback).setRoom(database.getRoom(param));
				}
				else {
					database.insertRoom(param);
					database.getRoom(param).addPlayer(database.getPlayer(callback));
					database.getPlayer(callback).setRoom(database.getRoom(param));
				}
				try {
					callback.sendMessage("SYSMSG JOIN ACCEPTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (command.equals("MSG")) {
			if(database.getPlayer(callback)!=null && database.getPlayer(callback).getRoom()!=null){
				Player player = database.getPlayer(callback);
				Room room = player.getRoom();
				for (Player p : room.getPlayerList()) {
					if (player.getNickname() != p.getNickname()) {
						try {
							p.getCallback().sendMessage("USERMSG " + player.getNickname() + ":" + param);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				try {
					player.getCallback().sendMessage("SYSMSG MSG ACCEPTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				try {
					callback.sendMessage("SYSMSG MSG REJECTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		if (command.equals("LISTGAMES")) {
			for (String game : database.getGamesList())
				callBackMess = callBackMess + " " + game;
			try {
				callback.sendMessage(callBackMess);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (command.equals("STARTGAME")) {
			if (database.getPlayer(callback)!=null && database.containsGame(param) && database.getPlayer(callback).getRoom()!=null && !database.getPlayer(callback).getRoom().isActiveGame()) {
				Player player = database.getPlayer(callback);
				Room room = player.getRoom();
				room.setGame(database.getGame(param).copy(room));
				room.setActiveGame(true);
				for (Player p : room.getPlayerList())
					p.setPlaying(true);
				try {
					callback.sendMessage("STARTGAME ACCEPTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
				room.getGame().startGame();
			} else
				try {
					callback.sendMessage("STARTGAME REJECTED");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		if (command.equals("TXTRESP")) {
			if (!database.getPlayer(callback).getRoom().getGame().textResponse(param.toLowerCase(), database.getPlayer(callback)))
				try {
					callback.sendMessage("SYSMSG TXTRESP REJECTED");
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		if (command.equals("SELECTRESP")) {
			if (!database.getPlayer(callback).getRoom().getGame().selectResponse(param, database.getPlayer(callback)))
				try {
					callback.sendMessage("SYSMSG SELECTRESP REJECTED");
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		if (command.equals("QUIT")) {
			try {
				callback.sendMessage("bye");
				database.removePlayer(callback);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isEnd(String msg) {
		return msg.equals("QUIT");
	}

	public boolean shouldClose() {
		return this.shouldClose;
	}

	public void connectionTerminated() {
		this.connectionTerminated = true;
	}

}
