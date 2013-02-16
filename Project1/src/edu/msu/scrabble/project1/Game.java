package edu.msu.scrabble.project1;

import java.util.Random;

public class Game {
	
	private String player1Name = null;
	
	private String player2Name = null;
	
	private int player1Score = 0;
	
	private int player2Score = 0;
	
	private String answer = null;
	
	private int category;
	
	private Random randomNumberGenerator = new Random();
	
	public static final int CATEGORY_ANIMAL = 0;
	public static final int CATEGORY_BUILDING = 1;
	public static final int CATEGORY_OBJECT = 2;
	public static final int CATEGORY_ACTION = 3;
	public static final int CATEGORY_MSU = 4;
	

	public Game() {
		// TODO Auto-generated constructor stub
	}
	
	// randomly sets the category to one of the 5 available
	public void randomlySelectCategory() {
		category = randomNumberGenerator.nextInt() % 5;
	}
	
	// returns true if the guess matches the answer
	public Boolean guessAnswer(String guess) {
		// change this so that it trims whitespace, etc. from the
		// ends and is case insensitive
		if (guess == answer)
			return true;
		
		return false;
	}
	
	// increments the chosen player's score
	// returns false if the player number is not valid
	public Boolean incrementPlayerScore(int playerNumber) {
		if (playerNumber == 1)
		{
			player1Score++;
			return true;
		}
		else if (playerNumber == 2)
		{
			player2Score++;
			return true;
		}
		else
			return false;
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(String player2Name) {
		this.player2Name = player2Name;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getPlayer1Score() {
		return player1Score;
	}

	public int getPlayer2Score() {
		return player2Score;
	}

	public int getCategory() {
		return category;
	}
	
}
