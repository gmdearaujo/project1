package edu.msu.scrabble.project1;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
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
