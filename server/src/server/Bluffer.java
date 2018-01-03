package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import com.google.gson.*;
import com.google.*;
import com.google.gson.stream.JsonReader;


public class Bluffer implements Game {

	private final int QUESTION_LIMIT=3;
	private LinkedList<QuestionAndAnswer> QA = new LinkedList<QuestionAndAnswer>();
	private Room room;
	private int numberOfQuestions;
	private int numberOfAnswers;
	private int numberOfResponse;
	private QuestionAndAnswer curr;
	private ArrayList<PlayerAnswer> shuffledAnswers;

	public Bluffer(){
	}
	public Bluffer(Bluffer b, Room room){
		this.room=room;
		this.numberOfAnswers = 0;
		this.numberOfResponse = 0;
		this.numberOfQuestions = 0;
	}
	public String getName() {
		return "Bluffer";
	}

	@Override
	public boolean selectResponse(String msg, Player p) {
		int selection;
		try{
			selection = Integer.parseInt(msg);
		}
		catch(NumberFormatException e){
			return false;
		}
		if(selection>room.getNumOfPlayers())
			return false;
		if(!p.respondedToCurrQuestion()){
			numberOfResponse++;
			p.setRespondedToCurrQuestion(true);
			try {				
				p.getCallback().sendMessage("SYSMSG SELECTRESP ACCEPTED");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(shuffledAnswers.get(Integer.parseInt(msg)).getPlayer() == null){
				p.setRoundScore(10);
				p.setGoodAnswer(true);
			}
			else{
				shuffledAnswers.get(Integer.parseInt(msg)).getPlayer().setRoundScore(5);
				p.setGoodAnswer(false);
			}
			if(numberOfResponse==room.getNumOfPlayers()){
				publishAnswer();
				numberOfResponse = 0;
				resetPlayers();
				newQuestion();
			}
			return true;
		}
		else
			return false;
	}

	private void resetPlayers() {
		for(Player p:room.getPlayerList()){
			p.setTotalScore();
			p.resetRoundScore();
			p.setAnsweredCurrQuestion(false);
			p.setRespondedToCurrQuestion(false);
		}

	}

	public boolean textResponse(String msg, Player p) {
		if(!p.isAnsweredCurrQuestion()){
			numberOfAnswers++;
			curr.addAnswer(p,msg);
			p.setAnsweredCurrQuestion(true);
			try {
				p.getCallback().sendMessage("SYSMSG TXTRESP ACCEPTED");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(numberOfAnswers==room.getNumOfPlayers()){
				shuffledAnswers = curr.getShuffledAnswers();
				sendChoiceOptions();
				numberOfAnswers = 0;
			}
			return true;
		}
		else
			return false;


	}
	public Bluffer copy(Room room){
		return new Bluffer(this, room);
	}

	public Room getRoom(){
		return room;
	}

	public void startGame() {

		JsonReader jreader = null;
		while (jreader == null){
			try {
				jreader = new JsonReader(new FileReader("/users/studs/bsc/2016/ezovn/workspace2/assignment3Server/bluffer.json"));
			} catch (FileNotFoundException e) {

			}
		}

		JsonParser jparser = new JsonParser();
		JsonElement element = jparser.parse(jreader);
		if (element.isJsonObject()){
			JsonObject jobject = element.getAsJsonObject();
			JsonArray questions = jobject.get("questions").getAsJsonArray();
			for (JsonElement question : questions){ 
				String q = (question.getAsJsonObject().get("questionText").getAsString());
				String a = (question.getAsJsonObject().get("realAnswer").getAsString());
				QA.add(new QuestionAndAnswer(q, a));
			}
		}
		Collections.shuffle(QA);
		newQuestion();
	}

	private void publishAnswer(){
		for(Player p: room.getPlayerList())
			try {
				p.getCallback().sendMessage("GAMEMSG The correct answer is: " + curr.getRealAnswer());
				if(p.getGoodAnswer())
					p.getCallback().sendMessage("GAMEMSG correct!+" + p.getRoundScore() + "pts" );
				else
					p.getCallback().sendMessage("GAMEMSG wrong!+" + p.getRoundScore() + "pts" );
			} catch (IOException e) {
				e.printStackTrace();
			}
	}


	private void sendChoiceOptions(){
		String msg = "";
		int i = 0;
		for(PlayerAnswer ans:shuffledAnswers){
			System.out.println(ans.getAnswer() + " ");
		}
		for(PlayerAnswer ans:shuffledAnswers){
			msg =msg+i+"."+ans.getAnswer()+" ";
			i++;
		}
		for(Player p: room.getPlayerList()){
			try {
				p.getCallback().sendMessage("ASKCHOICES " + msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void newQuestion(){
		if(numberOfQuestions<QUESTION_LIMIT){
			this.curr = QA.pop();
			numberOfQuestions++;
			for(Player p: room.getPlayerList()){
				try {
					p.getCallback().sendMessage("ASKTXT " + curr.getQuestion());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else
			endGame();
	}
	private void endGame() {
		String msg = "";
		for(Player p: room.getPlayerList()){
			msg = msg + p.getNickname() + ": " + p.getTotalScore() + "pts ";
		}
		for(Player p: room.getPlayerList()){
			try {
				p.getCallback().sendMessage("GAMEMSG Summary: " + msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.setPlaying(false);
			p.clearScore();
		}
		room.setActiveGame(false);
	}
}
