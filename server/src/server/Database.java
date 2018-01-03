package server;

import java.util.*;
import java.util.Map.Entry;

public class Database {

	private HashMap<ProtocolCallback<String>, Player> players;
	private ArrayList<String> gamesList;
	private ArrayList<String> playerNames;
	private HashMap<String, Game> games;
	private HashMap<String, Room> rooms;
	private Database instance;

	private static class SingletonHolder {
		private static Database instance = new Database();
	}

	private Database() {
		this.players = new HashMap<ProtocolCallback<String>, Player>();
		this.gamesList = new ArrayList();
		gamesList.add("BLUFFER");
		this.games = new HashMap<String, Game>();
		games.put("BLUFFER", new Bluffer());
		this.rooms = new HashMap<String, Room>();
		this.playerNames = new ArrayList<String>();
	}

	public static Database getInstance() {
		return SingletonHolder.instance;
	}

	public boolean containsNick(String name) {
		return playerNames.contains(name);
	}

	public void insertPlayer(ProtocolCallback callback, Player player) {
		players.put(callback, player);
		if (!playerNames.contains(player.getNickname()))
			playerNames.add(player.getNickname());
	}

	public void insertRoom(String room) {
		rooms.put(room, new Room(room));
	}

	public boolean containsRoom(String room) {
		return rooms.containsKey(room);
	}

	public Player getPlayer(ProtocolCallback<String> callback) {
		if (players.containsKey(callback))
			return players.get(callback);
		return null;
	}

	public Room getRoom(String room) {
		if (rooms.containsKey(room))
			return rooms.get(room);
		return null;
	}

	public ArrayList<String> getGamesList() {
		return gamesList;
	}

	public boolean containsGame(String game) {
		return gamesList.contains(game);
	}

	public Game getGame(String game) {
		return games.get(game);
	}

	public void removePlayer(ProtocolCallback<String> callback) {
		Player p = players.get(callback);
		Room r = p.getRoom();
		playerNames.remove(p.getNickname());
		if (p.getRoom() != null)
			r.removePlayer(p);
		players.remove(callback);
	}

}
