package server;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionAndAnswer {
	private String question;
	private ArrayList<PlayerAnswer> answers;
	private String realAnswer;

	public QuestionAndAnswer(String question, String realAnswer) {
		this.question = question;
		this.answers = new ArrayList<PlayerAnswer>();
		this.realAnswer = realAnswer;
		answers.add(new PlayerAnswer(null, realAnswer));
	}

	public String getQuestion() {
		return question;
	}

	public void addAnswer(Player player, String answer) {
		answers.add(new PlayerAnswer(player, answer));
	}

	public ArrayList<PlayerAnswer> getAnswers() {
		return answers;
	}

	public ArrayList<PlayerAnswer> getShuffledAnswers() {
		ArrayList<PlayerAnswer> temp = new ArrayList<PlayerAnswer>(answers);
		Collections.shuffle(temp);
		return temp;
	}

	public String getRealAnswer() {
		return this.realAnswer;
	}
}
