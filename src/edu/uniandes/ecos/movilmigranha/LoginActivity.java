package edu.uniandes.ecos.movilmigranha;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.uniandes.ecos.movilmigranha.utils.EncodingUtils;

public class LoginActivity extends Activity {

	private EditText usernameText;
	private EditText passwordText;
	private Button loginButton;
	private TextView errorTextView;
	
	public static final String LOGIN_ACTIVITY_TAG = "MovilMigranha:LoginActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setUpVariables();
		setUpListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setUpVariables(){
		this.usernameText = (EditText) findViewById(R.id.usernameEdit);
		this.passwordText = (EditText) findViewById(R.id.passwordEdit);
		this.loginButton = (Button) findViewById(R.id.loginButton);
		this.errorTextView = (TextView) findViewById(R.id.errorTextView);
	}
	
	private void setUpListeners(){
		//Password Text Listener (if user press Enter)
		this.passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_NULL &&
						event.getAction() == KeyEvent.ACTION_DOWN &&
						event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
					initLogin();
					return true;
				}
				return false;
			}
		});
		//Login Button Listener (If user press Login button)
		this.loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initLogin();
			}
		});
	}
	
	/**
	 * Calls the LoginTask instance to invoke the Login Service
	 */
	private void initLogin(){
		
		String user = usernameText.getText().toString();
		String pwd = passwordText.getText().toString();
		
		if(user.isEmpty() || pwd.isEmpty()){
			errorTextView.setText(R.string.login_fail_required);
			errorTextView.setVisibility(View.VISIBLE);
			usernameText.requestFocus();
			return;
		}
		
		LoginTask loginTask = new LoginTask();
		String userName = user;
		String passWord = EncodingUtils.getMD5Hash(pwd);
		
		Log.i(LOGIN_ACTIVITY_TAG, "Iniciando autenticación de usuario " + user);
		Log.d(LOGIN_ACTIVITY_TAG, "Password Hash: " + passWord);
		
		try {
			if(loginTask.execute(userName, passWord).get()){
				//Autenticación OK
			}
			else{
				//Autenticación FAIL
				errorTextView.setText(R.string.login_fail_not_found);
				errorTextView.setVisibility(View.VISIBLE);
				usernameText.requestFocus();
			}
		} catch (InterruptedException e) {
			Log.e(LOGIN_ACTIVITY_TAG, "Error ejecutando autenticación", e);
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.e(LOGIN_ACTIVITY_TAG, "Error ejecutando autenticación", e);
			e.printStackTrace();
		}
	}
	
}
