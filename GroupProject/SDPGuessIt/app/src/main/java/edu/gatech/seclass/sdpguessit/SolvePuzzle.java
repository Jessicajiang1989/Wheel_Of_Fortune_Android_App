package edu.gatech.seclass.sdpguessit;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.String;

public class SolvePuzzle extends AppCompatActivity
{

    //Global Variables
    public static String DEFAULT_PHRASE = "GUESS ME!";
    public String phraseInProgress = DEFAULT_PHRASE;
    public int PUZZLE_ID = 0;
    public String USERNAME = GlobalVariables.globalUserName;//bookmark, add user data
    DbHelper db = new DbHelper(this);
    ArrayList<String> dbvalues = new ArrayList();
    TextView allowedGuesses;
    String currentTournamentName = null;
    String vowels = "aeiou";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        //send data from one activity to another activity
        Bundle extras = getIntent().getExtras();
        //remaining number of guess
        allowedGuesses = findViewById(R.id.tRemainingNumOfGuesses);

        if (extras != null) {
            // This value is coming from join tournament
            //get the puzzle id from other activity!
            String[] puzzlesArray  = extras.getStringArray("kAllPuzzlesFromJoinTournament");
            currentTournamentName = extras.getString("kTournamentName");
            startPlayingTheFirstPuzzle(puzzlesArray);
        } else {
            try {
                dbvalues = db.selectRandom();
                PUZZLE_ID = Integer.parseInt(dbvalues.get(0).toString());
                DEFAULT_PHRASE = dbvalues.get(1).toString();//put back when done testing, need to merge with Yuhao's db
                allowedGuesses.setText(dbvalues.get(2).toString());
                hidePhrase();
                setPrize();
            } catch (IndexOutOfBoundsException e) {
                Intent exitIntent = new Intent(SolvePuzzle.this, NoPuzzlesAvailable.class);
                startActivity(exitIntent);
            }
        }
    }

    private void startPlayingTheFirstPuzzle(String[] puzzlesArray) {
        if (!(puzzlesArray.length == 0)) {
            String firstPuzzle = puzzlesArray[0];
            if (!firstPuzzle.isEmpty()) {
                startNewPuzzle(firstPuzzle);
            } else {
                Toast.makeText(this, "Puzzle Id Nil", Toast.LENGTH_LONG).show();
                db.Compute_Total_Prize_Tournament(currentTournamentName,GlobalVariables.getInstance().globalUserName);
            }
        } else {
            Toast.makeText(this, "All Puzzles Over. Tournament Over. Please Exit.", Toast.LENGTH_LONG).show();
//            compute the total prize for this tournament
            db.Compute_Total_Prize_Tournament(currentTournamentName,GlobalVariables.getInstance().globalUserName);
        }
    }

    private void startNewPuzzle(String newPuzzle) {
        TextView lettersNotYetChosen = findViewById(R.id.tLettersNotChosen);
        lettersNotYetChosen.setText("abcdefghijklmnopqrstuvwxyz");
        Integer puzzleInt = Integer.parseInt(newPuzzle) ;
        //dbvalues is arraylist<string>
        dbvalues = db.selectPuzzleId(puzzleInt);
        //get puzzle Id
        PUZZLE_ID = Integer.parseInt(dbvalues.get(0).toString());
        //get the phrase from create a puzzles
        DEFAULT_PHRASE = dbvalues.get(1).toString();//put back when done testing, need to merge with Yuhao's db
        allowedGuesses.setText(dbvalues.get(2).toString());
        hidePhrase();
        setPrize();
        enableButtons();
    }

    //default the prize choices
    protected void setPrize()
    {
        int currentPrize = ThreadLocalRandom.current().nextInt(1,10)*100;
        TextView tCurrentPrize = findViewById(R.id.tCurrentPrize);
        TextView tTotalPrize = findViewById(R.id.tTotalPrize);

        tCurrentPrize.setText(Integer.toString(currentPrize));
        tTotalPrize.setText("0");
    }

    protected void increasePrize(String letter)
    {
        int currentPrize = ThreadLocalRandom.current().nextInt(1,10)*100;
        TextView tCurrentPrize = findViewById(R.id.tCurrentPrize);
        TextView tTotalPrize = findViewById(R.id.tTotalPrize);
        int iTotalPrize = Integer.parseInt(tTotalPrize.getText().toString());
        EditText phrase = findViewById(R.id.tPhrase);
        String sPhrase = phrase.getText().toString().toLowerCase();

        if(letter.equals("WIN")){
            //add up all the letters
            //countLetters*1000
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<sPhrase.length(); i++){
                if(phraseInProgress.substring(i,i+1).equals("#")){
                    sb.append(sPhrase.substring(i,i+1));
                };
            }
            int numOfLettersLeft = sb.toString().length();
            int addToPrize = 1000*numOfLettersLeft;
            iTotalPrize = iTotalPrize + addToPrize;

            tTotalPrize.setText(Integer.toString(iTotalPrize));
            phrase.setText("YOU WIN!");
            //store to tpuzzlestatistics(puzzle ID, username, prize)
            saveStatistics(PUZZLE_ID,USERNAME,iTotalPrize, currentTournamentName);
            disableButtons();
            //if this puzzle was solved as part of a tournament
            //do something move to next puzzle
            if(GlobalVariables.inTournament == true) {
                // get the list of puzzles remaining in tournament.
                //bookmark, this needs to be inside of an if statement
                List puzzlesList = db.PuzzlesInTournament(currentTournamentName);
                String[] puzzlesArray = new String[puzzlesList.size()];
                puzzlesList.toArray(puzzlesArray);

                startPlayingTheFirstPuzzle(puzzlesArray);
            }
        }
        else {
            int numOfConsonants = countConsonants(letter);
            int newTotal = Integer.parseInt(tCurrentPrize.getText().toString())*numOfConsonants + Integer.parseInt(tTotalPrize.getText().toString());
            tCurrentPrize.setText(Integer.toString(currentPrize));
            tTotalPrize.setText(Integer.toString(newTotal));
        }

    }

    protected void hidePhrase()
    {
        EditText phrase = findViewById(R.id.tPhrase);
        TextView lettersNotYetChosen = findViewById(R.id.tLettersNotChosen);
        String sLettersNotYetChosen = lettersNotYetChosen.getText().toString().toLowerCase();

        StringBuilder sb = new StringBuilder();

        for(int i=0; i<DEFAULT_PHRASE.length();i++){
            if(Character.isLetter(DEFAULT_PHRASE.charAt(i)) && sLettersNotYetChosen.contains(DEFAULT_PHRASE.substring(i,i+1).toLowerCase()) == true ){
                sb.append("#");
            }
            else{
                sb.append(DEFAULT_PHRASE.charAt(i));
            }
        }
        phrase.setText(sb.toString());
        phraseInProgress = sb.toString();
        //if all letters are revealed, alert user that they won
    }

    protected void buyAVowel(View view){
        EditText letterToGuess = findViewById(R.id.tLetterToGuess);
        String sLetterToGuess = letterToGuess.getText().toString().toLowerCase();
        TextView lettersNotYetChosen = findViewById(R.id.tLettersNotChosen);
        String sLettersNotYetChosen = lettersNotYetChosen.getText().toString().toLowerCase();
        TextView totalPrize = findViewById(R.id.tTotalPrize);
        int iTotalPrize = Integer.parseInt(totalPrize.getText().toString());
        EditText phrase = findViewById(R.id.tPhrase);
        String sPhrase = DEFAULT_PHRASE.toLowerCase();
        int vowelPenalty = 300;
        //validate that the letter is a vowel, return message if not
        if(vowels.contains(sLetterToGuess) == false){
            letterToGuess.setError("To buy a vowel the letter to guess must be one of a,e,i,o,u");
            return;
        }
        if(sLettersNotYetChosen.contains(sLetterToGuess) == false){
            letterToGuess.setError("This letter has been chosen already. Please choose another.");
            return;
        }
        //remove from chosen letters
        recordChosenLetters(sLetterToGuess);
        //subtract 300 from total prize
        int newTotal = iTotalPrize - vowelPenalty;
        totalPrize.setText(Integer.toString(newTotal));
        //validate that guess is correct
        if(sPhrase.contains(sLetterToGuess)){
            //if yes, do nothing because it's a vowel
            hidePhrase();
        }
        else {
            //if no, subtract from guesses
            subtractGuesses();
        }
    }

    public void guessAConsonant(View view)
    {
        EditText letterToGuess = findViewById(R.id.tLetterToGuess);
        TextView lettersNotYetChosen = findViewById(R.id.tLettersNotChosen);
        EditText phrase = findViewById(R.id.tPhrase);
        String sLettersNotYetChosen = lettersNotYetChosen.getText().toString().toLowerCase();
        String sLetterToGuess = letterToGuess.getText().toString().toLowerCase();
        String sPhrase = DEFAULT_PHRASE.toLowerCase();
        //check to make sure letter is a consonant
        //check to make sure the letter has not been guessed before
        if(sLettersNotYetChosen.contains(sLetterToGuess) == false){
            letterToGuess.setError("This letter has been chosen already. Please choose another.");
            return;
        }
        if(vowels.contains(sLetterToGuess) == true){
            letterToGuess.setError("A consonant cannot be any of the letters a,e,i,o,u");
            return;
        }
        //remove letter from lettersNotYetChosen
        recordChosenLetters(sLetterToGuess);
        //check for whether the letter is in the phrase
        if(sPhrase.contains(sLetterToGuess)){
            //if yes add to prize
            increasePrize(sLetterToGuess);
            hidePhrase();
        }
        else {
            //if no, subtract from guesses
            subtractGuesses();
        }
    }

    protected void solvePuzzle(View view)
    {
        EditText phrase = findViewById(R.id.tPhrase);
        String sPhrase = phrase.getText().toString().toLowerCase();
        TextView totalPrize = findViewById(R.id.tTotalPrize);

        //check to make sure the user didn't accidentally click solve puzzle
        if(sPhrase.contains("#")){
            phrase.setError("Please replace all occurrences of # with a letter before solving the puzzle.");
            return;
        }
        if (sPhrase.equals(DEFAULT_PHRASE.toLowerCase())){
            increasePrize("WIN");
        }
        else{
            totalPrize.setText("0");
            phrase.setText("GAME OVER!");
            //save to statistics to database
            saveStatistics(PUZZLE_ID,USERNAME,Integer.parseInt(totalPrize.getText().toString()),currentTournamentName);
            disableButtons();
            //set prize to 0 and end game
        }
    }

    protected void subtractGuesses()
    {
        //if end of guesses, end puzzle
        //else hide phrase
        //subtract from numofallowedwrong guesses
        TextView phrase = findViewById(R.id.tPhrase);
        TextView prize = findViewById(R.id.tTotalPrize);
        TextView allowedGuesses = findViewById(R.id.tRemainingNumOfGuesses);
        int iAllowedGuesses = Integer.parseInt(allowedGuesses.getText().toString());
        iAllowedGuesses = iAllowedGuesses - 1;
        allowedGuesses.setText(Integer.toString(iAllowedGuesses));

        if(iAllowedGuesses < 1){
            phrase.setText("GAME OVER! You ran out of guesses.");
            prize.setText("0");
            saveStatistics(PUZZLE_ID,USERNAME,Integer.parseInt(prize.getText().toString()),currentTournamentName);
            db.Compute_Total_Prize_Tournament(currentTournamentName,GlobalVariables.getInstance().globalUserName);
            disableButtons();
            return;
        }
        //should add something to lock down buttons, or prevent player from doing any other actions
        hidePhrase();
    }

    protected int countConsonants(String letter)
    {
        int oldPhraseSize = DEFAULT_PHRASE.length();
        String guessesRemoved = DEFAULT_PHRASE.replace(letter,"");
        int newPhraseSize = guessesRemoved.length();
        int numOfConsonants = oldPhraseSize - newPhraseSize;

        return numOfConsonants;
    }

    public void recordChosenLetters(String letter)
    {
        EditText letterToGuess = findViewById(R.id.tLetterToGuess);
        TextView lettersNotYetChosen = findViewById(R.id.tLettersNotChosen);
        EditText phrase = findViewById(R.id.tPhrase);
        String sLettersNotYetChosen = lettersNotYetChosen.getText().toString().toLowerCase();
        String sLetterToGuess = letterToGuess.getText().toString().toLowerCase();
        String sPhrase = DEFAULT_PHRASE.toLowerCase();

        sLettersNotYetChosen = sLettersNotYetChosen.replace(sLetterToGuess,"");
        lettersNotYetChosen.setText(sLettersNotYetChosen);
    }

    public void saveStatistics(int puzzleId,String username, int prize, String tournamentName){
        //save username, puzzleid, prize
        db.saveStatistics(puzzleId,username,prize, tournamentName);
    }

    public void onBackPressed(){
        AlertDialog.Builder areyousure = new AlertDialog.Builder(this);
        areyousure.setMessage("Are you sure you want to leave?");
        areyousure.setCancelable(true);

        areyousure.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Button solve = findViewById(R.id.solvePuzzle);
                        if(solve.isEnabled()) {
                            saveStatistics(PUZZLE_ID,USERNAME,0, currentTournamentName);
                        }
                        Intent exitIntent = new Intent(SolvePuzzle.this, MenuActivity.class);
                        startActivity(exitIntent);
                    }
                });

        areyousure.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = areyousure.create();
        alert11.show();
    }

    public void disableButtons(){
        Button buyVowel = findViewById(R.id.buyVowel);
        Button guessConsonant = findViewById(R.id.guessConsonant);
        Button solve = findViewById(R.id.solvePuzzle);

        buyVowel.setEnabled(false);
        guessConsonant.setEnabled(false);
        solve.setEnabled(false);
    }

    public void enableButtons(){
        Button buyVowel = findViewById(R.id.buyVowel);
        Button guessConsonant = findViewById(R.id.guessConsonant);
        Button solve = findViewById(R.id.solvePuzzle);

        buyVowel.setEnabled(true);
        guessConsonant.setEnabled(true);
        solve.setEnabled(true);
    }
}
