package edu.msu.scrabble.project2;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends Activity {
	
	static boolean nuSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
	}

	public void onCreateUser(final View view) {
		
		final EditText username = (EditText)findViewById(R.id.editTextPlayer1);
    	final EditText password = (EditText)findViewById(R.id.editTextPlayer2);
    	final EditText comPassword = (EditText)findViewById(R.id.editTextPlayer3);
    	
    	if(username.length()!=0 && password.length()!=0 
    			&& password.getText().toString().equals(comPassword.getText().toString())){
    		
    		new Thread(new Runnable(){
        		
        		@Override
        		public void run(){
		    		Cloud cloud = new Cloud();
		    		boolean valid = cloud.newUser(username.getText().toString(), password.getText().toString(), comPassword.getText().toString());
		        		
		    		if(valid == false){
	    	    		view.post(new Runnable(){
	    	    			@Override
	    	    			public void run(){
	    	    				Toast.makeText(view.getContext(), 
	    	    						"User creation failed.", 
	    	    						Toast.LENGTH_SHORT).show();
	    	    				nuSuccess = false;
	    	    			}
	    	
	    	    		});
	    	    		return;
	    	    	}else{
	    	    		view.post(new Runnable(){
	    	    			@Override
	    	    			public void run(){
	    	    				Toast.makeText(view.getContext(), 
	    	    						"User created successfully.", 
	    	    						Toast.LENGTH_SHORT).show();
	    	    				nuSuccess = true;
	    	    			}
	    	    			
	    	    		});
	    	    	}
        		}
    		}).start();
    		
    	}else{
    		Toast.makeText(getApplicationContext(), 
					"Please fill in all fields. Make sure passwords match.", 
					Toast.LENGTH_SHORT).show();
    				nuSuccess = false;
    	}
    	
    	if(nuSuccess == true){
    		finish();
    	}
	}
	
	public void onCancel(View view) {
		finish();
	}

}
