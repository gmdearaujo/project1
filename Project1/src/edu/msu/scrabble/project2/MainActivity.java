package edu.msu.scrabble.project2;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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
	static Document gameinfo = null;

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
    	
    	final Intent lobbyIntent = new Intent(this, LobbyActivity.class);
    	final Intent editIntent = new Intent(this, EditActivity.class);
    	final Intent waitIntent = new Intent(this, WaitTurnActivity.class);
    	final Intent guessIntent = new Intent(this, GuessActivity.class);
    	final Intent finalIntent = new Intent(this, FinalActivity.class);
    	
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
		    			}
		    		});
					logSuccess = false;
		    	}else{
		    		view.post(new Runnable(){
		    			@Override
		    			public void run(){
							Toast.makeText(view.getContext(), 
									"Welcome.", 
									Toast.LENGTH_SHORT).show();
		    			}
		    		});
					logSuccess = true;
		    	}
		    	
		    	if(logSuccess == false){
		    		return;
		    	}
		    	
		    	
		    	
		    	else{
			    	Game game = new Game();
			    	game.setPlayer1Name(username.getText().toString());
			    	
			    	// Pick a category
			    	game.randomlySelectCategory();
					
			    	InputStream stream = cloud.readUserInfo(username.getText().toString());
			    	gameinfo = xmlParserFunc(stream);
					
					if(gameinfo != null){
						updateGame(game);
						String activity;
						NamedNodeMap tinker = gameinfo.getElementsByTagName("game").item(0).getAttributes();
						if(tinker.getNamedItem("player1").getNodeValue().equals(username.getText().toString())){
				    		activity = tinker.getNamedItem("p1state").getNodeValue();
				    	}
				    	else{
				    		activity = tinker.getNamedItem("p2state").getNodeValue();
				    	}
						
						if(activity.equals("lobby")){
					    	lobbyIntent.putExtra("GAME", game);
					    	lobbyIntent.putExtra("user", username.getText().toString());
							startActivity(lobbyIntent);
						}
						else if(activity.equals("edit")){
					    	editIntent.putExtra("GAME", game);
					    	editIntent.putExtra("user", username.getText().toString());
							startActivity(editIntent);
						}
						else if(activity.equals("wait")){
					    	waitIntent.putExtra("GAME", game);
					    	waitIntent.putExtra("user", username.getText().toString());
							startActivity(waitIntent);
						}
						else if(activity.equals("guess")){
					    	guessIntent.putExtra("GAME", game);
					    	guessIntent.putExtra("user", username.getText().toString());
							startActivity(guessIntent);
						}
						else if(activity.equals("final")){
					    	finalIntent.putExtra("GAME", game);
					    	finalIntent.putExtra("user", username.getText().toString());
							startActivity(finalIntent);
						}
					}
		    	}
			}
    	}).start();
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
    	
    	NamedNodeMap tinker = gameinfo.getElementsByTagName("game").item(0).getAttributes();
    	
    	if(tinker.getNamedItem("player1").getNodeValue().equals(g.getPlayer1Name())){
    		g.setPlayer1Name(g.getPlayer1Name());
    		g.setPlayer2Name(tinker.getNamedItem("player2").getNodeValue());
    		g.setPlayer1Score(Integer.parseInt(tinker.getNamedItem("p1score").getNodeValue()));
    		g.setPlayer2Score(Integer.parseInt(tinker.getNamedItem("p2score").getNodeValue()));
    	}
    	else{
    		g.setPlayer1Name(g.getPlayer1Name());
    		g.setPlayer2Name(tinker.getNamedItem("player1").getNodeValue());
    		g.setPlayer1Score(Integer.parseInt(tinker.getNamedItem("p2score").getNodeValue()));
    		g.setPlayer2Score(Integer.parseInt(tinker.getNamedItem("p1score").getNodeValue()));
    	}
    	g.setAnswer(tinker.getNamedItem("answer").getNodeValue());
    	g.setTip(tinker.getNamedItem("tip").getNodeValue());
    	g.setCategory(tinker.getNamedItem("category").getNodeValue());
    	g.setGameId(Integer.parseInt(tinker.getNamedItem("gameId").getNodeValue()));
    }
}
