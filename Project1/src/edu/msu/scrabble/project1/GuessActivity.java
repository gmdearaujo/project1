package edu.msu.scrabble.project1;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

public class GuessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);
	}
	
	/**
     * Handle a Done button press
     * @param view
     */
	public void onDone(View view)
	{
		
		
		if(isWinner()) {
			// We have a winner
	        // Instantiate a dialog box builder
	        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
	        
	        // Parameterize the builder
	        builder.setTitle(R.string.text_winner);
	        builder.setMessage(R.string.text_score);
	        
	        // Add action buttons
	        builder
	           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   onOk();
	               }
	           })
	           .setNegativeButton(R.string.text_restart, new DialogInterface.OnClickListener() {
	        	   @Override
	        	   public void onClick(DialogInterface dialog, int id) {
	        		   onRestart();
	               }
	           });
	        
	        // Create the dialog box and show it
	        AlertDialog alertDialog = builder.create();
	        alertDialog.show();
		}
		else {
			Intent intent = new Intent(this, GuessActivity.class);
			startActivity(intent);
		}
		
	}
	
	/**
     * Determine if the player is winner!
     * @return true if player is winner
     */
    public boolean isWinner() {
    	return true;
        
    }
    
    public void onOk() {
    	Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
    
    public void onRestart() {
    	Intent intent = new Intent(this, EditActivity.class);
		startActivity(intent);
	}
	
	
}
