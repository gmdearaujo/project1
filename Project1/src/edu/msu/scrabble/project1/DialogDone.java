package edu.msu.scrabble.project1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DialogDone extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_done);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog_done, menu);
		return true;
	}

}
