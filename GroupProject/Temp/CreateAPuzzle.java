package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateAPuzzle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_puzzle);
        //placeholder for instantiating database class
    }

    //global variables & db
    DbHelper db = new DbHelper(this);

    public void savePuzzle(View view) {
        EditText txtPhrase = findViewById(R.id.tPhrase);
        EditText numOfAllowedGuesses = findViewById(R.id.txtNumOfAllowedWrong);


        String phrase = "";
        int noAllowedWrongGuesses = 0;
        //boolean isRandom = false;//this doesn't serve any purpose, need to remove
        //boolean isPartofTournament = false;//this is probably better handled by creating an entry in a puzzle/tourn relationship table
        //int currentGuess = 0;//this was a char in the UML, need to change, no longer need this, fix UML
        String puzzleCreator = "system";//placeholder until player.getUsername is implemented
        boolean isPuzzleComplete = false;
        int noOfConsonantOccurences = 0;
        int prizeEarned = 0;

        phrase = txtPhrase.getText().toString();
        noAllowedWrongGuesses = Integer.parseInt(numOfAllowedGuesses.getText().toString());
        if(noAllowedWrongGuesses <0 || noAllowedWrongGuesses>10){
            numOfAllowedGuesses.setError("This number must be between 0 and 10");
            return;
        }

        db.insertPuzzle(phrase,noAllowedWrongGuesses,"system");//need to add "getUser()" method

        Intent intent = new Intent(CreateAPuzzle.this, SolvePuzzle.class);
        startActivity(intent);
    }
}
