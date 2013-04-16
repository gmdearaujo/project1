package edu.msu.scrabble.project2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class WaitTurnActivity extends Activity {

	private Game game;
	private java.util.Timer timer;
	
	private static Document gameinfo;
	private static String user;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_wait_turn);
		
		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		user = (String)intent.getStringExtra("user");
		
		timer = new Timer();
	    timer.schedule(new waitLoop(), 0, 1000*3);
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
			    	if(gameinfo != null){
						updateGame(game);
						String activity;
						NamedNodeMap tinker = gameinfo.getElementsByTagName("game").item(0).getAttributes();
						if(tinker.getNamedItem("player1").getNodeValue().equals(game.getPlayer1Name())){
				    		activity = tinker.getNamedItem("p1state").getNodeValue();
				    	}
				    	else{
				    		activity = tinker.getNamedItem("p2state").getNodeValue();
				    	}
						launchNewActivity(activity);
					}
	    		}
	    	}).start();
	    }
	}
	
	public void launchNewActivity(String s){		
		if(s.equals("edit")){
			timer.cancel();
			Intent intent = new Intent(this, EditActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
			startActivity(intent);
		}
		else if(s.equals("lobby")){
			timer.cancel();
			Intent intent = new Intent(this, LobbyActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
			startActivity(intent);
		}
		else if(s.equals("guess")){
			timer.cancel();
			Intent intent = new Intent(this, GuessActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
			startActivity(intent);
		}
		else if(s.equals("final")){
			timer.cancel();
			Intent intent = new Intent(this, FinalActivity.class);
	    	intent.putExtra("GAME", game);
	    	intent.putExtra("user", user);
	    	intent.putExtra("state", "wait");
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
    	
    	NamedNodeMap tinker = gameinfo.getElementsByTagName("game").item(0).getAttributes();
    	
    	if(tinker.getNamedItem("player1").getNodeValue().equals(user)){
    		g.setPlayer1Name(user);
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
