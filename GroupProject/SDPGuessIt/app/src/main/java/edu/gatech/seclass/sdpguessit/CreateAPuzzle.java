package edu.gatech.seclass.sdpguessit;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateAPuzzle extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_puzzle);
        //placeholder for instantiating database class
    }

    //global variables & db
    DbHelper db = new DbHelper(this);

    public void savePuzzle(View view)
    {
        //input text of phrase
        EditText txtPhrase = findViewById(R.id.tPhrase);
        //max number of wrong guess
        EditText numOfAllowedGuesses = findViewById(R.id.txtNumOfAllowedWrong);


        String phrase = "";
        int noAllowedWrongGuesses = 0;
        //boolean isRandom = false;//this doesn't serve any purpose, need to remove
        //boolean isPartofTournament = false;//this is probably better handled by creating an entry in a puzzle/tourn relationship table
        //int currentGuess = 0;//this was a char in the UML, need to change, no longer need this, fix UML
        //static global user name
        String username = GlobalVariables.getInstance().globalUserName;//placeholder until player.getUsername is implemented

        boolean isPuzzleComplete = false;
        int noOfConsonantOccurences = 0;
        int prizeEarned = 0;

        phrase = txtPhrase.getText().toString();
        try {
            noAllowedWrongGuesses = Integer.parseInt(numOfAllowedGuesses.getText().toString());
        } catch (NumberFormatException e) {
            numOfAllowedGuesses.setError("Please use a whole number between 0 and 10");
            e.printStackTrace();
            return;
        }

        if(noAllowedWrongGuesses <0 || noAllowedWrongGuesses>10){
            numOfAllowedGuesses.setError("This number must be between 0 and 10");
            return;
        }


        //insert puzzle into database
        db.insertPuzzle(phrase,noAllowedWrongGuesses,username);//need to add "getUser()" method

        //return unique id
        TextView puzzleId = findViewById(R.id.lPuzzleId);
        int id = db.selectid();

        puzzleId.setText("Your Puzzle ID: " + id);
    }

    public void exitPressed(View view) {
        finish();
    }
}
