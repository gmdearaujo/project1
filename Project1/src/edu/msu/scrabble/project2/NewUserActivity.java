package edu.msu.scrabble.project2;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
	}

	public void onCreateUser(View view) {
		
		EditText username = (EditText)findViewById(R.id.editTextPlayer1);
    	EditText password = (EditText)findViewById(R.id.editTextPlayer2);
    	EditText comPassword = (EditText)findViewById(R.id.editTextPlayer3);
    	
    	if(username.length() != 0 && password.length()!=0 && password.getText().toString().equals(comPassword.getText().toString())){
    		boolean valid = false;
    		
    		//check with server if username is valid
    		
    		if(valid){
    			Toast.makeText(getApplicationContext(), 
    					"User successfully created.", 
    					Toast.LENGTH_SHORT).show();
    			finish();
    		}else{
    			Toast.makeText(getApplicationContext(), 
    					"User creation failed.", 
    					Toast.LENGTH_SHORT).show();
    		}
    	}else{
    		Toast.makeText(getApplicationContext(), 
    				"Error. Please fill in both Username and Password fields. Make sure passwords match.", 
    				Toast.LENGTH_SHORT).show();
    	}
	}
	
	public void onCancel(View view) {
		finish();
	}

}
