package edu.msu.scrabble.project2;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Base64;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GuessActivity extends Activity {
	
	/**
     * Tag used for saving bundle
     */
	private static final String PICTURE = "PICTURE";
	/**
     * Tag used for saving bundle
     */
	private static final String GAME = "GAME";
	/**
     * Tag used for saving bundle
     */
	private static final String TIME = "TIME";
	/**
	 * The game
	 */
	private Game game;
	private String user;
	
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
	
	private int remainingTime = 130;
	
	private GameTimer gtimer;
	
	/**
	 * Countdown timer, counts from time to 0
	 */
	public class GameTimer extends CountDownTimer {
		public GameTimer (long time) {
			super(time, 1);
		}
		
		@Override
		public void onFinish() {
			onGuessFail();
		}
		
		@Override
		public void onTick(long timeLeft) {
			long timeSeconds = timeLeft / 1000;
			remainingTime = (int)timeSeconds;
			timeText.setText(Long.toString(timeSeconds));
			
			if (timeSeconds <= 60)
				tipText.setText(game.getTip());
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);

		drawingView = (DrawingView)findViewById(R.id.guessingView);
		drawingView.setEditable(false);
		
		Intent intent = getIntent();
		if (intent != null) {
			game = (Game)intent.getSerializableExtra(GAME);
			user = (String)intent.getStringExtra("user");
			drawingView.getDrawings(intent);
		}
		
		loadPicture(drawingView,user);

        /*
         * Restore any state
         */
        if(savedInstanceState != null) {
            loadUi(savedInstanceState);
            drawingView.getFromBundle(PICTURE, savedInstanceState);
        }
		
        /*
         * Get some of the views we'll keep around
         */
		p1Name = (TextView)findViewById(R.id.textViewP1);
		p2Name = (TextView)findViewById(R.id.textViewP2);
		timeText = (TextView)findViewById(R.id.textViewTimeLeft);
		categoryText = (TextView)findViewById(R.id.textViewCategoryType);
		tipText = (TextView)findViewById(R.id.textViewTipText);
		answerText = (EditText)findViewById(R.id.editTextAnswer);
		p1Score = (TextView)findViewById(R.id.textViewScoreP1);
		p2Score = (TextView)findViewById(R.id.textViewScoreP2);
		
		answerText.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);
		
		answerText.setOnKeyListener(new TextView.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	onDone(v);
                	return true;
                }
                return false;
            }
        });
		
		/*
		 *  Set text boxes
		 */
		p1Name.setText(game.getPlayer1DisplayName() + ":");
		p2Name.setText(game.getPlayer2DisplayName() + ":");
		categoryText.setText(game.getCategory());
		p1Score.setText(Integer.toString(game.getPlayer1Score()));
		p2Score.setText(Integer.toString(game.getPlayer2Score()));
	}
	
	/**
     * Handle a Done button press
     * @param view The view that was pressed
     */
	public void onDone(View view)
	{
		if (game.guessAnswer(answerText.getText().toString())) {
			
			game.incrementPlayerScore(game.getGuessingPlayer(), remainingTime);

			game.swapPlayers();
			
			game.randomlySelectCategory();
			
			// Freeze timer
			int finalTime = remainingTime;
			timeText.setText(Integer.toString(finalTime));
			
			if (game.checkForWinner()) {
				updateWinServer();
	        	Intent intent = new Intent(this, FinalActivity.class);
	        	intent.putExtra(GAME, game);
	        	intent.putExtra("user", user);
	        	intent.putExtra("state", "guess");
	    		startActivity(intent);
	        	finish();
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
		} else {
			Toast.makeText(this,"Incorrect", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Timer ran out, alert the player and swap roles
	 */
	public void onGuessFail() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				drawingView.getContext());

		// set title
		alertDialogBuilder.setTitle("Time's up!");
		alertDialogBuilder.setMessage("The correct answer was: " + game.getAnswer());

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
	
	/**
     * Handle an Ok button press
     */
	public void onOk() {
		updateServer();
    	Intent intent = new Intent(this, EditActivity.class);
    	intent.putExtra(GAME, game);
    	intent.putExtra("user", user);
		startActivity(intent);
    	finish();
	}
    
	/**
	 * Handle a back key press
	 */
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

    /* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		gtimer.cancel();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		gtimer = new GameTimer(remainingTime*1000);
		gtimer.start();
		super.onResume();
	}

	/**
     * Handle a saveInstanceState
     */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		drawingView.putToBundle(PICTURE, outState);
		saveUi(outState);
	}
	
    /**
     * Save the view state to a bundle
     * @param bundle bundle to save to
     */
    public void saveUi(Bundle bundle) {
    	bundle.putSerializable(GAME, game);
    	bundle.putInt(TIME, remainingTime);
    	
    }
    
    /**
     * Get the view state from a bundle
     * @param bundle bundle to load from
     */
    public void loadUi(Bundle bundle) {
    	game = (Game)bundle.getSerializable(GAME);
    	remainingTime = bundle.getInt(TIME);
    }
    
    public void loadPicture(DrawingView view, String user)
    {
    	final DrawingView theView = view;
    	final String theUser = user;
    	
    	new Thread(new Runnable() {

            @Override
            public void run() {
            	
            	Cloud cloud = new Cloud();
                InputStream stream = cloud.pullDrawing(theUser);

                // Test for an error
                boolean fail = stream == null;
                if(!fail) {
                    try {
                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");       
                        
                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "tinker");
                        String status = xml.getAttributeValue(null, "status");
                        if(status.equals("yes")) {
                        
                            while(xml.nextTag() == XmlPullParser.START_TAG) {
                                if(xml.getName().equals("drawing")) {
                                    String drawingString = xml.getAttributeValue(null, "picture");
                                    byte[] picBytes = Base64.decode(drawingString, Base64.URL_SAFE);
                                    Picture pic = (Picture)cloud.convertBytesToPic(picBytes);
                                    theView.setPicture(pic);
                                    break;
                                }
                                
                                Cloud.skipToEndTag(xml);
                            }
                        } else {
                            fail = true;
                        }
                        
                    } catch(IOException ex) {
                        fail = true;
                    } catch(XmlPullParserException ex) {
                        fail = true;
                    } finally {
                        try {
                            stream.close(); 
                        } catch(IOException ex) {
                        }
                    }
                }

            }
        }).start();
    }
    
    public void updateServer(){
    	//update server, telling server to switch your role to edit and opponents to wait
    	new Thread(new Runnable(){
    		@Override
    		public void run(){
				Cloud cloud = new Cloud();
		    	cloud.writeUserInfo(game.getPlayer1Name(), game.getPlayer1Score(), 
		    			"edit", game.getPlayer2Name(), game.getPlayer2Score(), 
		    			"wait", game.getAnswer(), game.getTip(), game.getCategory());
    		}
    	}).start();
    }
    
    public void updateWinServer(){
    	//update game, change your state and opponent state to final
    	new Thread(new Runnable(){
    		@Override
    		public void run(){
				Cloud cloud = new Cloud();
		    	cloud.writeUserInfo(game.getPlayer1Name(), game.getPlayer1Score(), 
		    			"final", game.getPlayer2Name(), game.getPlayer2Score(), 
		    			"final", game.getAnswer(), game.getTip(), game.getCategory());
    		}
    	}).start();
    }
}
