package com.example.citycat;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Login extends Activity {

	Button submit;
	EditText username;
	EditText password;
	EditText verify;
	Context thisContext;
	Intent mainAcitvity;
	int option;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		thisContext = this;

		mainAcitvity = new Intent(this, MainActivity.class);

		// check user's sign up status
		SharedPreferences ref = this.getSharedPreferences("user_details",MODE_PRIVATE);
		boolean userStatus = ref.getBoolean("signupStatus", false);

	if (userStatus){
			this.startActivity(mainAcitvity);
		}

		/* Parse.com - Initialize */
		Parse.initialize(this, "sN3Uhl2rCCJvp1rodg9hYqw9pZN8kVkYuPCCwn5D", "ECprIUSxorEFhSSzq7ani1dR7Up4gApnjAmPFjiY"); 

		username = (EditText) this.findViewById(R.id.username_input);
		password = (EditText) this.findViewById(R.id.password_input);
		submit = (Button) this.findViewById(R.id.SignUp_button);

		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				ParseQuery query = new ParseQuery("Users");
				query.whereMatches("username", username.getText().toString());
				query.whereMatches("password", password.getText().toString());
				query.findInBackground(new FindCallback() {
					public void done(List<ParseObject> objects, ParseException e) {

						if (objects.size() == 0 || e != null ){
							AppAlertDialog.showNeutraAlertDialog(thisContext,"Sign Up","Username or password is incorredct. Please type them again.",null);
							password.setText("");
							username.setText("");
						}
						else {
							SharedPreferences ref = getSharedPreferences("user_details",MODE_PRIVATE);
							SharedPreferences.Editor ed = ref.edit();
							ed.putString("username",username.getText().toString().trim());
							ed.putBoolean("signupStatus",true);
							ed.commit();
							thisContext.startActivity(mainAcitvity);						}
					}
				});
			}
		});
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

}
