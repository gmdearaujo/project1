package edu.msu.scrabble.project2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class LobbyActivity extends Activity {
	
	private Game game;
	private java.util.Timer timer;
	
	private static Document gameinfo;
	private static String user;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_lobby);
		
		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		user = (String)intent.getStringExtra("user");
		
		timer = new Timer();
	    timer.schedule(new waitLoop(), 0, 1000*15);
	}

	private class waitLoop extends java.util.TimerTask{
	    public void run() //this becomes the loop
	    {
	    	new Thread(new Runnable(){
	    		@Override
	    		public void run(){
					Cloud cloud = new Cloud();
			    	InputStream stream = cloud.readUserInfo(user);
			    	gameinfo = xmlParserFunc(stream);
	    		}
	    	}).start();
			
			if(gameinfo != null){
				updateGame(game);
				String activity;
				if(gameinfo.getElementsByTagName("Player1").item(0).getNodeValue().equals(game.getPlayer1Name())){
		    		activity = gameinfo.getElementsByTagName("P1State").item(0).getNodeValue();
		    	}
		    	else{
		    		activity = gameinfo.getElementsByTagName("P2State").item(0).getNodeValue();
		    	}
				launchNewActivity(activity);
			}
	    }
	}
	
	public void launchNewActivity(String s){
		timer.cancel();
		
		if(s.equals("edit")){
			Intent intent = new Intent(this, EditActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
			startActivity(intent);
		}
		else if(s.equals("wait")){
			Intent intent = new Intent(this, WaitTurnActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
			startActivity(intent);
		}
		else if(s.equals("guess")){
			Intent intent = new Intent(this, GuessActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
			startActivity(intent);
		}
		else if(s.equals("final")){
			Intent intent = new Intent(this, FinalActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
			startActivity(intent);
		}
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
    	if(gameinfo.getElementsByTagName("Player1").item(0).getNodeValue().equals(g.getPlayer1Name())){
    		g.setPlayer2Name(gameinfo.getElementsByTagName("Player2").item(0).getNodeValue());
    		g.setPlayer1Score(Integer.parseInt(gameinfo.getElementsByTagName("P1Score").item(0).getNodeValue()));
    		g.setPlayer2Score(Integer.parseInt(gameinfo.getElementsByTagName("P2Score").item(0).getNodeValue()));
    	}
    	else{
    		g.setPlayer2Name(gameinfo.getElementsByTagName("Player1").item(0).getNodeValue());
    		g.setPlayer1Score(Integer.parseInt(gameinfo.getElementsByTagName("P1Score").item(0).getNodeValue()));
    		g.setPlayer2Score(Integer.parseInt(gameinfo.getElementsByTagName("P2Score").item(0).getNodeValue()));
    	}
    	g.setAnswer(gameinfo.getElementsByTagName("Answer").item(0).getNodeValue());
    	g.setTip(gameinfo.getElementsByTagName("Tip").item(0).getNodeValue());
    	g.setCategory(gameinfo.getElementsByTagName("Category").item(0).getNodeValue());
    }
}
