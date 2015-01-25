package com.rahulisola.carpool;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	TextView mainlink;
	EditText username, password, confirmPass, email, phone, fullname;
	Button register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		register = (Button)findViewById(R.id.button1);
		username = (EditText)findViewById(R.id.editText1);
		fullname = (EditText)findViewById(R.id.full_name);
		password = (EditText)findViewById(R.id.editText2);
		confirmPass = (EditText)findViewById(R.id.editText3);
		email = (EditText)findViewById(R.id.editText4);
		phone = (EditText)findViewById(R.id.editText5);
		mainlink = (TextView)findViewById(R.id.textView7);
		mainlink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(password.getText().toString().equals(confirmPass.getText().toString())) {
					if( password.getText().toString().length() >= 8) {
						if( !password.getText().toString().isEmpty() && !username.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !phone.getText().toString().isEmpty() && !fullname.getText().toString().isEmpty()) {
							completeRegistrationProcess();
						}
						else {
							Toast.makeText(getApplicationContext(), "Please complete the form", Toast.LENGTH_SHORT).show();
						}
					}
					else {
						Toast.makeText(getApplicationContext(), "Password should be at least 8 characters", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void completeRegistrationProcess() {
		ParseUser user = new ParseUser();
		user.setUsername(username.getText().toString());
		user.setPassword(password.getText().toString());
		user.setEmail(email.getText().toString());
		user.put("Phone", phone.getText().toString());
		user.put("name", fullname.getText().toString());
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
			    } else {
			    	Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			    }
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
