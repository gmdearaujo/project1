package edu.msu.scrabble.project1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Game implements Serializable {
	
	private final int maxScore = 500;
	
	private int editingPlayer = 0;
	private int guessingPlayer = 1;
	
	private String player1Name = "";
	
	private String player2Name = "";
	
	private int player1Score = 0;
	
	private int player2Score = 0;
	
	private String answer = "";
	
	private String tip = "";
	
	private String category = "";
	
	private Random randomNumberGenerator = new Random();
	
	private ArrayList<String> categories = new ArrayList<String>();
	
	public Game() {
		categories.add("Animal");
		categories.add("Building");
		categories.add("Object");
		categories.add("Action");
		categories.add("MSU");
	}
	
	// randomly sets the category to one of the 5 available
	public void randomlySelectCategory() {
		category = categories.get(randomNumberGenerator.nextInt(5) % 5);
	}
	
	// returns true if the guess matches the answer
	public Boolean guessAnswer(String guess) {
		// change this so that it trims whitespace, etc. from the
		// ends and is case insensitive
		if (guess.equalsIgnoreCase(answer))
			return true;
		
		return false;
	}
	
	// increments the chosen player's score
	// returns false if the player number is not valid
	public Boolean incrementPlayerScore(int playerNumber, int amount) {
		if (playerNumber == 1)
		{
			player1Score = player1Score + amount;
			return true;
		}
		else if (playerNumber == 2)
		{
			player2Score = player2Score + amount;
			return true;
		}
		else
			return false;
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(String player1Name) {
		if (!player1Name.equals(null) && !player1Name.equals("")) {
			this.player1Name = player1Name;
		} else {
			this.player1Name = "Player 1";
		}
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(String player2Name) {
		if (!player2Name.equals(null) && !player2Name.equals("")) {
			this.player2Name = player2Name;
		} else {
			this.player2Name = "Player 2";
		}
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		if (!answer.equals(null) && !answer.equals("")) {
			this.answer = answer;
		}
	}

	public int getPlayer1Score() {
		return player1Score;
	}

	public int getPlayer2Score() {
		return player2Score;
	}

	public String getCategory() {
		return category;
	}
	
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		if (!tip.equals(null) && !tip.equals("")) {
			this.tip = tip;
		}
	}
	
	public boolean checkAnswerAndTip() {
		if (tip.equals("") || answer.equals("")) {
			return false;
		}
		return true;
	}
	
	public int getEditingPlayer() {
		return editingPlayer;
	}
	
	public int getGuessingPlayer() {
		return guessingPlayer;
	}

	public void swapPlayers() {
		int temp = guessingPlayer;
		guessingPlayer = editingPlayer;
		editingPlayer = temp;
	}
	
	public boolean checkForWinner() {
		if (player1Score >= maxScore || player2Score >= maxScore) {
			return true;
		}
		return false;
	}
}
