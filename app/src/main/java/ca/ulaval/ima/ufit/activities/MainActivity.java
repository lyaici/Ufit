package ca.ulaval.ima.ufit.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ca.ulaval.ima.ufit.R;

public class MainActivity extends AppCompatActivity {

  ProgressBar progressBar;
  EditText editUsername;
  Button createUserBtn;
  TextView errorMessage;
  SQLiteDatabase mydatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SharedPreferences dataSave = getSharedPreferences("firstLog", 0);
    if(dataSave.getString("firstTime", "").toString().equals("no")){
      Intent intent = new Intent(this, UserSelectActivity.class);
      finishAffinity();
      startActivity(intent);
    }
    else{
      SharedPreferences.Editor editor = dataSave.edit();
      editor.putString("firstTime", "no");
      editor.commit();
    }
    mydatabase = openOrCreateDatabase("ufit",MODE_PRIVATE,null);
    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Users(Username VARCHAR NOT NULL UNIQUE PRIMARY KEY)");
    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Items(Username VARCHAR NOT NULL, Title VARCHAR, Description VARCHAR, Brand VARCHAR, Image VARCHAR, Calories INT, FOREIGN KEY(Username) REFERENCES Users(Username))");
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onStart() {
    super.onStart();
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    editUsername = (EditText) findViewById(R.id.editUsername);
    createUserBtn = (Button) findViewById(R.id.loginButton);
    errorMessage = (TextView) findViewById(R.id.errorMessage);
  }

  public void login(View view) {
    errorMessage.setVisibility(View.GONE);
    String username = editUsername.getText().toString();
    if (username.length() > 0) {
      createUserBtn.setVisibility(View.GONE);
      progressBar.setVisibility(View.VISIBLE);
      ContentValues user = new ContentValues();
      user.put("Username", username);
      try {
        mydatabase.insert("Users", null, user);
        Intent intent = new Intent(this, UserSelectActivity.class);
        startActivity(intent);
        createUserBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
      } catch (SQLiteConstraintException e) {
        createUserBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
      }
    } else {
      errorMessage.setVisibility(View.VISIBLE);
    }
  }

}
