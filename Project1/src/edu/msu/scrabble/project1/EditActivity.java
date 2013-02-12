package edu.msu.scrabble.project1;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class EditActivity extends Activity {
	
	/**
     * The color select button
     */
    private Button colorButton = null;
    
    /**
     * Request code when selecting a color
     */
    private static final int SELECT_COLOR = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		/*
         * Get some of the views we'll keep around
         */
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
        if(requestCode == SELECT_COLOR && resultCode == Activity.RESULT_OK) {
            // This is a color response
            int color = data.getIntExtra(ColorSelectActivity.COLOR, Color.BLACK);
        }
	}

	/**
     * Handle a Color button press
     * @param view
     */
    public void onColor(View view) {
        // Get a color
        Intent intent = new Intent(this, ColorSelectActivity.class);
        startActivityForResult(intent, SELECT_COLOR);      
    }
    
    /**
     * Handle a Done button press
     * @param view
     */
    public void onDone(View view) {
        
    	// The drawing is done
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        
        // Parameterize the builder
        builder.setTitle(R.string.text_done);
        
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_done, null))
        // Add action buttons
           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   
               }
           })
           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   
               }
           }); 
        
        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    	
    }
    
    public void onDialogOk(View view) {
    	Intent intent = new Intent(this, GuessActivity.class);
		startActivity(intent);
	}

}