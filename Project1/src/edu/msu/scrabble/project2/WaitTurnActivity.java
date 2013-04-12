package edu.msu.scrabble.project2;

import java.util.Timer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class WaitTurnActivity extends Activity {

	private Game game;
	private java.util.Timer timer;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_wait_turn);
		
		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		
		timer = new Timer();
	    timer.schedule(new waitLoop(), 0, 1000*15);
	}

	private class waitLoop extends java.util.TimerTask{
	    public void run() //this becomes the loop
	    {
	    	//update my game
	    	//if gamestate changed to guessing
				//update game
				//launch guessing activity
			//if gamestate changed to final
				//update game and go to final
	    }
	}
}
