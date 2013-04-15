package edu.msu.scrabble.project2;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
	static Document gameinfo;

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
    	else{
	    	Game game = new Game();
	    	game.setPlayer1Name(username.getText().toString());
	    	//game.setPlayer2Name(________________);
	    	
	    	// Pick a category
	    	game.randomlySelectCategory();
			
			new Thread(new Runnable(){
	    		@Override
	    		public void run(){
					Cloud cloud = new Cloud();
			    	InputStream stream = cloud.readUserInfo(username.getText().toString());
			    	gameinfo = xmlParserFunc(stream);
	    		}
	    	}).start();
			
			if(gameinfo != null){
				updateGame(game);
				String activity;
				if(gameinfo.getElementsByTagName("Player1").item(0).toString().equals(game.getPlayer1Name())){
		    		activity = gameinfo.getElementsByTagName("P1State").item(0).toString();
		    	}
		    	else{
		    		activity = gameinfo.getElementsByTagName("P2State").item(0).toString();
		    	}
				if(activity.equals("lobby")){
					Intent intent = new Intent(this, LobbyActivity.class);
			    	intent.putExtra("GAME", game);
					startActivity(intent);
				}
				else if(activity.equals("edit")){
					Intent intent = new Intent(this, EditActivity.class);
			    	intent.putExtra("GAME", game);
					startActivity(intent);
				}
				else if(activity.equals("wait")){
					Intent intent = new Intent(this, WaitTurnActivity.class);
			    	intent.putExtra("GAME", game);
					startActivity(intent);
				}
				else if(activity.equals("guess")){
					Intent intent = new Intent(this, GuessActivity.class);
			    	intent.putExtra("GAME", game);
					startActivity(intent);
				}
				else if(activity.equals("final")){
					Intent intent = new Intent(this, FinalActivity.class);
			    	intent.putExtra("GAME", game);
					startActivity(intent);
				}
			}
    	}
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
    
    public static Document xmlParserFunc(InputStream s){
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder parser;
    	Document dc;
		try{
			parser = factory.newDocumentBuilder();
		}
		catch(ParserConfigurationException pce){
    		return null;
    	}
		try{
			dc = parser.parse(s);
		}
    	catch(IOException ioe){
    		return null;
    	}
		catch(SAXException saxe){
    		return null;
    	}
		
		return dc;
    }
    
    public static void updateGame(Game g){
    	if(gameinfo.getElementsByTagName("Player1").item(0).toString().equals(g.getPlayer1Name())){
    		g.setPlayer2Name(gameinfo.getElementsByTagName("Player2").item(0).toString());
    	}
    	else{
    		g.setPlayer2Name(gameinfo.getElementsByTagName("Player1").item(0).toString());
    	}
    	g.setAnswer(gameinfo.getElementsByTagName("Answer").item(0).toString());
    	g.setTip(gameinfo.getElementsByTagName("Tip").item(0).toString());
    	g.setCategory(gameinfo.getElementsByTagName("Category").item(0).toString());
    }
}
