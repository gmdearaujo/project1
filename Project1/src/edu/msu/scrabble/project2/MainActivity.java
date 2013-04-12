package edu.msu.scrabble.project2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	/**
     * The edit text for entering player 2 name
     */
	private EditText editTextPlayer2 = null;
	
	static boolean logSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
         * Get some of the views we'll keep around
         */
        editTextPlayer2 = (EditText)findViewById(R.id.editTextPlayer2);
        
        /*
         * Change the Done button to Start Game
         */
        editTextPlayer2.setImeActionLabel("Start Game", KeyEvent.KEYCODE_ENTER);
        
        editTextPlayer2.setOnKeyListener(new TextView.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	onStartGame(v);
                	return true;
                }
                return false;
            }
        });
        
	}

	/**
     * Handle a Start Game button
     * @param view
     */
    public void onStartGame(final View view) {
    	// gather username
    	final EditText username = (EditText)findViewById(R.id.editTextPlayer1);
    	final EditText password = (EditText)findViewById(R.id.editTextPlayer2);
 
    	new Thread(new Runnable(){
    		
    		@Override
    		public void run(){
    			Cloud cloud = new Cloud();
    	    	boolean valid = cloud.loginToGame(username.getText().toString(), password.getText().toString());
    	    	
    	    	if(valid == false){
    	    		view.post(new Runnable(){
    	    			@Override
    	    			public void run(){
    	    				Toast.makeText(view.getContext(), 
    	    						"Login failed.", 
    	    						Toast.LENGTH_SHORT).show();
    	    				logSuccess = false;
    	    			}
    	    			
    	    		});
    	    	}else{
    	    		view.post(new Runnable(){
    	    			@Override
    	    			public void run(){
    	    				Toast.makeText(view.getContext(), 
    	    						"Welcome.", 
    	    						Toast.LENGTH_SHORT).show();
    	    				logSuccess = true;
    	    			}
    	    			
    	    		});
    	    	}
    		}
    	}).start();
    	
    	if(logSuccess == false){
    		return;
    	}
    	
    	//check game status
    	//check which activity currently in
    	//launch particular activity
    	
		/*request new opponent
		
		Game game = new Game();
    	game.setPlayer1Name(username.getText().toString());
    	//game.setPlayer2Name(________________);
    	
    	// Pick a category
    	game.randomlySelectCategory();
    	
    	Intent intent = new Intent(this, LobbyActivity.class);
    	intent.putExtra("GAME", game);
		startActivity(intent);*/
	}

    public void onNewUser(View view) {
    	Intent intent = new Intent(this, NewUserActivity.class);
    	startActivity(intent);
    }
    
    /**
     * Handle a How To Play button
     * @param view
     */
    public void onHowToPlay(View view) {
    	// The drawing is done
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        
        // Parameterize the builder
        builder.setTitle(R.string.how_to_play);
        
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_howto, null))
        // Add action buttons
           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   
               }
           }); 
        
        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
