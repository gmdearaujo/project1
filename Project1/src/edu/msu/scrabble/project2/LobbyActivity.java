package edu.msu.scrabble.project2;

import android.os.Bundle;
import android.app.Activity;

public class LobbyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
	}

	//create function that constantly pulls from server
		//if gamestate changed to drawing
			//create game file and launch drawing activity
		//if gamestate changed to waiting
			//create opGame file and launch wait activity
}
