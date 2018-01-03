package server;

public class Player {

	private ProtocolCallback<String> callback;
	private String nickname;
	private Room room;
	private boolean isPlaying;
	private boolean answeredCurrQuestion;
	private boolean respondedToCurrQuestion;
	private int totalScore, roundScore;
	private boolean goodAnswer;

	public Player(ProtocolCallback<String> callback, String nickname, Room room) {
		this.nickname = nickname;
		this.callback = callback;
		this.setRoom(room);
		this.setPlaying(false);
		this.setAnsweredCurrQuestion(false);
		this.totalScore = 0;
		this.roundScore = 0;
		this.goodAnswer = false;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getNickname() {
		return nickname;
	}

	public ProtocolCallback<String> getCallback() {
		return callback;
	}

	public boolean isAnsweredCurrQuestion() {
		return answeredCurrQuestion;
	}

	public void setAnsweredCurrQuestion(boolean answeredCurrQuestion) {
		this.answeredCurrQuestion = answeredCurrQuestion;
	}

	public void setRespondedToCurrQuestion(boolean respondedToCurrQuestion) {
		this.respondedToCurrQuestion = respondedToCurrQuestion;
	}

	public boolean respondedToCurrQuestion() {
		return respondedToCurrQuestion;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public void setRoundScore(int score) {
		this.roundScore += score;
	}

	public void setTotalScore() {
		this.totalScore += roundScore;
	}

	public boolean getGoodAnswer() {
		return goodAnswer;
	}

	public void setGoodAnswer(boolean wasCorrect) {
		this.goodAnswer = wasCorrect;
	}

	public void resetRoundScore() {
		this.roundScore = 0;

	}
	public void clearScore(){
		this.totalScore = 0;
	}
}
