package edu.msu.scrabble.project2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class FinalActivity extends Activity {

	private Game game;
	//private String user;
	private String state;
	
	private TextView player1Name;
	private TextView player2Name;
	private TextView player1Score;
	private TextView player2Score;
	
	private TextView winnerMessage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);

		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		//user = (String)intent.getStringExtra("user");
		state = (String)intent.getStringExtra("state"); 
		
		int p1Score = game.getPlayer1Score();
		int p2Score = game.getPlayer2Score();
		
		player1Name = (TextView)findViewById(R.id.textViewPlayer1);
		player2Name = (TextView)findViewById(R.id.textViewPlayer2);
		player1Score = (TextView)findViewById(R.id.textViewPlayer1Score);
		player2Score = (TextView)findViewById(R.id.textViewPlayer2Score);
		
		winnerMessage = (TextView)findViewById(R.id.textViewWinner);
		
		player1Name.setText(game.getPlayer1Name());
		player2Name.setText(game.getPlayer2Name());
		
		player1Score.setText(Integer.toString(p1Score));
		player2Score.setText(Integer.toString(p2Score));
		
		if (p1Score > p2Score) {
			winnerMessage.setText(game.getPlayer1Name() + " Wins!");
		} else {
			winnerMessage.setText(game.getPlayer2Name() + " Wins!");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_final, menu);
		return true;
	}
	
	public void onNewGame(View view) {
		//reset server game
			//if p1, load edit
			//if p2, load wait
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	public void updateServer(){
		if(state.equals("wait")){
	    	new Thread(new Runnable(){
	    		@Override
	    		public void run(){
					Cloud cloud = new Cloud();
			    	cloud.writeUserInfo(game.getGameId(), game.getPlayer1Name(), 0, 
			    			"edit", game.getPlayer2Name(), 0, 
			    			"wait", "", "", "");
	    		}
	    	}).start();
		}
		else{
			new Thread(new Runnable(){
	    		@Override
	    		public void run(){
					Cloud cloud = new Cloud();
			    	cloud.writeUserInfo(game.getGameId(), game.getPlayer1Name(), game.getPlayer1Score(), 
			    			"wait", game.getPlayer2Name(), game.getPlayer2Score(), 
			    			"edit", game.getAnswer(), game.getTip(), game.getCategory());
	    		}
	    	}).start();
		}
    }

}
