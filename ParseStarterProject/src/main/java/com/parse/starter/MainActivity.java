/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signUpModeActive = true;
  TextView loginTextView;
  EditText usernameEditText;
  EditText passwordEditText;

  public void showUserList(){
    //Otvaranje novog activitya (Lista korisnika)
    Intent intent = new Intent(getApplicationContext(), ListaKorisnika.class);
    startActivity(intent);
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
  //sluzi za submitanje obrasca pritiskom na tipku enter

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN){
      signUpClicked(view);
    }

    return false;
  }

  @Override
  public void onClick(View view) {
    if(view.getId() == R.id.loginTextView){
      //ako korisnik klikne na promjenu registracija/prijava
      Button signUpButton = (Button) findViewById(R.id.signUpButton);

      if(signUpModeActive){
        signUpModeActive = false;
        signUpButton.setText("PRIJAVA");
        loginTextView.setText("Idi na REGISTRACIJU");
      }else{
        signUpModeActive = true;
        signUpButton.setText("REGISTRACIJA");
        loginTextView.setText("Idi na PRIJAVU");
      }
    }else if (view.getId() == R.id.logoImageView || view.getId() == R.id.backgroundLayout){
      //ako korinsik klikne na prazno polje ili na sliku, tipkovnica se skriva
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }

  public void signUpClicked(View view){

    
    if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
      Toast.makeText(this, "Potrebni su korisničko ime i lozinka", Toast.LENGTH_SHORT).show();

    }else{
      if(signUpModeActive) {
        //Sign up
        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Registracija", "uspjesna");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }else{
        //Login
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if(user != null){
              Log.i("Prijava", "OK");
              showUserList();
            }else{
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Scope");

    //pristupanje svim elemntima layouta
    loginTextView = (TextView) findViewById(R.id.loginTextView);
    loginTextView.setOnClickListener(this);
    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    ImageView logoImageView = findViewById(R.id.logoImageView);
    RelativeLayout backgroundLayout = findViewById(R.id.backgroundLayout);
    logoImageView.setOnClickListener(this);
    backgroundLayout.setOnClickListener(this);

    passwordEditText.setOnKeyListener(this);

    if(ParseUser.getCurrentUser() != null){ //provjerava ako je korisnik otvorio aplikaciju a već je logiran od prije
      showUserList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}