package server;

import java.util.*;

public class Room {

	private boolean activeGame;
	private String name;
	private ArrayList<Player> players;
	private Game game;
	private int numOfPlayers;

	public Room(String name) {
		this.name = name;
		this.activeGame = false;
		this.players = new ArrayList<Player>();
		this.numOfPlayers = 0;
	}

	public boolean isActiveGame() {
		return activeGame;
	}

	public void setActiveGame(boolean activeGame) {
		this.activeGame = activeGame;
	}

	public void addPlayer(Player p) {
		players.add(p);
		numOfPlayers++;
	}

	public void removePlayer(Player p) {
		players.remove(p);
		numOfPlayers--;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Player> getPlayerList() {
		return players;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	public void newQuestion() {
		for (Player p : players)
			p.setAnsweredCurrQuestion(false);
	}
}
