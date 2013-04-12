package edu.msu.scrabble.project2;

import java.util.Timer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class LobbyActivity extends Activity {
	
	private Game game;
	private java.util.Timer timer;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_lobby);
		
		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		
		timer = new Timer();
	    timer.schedule(new waitLoop(), 0, 1000*15);
	}

	private class waitLoop extends java.util.TimerTask{
	    public void run() //this becomes the loop
	    {
	    	//update my game
			//if gamestate changed to drawing
				//create game file and launch drawing activity
			//if gamestate changed to waiting
				//create opGame file and launch wait activity
	    }
	}
}
