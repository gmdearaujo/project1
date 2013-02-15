package edu.msu.scrabble.project1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class GuessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);
	}
	
	public void onDone(View view)
	{
		Intent intent = new Intent(this, EditActivity.class);
		startActivity(intent);
	}

}
