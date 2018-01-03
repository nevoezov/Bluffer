package server;

public class PlayerAnswer {

	private Player player;
	private String answer;
	
	public PlayerAnswer(Player player, String answer){
		this.player = player;
		this.answer = answer;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getAnswer(){
		return answer;
	}
	
}
