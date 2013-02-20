package edu.msu.scrabble.project1;


import java.util.Timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GuessActivity extends Activity {
	
	/**
	 * The game
	 */
	private Game game;
	
	/**
	 * Time left
	 */
	private TextView timeText;
	
	/**
	 * Player Names
	 */
	private TextView p1Name;
	private TextView p2Name;
	
	/**
	 * Player Scores
	 */
	private TextView p1Score;
	private TextView p2Score;
	
	/**
	 * Category
	 */
	private TextView categoryText;	
	
	/**
	 * Tip
	 */
	private TextView tipText;
	
	/**
	 * Answer
	 */
	private EditText answerText;
	
	/**
	 * Answer
	 */
	private DrawingView drawingView;
	
	private int remainingTime;
	
	private GameTimer gtimer;
	
	/**
	 * Timer
	 */
	public class GameTimer extends CountDownTimer {
		public GameTimer (long time) {
			super(time, 1);
		}
		
		@Override
		public void onFinish() {
			
		}
		
		@Override
		public void onTick(long timeLeft) {
			long timeSeconds = timeLeft / 1000;
			remainingTime = (int)timeSeconds;
			timeText.setText(Long.toString(timeSeconds));
			
			if (timeSeconds < 70)
				tipText.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);
		
		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		drawingView = (DrawingView)findViewById(R.id.guessingView);
		drawingView.setEditable(false);
		
		p1Name = (TextView)findViewById(R.id.textViewP1);
		p2Name = (TextView)findViewById(R.id.textViewP2);
		timeText = (TextView)findViewById(R.id.textViewTimeLeft);
		categoryText = (TextView)findViewById(R.id.textViewCategoryName);
		tipText = (TextView)findViewById(R.id.textViewTipText);
		answerText = (EditText)findViewById(R.id.editTextAnswer);
		p1Score = (TextView)findViewById(R.id.textViewScoreP1);
		p2Score = (TextView)findViewById(R.id.textViewScoreP2);
		
		// Set text boxes
		p1Name.setText(game.getPlayer1Name() + ":");
		p2Name.setText(game.getPlayer2Name() + ":");
		categoryText.setText(game.getCategory());
		tipText.setText(game.getTip());
		tipText.setVisibility(View.INVISIBLE);
		p1Score.setText(Integer.toString(game.getPlayer1Score()));
		p2Score.setText(Integer.toString(game.getPlayer2Score()));
		
		gtimer = new GameTimer(130*1000);
		gtimer.start();
	}
	
	/**
     * Handle a Done button press
     * @param view
     */
	public void onDone(View view)
	{
		if (game.guessAnswer(answerText.getText().toString())) {
			
			game.incrementPlayerScore(game.getGuessingPlayer(), remainingTime);

			game.swapPlayers();
			
			// Freeze timer
			int finalTime = remainingTime;
			timeText.setText(Integer.toString(finalTime));
			
			if (game.checkForWinner()) {
	        	Intent intent = new Intent(this, FinalActivity.class);
	        	intent.putExtra("GAME", game);
	    		startActivity(intent);
			} else {
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						view.getContext());
	 
				// set title
				alertDialogBuilder.setTitle("Correct!");

		        alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   onOk();
		               }
		        });
		        
		        // Create the dialog box and show it
		        AlertDialog alertDialog = alertDialogBuilder.create();
		        alertDialog.show();
			}
		}
	}
	
	public void onOk() {
    	Intent intent = new Intent(this, EditActivity.class);
    	intent.putExtra("GAME", game);
		startActivity(intent);
	}

    @Override
    public void onPause() {
    	super.onPause();
    	finish();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(drawingView.getContext());
 
			// set title
			alertDialogBuilder.setTitle(R.string.quit);

	        alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   finish();
	               }
	        })
	        
           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   
               }
           });
	        
	        // Create the dialog box and show it
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
		}

        return true;
    }

}
