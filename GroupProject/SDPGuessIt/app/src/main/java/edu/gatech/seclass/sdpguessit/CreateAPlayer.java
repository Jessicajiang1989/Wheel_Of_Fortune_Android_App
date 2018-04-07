package edu.gatech.seclass.sdpguessit;

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


public class CreateAPlayer extends AppCompatActivity
{
    //define the global variables
    protected EditText FirstName;
    protected EditText LastName;
    protected EditText UserName;
    protected EditText Email;
    //initialize the DbHelper class to do the database part


    //OnCreate(): this is create an activity
    //The entire lifetime of an activity happens between the first call to onCreate(Bundle) through to a single final call to onDestroy().
    // An activity will do all setup of "global" state in onCreate(), and release all remaining resources in onDestroy().
    //For example, if it has a thread running in the background to download data from the network, it may create that thread in onCreate() and then stop the thread in onDestroy().
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //By calling super.onCreate(savedInstanceState);,
        //you tell the Dalvik VM to run your code in addition to the existing code in the onCreate() of the parent class.
        //If you leave out this line, then only your code is run. The existing code is ignored completely.
        super.onCreate(savedInstanceState);
        //set the UI we want
        setContentView(R.layout.activity_create_a_player);
        //get the global variable in this class
        FirstName = (EditText) findViewById(R.id.FirstName);
        LastName = (EditText) findViewById(R.id.LastName);
        UserName = (EditText) findViewById(R.id.UserName);
        Email = (EditText) findViewById(R.id.Email);
        //set the initial value
        FirstName.setText("");
        LastName.setText("");
        UserName.setText("");
        Email.setText("");

    }

    //onCreate table already
    DbHelper db = new DbHelper(this);
    //handle click on Submit Button
    public void handleClick(View view)
    {
        //get all the Strings from the input window of UI
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();
        String username = UserName.getText().toString();
        String email = Email.getText().toString();
        //this is for checking whitespace
        Pattern pattern = Pattern.compile("\\s");
        //this is for check email format
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern_email = Pattern.compile(EMAIL_PATTERN);
        //check is for making sure each input is correct in format
        boolean check = false;

        switch (view.getId())
        {
            case R.id.SubmitButton:
                //check these 4 variables

                //check the length of firstname
                if(firstname.length() < 2)
                {
                    FirstName.setError("Please make sure the length of firstname is at least 2");
                }
                //check the whitespace character in firstname
                Matcher matcher = pattern.matcher(firstname);
                boolean found = matcher.find();
                if(found == true)
                {
                    FirstName.setError("Whitespace character in firstname");
                    check = true;
                }
                //check the length of lastname
                if(lastname.length() < 2)
                {
                    LastName.setError("Please make sure the length of lastname is at least 2");
                    check = true;
                }
                //check the whitespace character in lastname
                matcher = pattern.matcher(lastname);
                found = matcher.find();
                if(found == true)
                {
                    LastName.setError("Whitespace character in lastname");
                    check = true;
                }
                //check the length of username
                if(username.length() < 4)
                {
                    UserName.setError("Please make sure the length of username is at least 4");
                    check = true;
                }
                //check the whitespace character in username
                matcher = pattern.matcher(username);
                found = matcher.find();
                if(found == true)
                {
                    UserName.setError("Whitespace character in username");
                    check = true;
                }
                //check the length of email
                if(email.length() < 6)
                {
                    Email.setError("Please make sure the length of email is at least 6");
                    check = true;
                }
                //check the whitespace character in username
                matcher = pattern_email.matcher(email);
                found = matcher.find();
                if(found == false)
                {
                    Email.setError("Please check your email format");
                    check = true;
                }

                //all the input formats are right
                if(check == false)
                {
                    LinkToDatabase(firstname,lastname,username,email);
                }
                break;
            //this is for exitting
            //return to the front page
            case R.id.ExitButton:
                finish();
                break;
        }





    }


    //handle with the database
    public void LinkToDatabase(String firstname, String lastname, String username, String email)
    {


        //create a database named WheelOfFortune
        try
        {

            //check the CheckUserName is empty or not
            //if not it will move to the first row
            //if it is, so the USERNAME is unique
            //check the duplicate!
            //! check the username is used or not
            if(db.checkUserName(username))
            {
                UserName.setError("Duplicated UserName!Please another one!");
            }
            //insert into player table
            db.insertPlayer(firstname,lastname,username,email);

            //show everything in database to logfile
            //Cursor showeverything = DB.rawQuery(SELECT_EVERYTHING,null);
            //Log.v("Cursor_showeverything", DatabaseUtils.dumpCursorToString(showeverything));
            //successfully and show the message
            Toast.makeText(this, "Create A User Successfully\nPlease Click Exit!", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
