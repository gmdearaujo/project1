package edu.msu.scrabble.project2;

import android.os.Bundle;
import android.app.Activity;

public class WaitTurnActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wait_turn);
	}
	
	//create function that constantly pulls from server
			//if gamestate changed to guessing
				//update your game
				//launch guessing activity
			//if gamestate changed to final
				//update game and go to final

}
