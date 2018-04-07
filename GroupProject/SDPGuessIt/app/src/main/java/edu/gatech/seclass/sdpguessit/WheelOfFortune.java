package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.util.Log;
import java.util.regex.*;
import android.database.DatabaseUtils;
import android.database.sqlite.*;

public class WheelOfFortune extends AppCompatActivity
{

    EditText usernameEt;
    DbHelper db = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_of_fortune);
        usernameEt = (EditText) findViewById(R.id.userNameEditText);
    }
    //this function is for CreateANewPlayer
    public void clickCreateANewPlayer(View view)
    {
        //An Intent is an object that provides runtime binding between separate components,
        // such as two activities. The Intent represents an app’s "intent to do something."
        // You can use intents for a wide variety of tasks,
        // but in this lesson, your intent starts another activity.
        Intent newPlayerIntent = new Intent(this, CreateAPlayer.class);
        startActivity(newPlayerIntent);
    }
    //this function is for LogIn
    public void loginClicked(View view)
    {

        String userNameStr = usernameEt.getText().toString();


        //Intent menuInent = new Intent(this, MenuActivity.class);
        //startActivity(menuInent);


        if (userNameStr.isEmpty())
        {
            Toast.makeText(WheelOfFortune.this, "Username Is Empty!", Toast.LENGTH_LONG).show();
        }
        else if (db.checkUserName(userNameStr))
        {
            GlobalVariables.getInstance().globalUserName = userNameStr;
            Intent menuInent = new Intent(this, MenuActivity.class);
            startActivity(menuInent);
        }
        else
        {
            Toast.makeText(WheelOfFortune.this, "Username Not Exist!", Toast.LENGTH_LONG).show();
        }


    }



    /*open a database
    @Override
    protected void onResume()
    {
        super.onResume();
        db.open();
    }
    //close a database
    @Override
    protected void onPause()
    {
        super.onPause();
        db.close();
    }
    */


}
