package com.rahulisola.carpool;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText username, password;
	TextView registrationLink;
	Button login;
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, "n9Bu7UPggmrWiKExrG4Pfgx1rTyhwDlWKCZDOKvq", "HLn2ZZ8axtoiWRlxzMdUVpau1LLlQ1e7fnlGN6hJ");
		ParseAnalytics.trackAppOpened(getIntent());
		
		username = (EditText)findViewById(R.id.editText1);
		password = (EditText)findViewById(R.id.editText2);
		
		registrationLink = (TextView)findViewById(R.id.textView3);
		registrationLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		login = (Button)findViewById(R.id.button1);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
					  public void done(ParseUser user, ParseException e) {
					    if (user != null) {
					    	System.out.println("done");
							Intent i = new Intent(MainActivity.this, HomeActivity.class);
							startActivity(i);
					    } else {
					    	Toast.makeText(getApplicationContext(), "Unable to log in. Please try again", Toast.LENGTH_SHORT).show();
					    }
					  }
					});
				if(ParseUser.getCurrentUser()!=null) {
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
