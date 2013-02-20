package edu.msu.scrabble.project1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

    public void onStartGame(View view) {
    	Game game = new Game();
    	
    	// Get player names, set in game
    	EditText p1Text = (EditText)findViewById(R.id.editTextPlayer1);
    	EditText p2Text = (EditText)findViewById(R.id.editTextPlayer2);
    	
		game.setPlayer1Name(p1Text.getText().toString());
		game.setPlayer2Name(p2Text.getText().toString());
    	
    	// Pick a category
    	game.randomlySelectCategory();
    	
    	Intent intent = new Intent(this, EditActivity.class);
    	intent.putExtra("GAME", game);
		startActivity(intent);
	}

}
