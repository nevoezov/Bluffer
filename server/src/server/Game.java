package server;

public interface Game {

	String getName();

	Game copy(Room room);

	boolean textResponse(String msg, Player p);

	boolean selectResponse(String msg, Player p);

	void startGame();

}
