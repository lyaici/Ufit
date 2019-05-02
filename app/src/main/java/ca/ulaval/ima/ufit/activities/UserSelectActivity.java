package ca.ulaval.ima.ufit.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import ca.ulaval.ima.ufit.R;

public class UserSelectActivity extends AppCompatActivity {

  ListView userList;
  SQLiteDatabase mydatabase;
  Button loginButtonDialog;
  EditText editUsernameDialog;
  AlertDialog dialog;
  ArrayAdapter<String> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mydatabase = openOrCreateDatabase("ufit",MODE_PRIVATE,null);
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_user_select);
  }

  @Override
  protected void onStart() {
    super.onStart();
    userList = findViewById(R.id.userList);
    Cursor resultSet = mydatabase.rawQuery("SELECT * from Users", null);
    ArrayList<String> users = new ArrayList<>();
    while (resultSet.moveToNext()) {
      users.add(resultSet.getString(0));
    }
    adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, users);
    userList.setAdapter(adapter);
    userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String user = (String) parent.getItemAtPosition(position);
        Intent intent = new Intent(parent.getContext(), UserConsumptionActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
      }
    });
  }

  public void addUser(View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_login, null);
    builder.setView(dialogView);
    dialog = builder.create();
    dialog.show();
    editUsernameDialog = dialog.findViewById(R.id.editUsernameDialog);
    loginButtonDialog = dialog.findViewById(R.id.loginButtonDialog);
  }

  public void loginDialog(View view) {
    String username = editUsernameDialog.getText().toString();
    ContentValues user = new ContentValues();
    user.put("Username", username);
    mydatabase.insert("Users", null, user);
    adapter.add(username);
    dialog.dismiss();
  }
}
