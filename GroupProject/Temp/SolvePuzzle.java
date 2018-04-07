package edu.gatech.seclass.sdpguessit;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.String;

public class SolvePuzzle extends AppCompatActivity {

    //Global Variables
    public static String DEFAULT_PHRASE = "GUESS ME!";
    public String phraseInProgress = DEFAULT_PHRASE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);
        TextView allowedGuesses = findViewById(R.id.tRemainingNumOfGuesses);
        ArrayList<String> dbvalues = new ArrayList();
        DbHelper db = new DbHelper(this);
        dbvalues = db.selectRandom();
        DEFAULT_PHRASE = dbvalues.get(0).toString();//put back when done testing, need to merge with Yuhao's db
        allowedGuesses.setText(dbvalues.get(1).toString());
        hidePhrase();
        setPrize();
    }

    //default the prize choices
    protected void setPrize(){
        int currentPrize = ThreadLocalRandom.current().nextInt(1,10)*100;
        TextView tCurrentPrize = findViewById(R.id.tCurrentPrize);
        TextView tTotalPrize = findViewById(R.id.tTotalPrize);

        tCurrentPrize.setText(Integer.toString(currentPrize));
        tTotalPrize.setText("0");
    }

    protected void increasePrize(String letter){
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
        }
        else {
            int numOfConsonants = countConsonants(letter);
            int newTotal = Integer.parseInt(tCurrentPrize.getText().toString())*numOfConsonants + Integer.parseInt(tTotalPrize.getText().toString());
            tCurrentPrize.setText(Integer.toString(currentPrize));
            tTotalPrize.setText(Integer.toString(newTotal));
        }

    }

    protected void hidePhrase(){
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
        int vowelPenalty = 300;
        TextView totalPrize = findViewById(R.id.tTotalPrize);
        int iTotalPrize = Integer.parseInt(totalPrize.getText().toString());
        EditText phrase = findViewById(R.id.tPhrase);
        String sPhrase = DEFAULT_PHRASE.toLowerCase();
        String vowels = "aeiou";
        //validate that the letter is a vowel, return message if not
        if(vowels.contains(sLetterToGuess) == false){
            letterToGuess.setError("To buy a vowel the letter to guess must be one of a,e,i,o,u");
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
        }
        else {
            //if no, subtract from guesses
            subtractGuesses();
        }
        hidePhrase();
    }

    public void guessAConsonant(View view){
        EditText letterToGuess = findViewById(R.id.tLetterToGuess);
        TextView lettersNotYetChosen = findViewById(R.id.tLettersNotChosen);
        EditText phrase = findViewById(R.id.tPhrase);
        String sLettersNotYetChosen = lettersNotYetChosen.getText().toString().toLowerCase();
        String sLetterToGuess = letterToGuess.getText().toString().toLowerCase();
        String sPhrase = DEFAULT_PHRASE.toLowerCase();
        System.out.println("MY DEFAULT PHRASE IS: "+DEFAULT_PHRASE);
        //check to make sure letter is a consonant
        //check to make sure the letter has not been guessed before
        if(sLettersNotYetChosen.contains(sLetterToGuess) == false){
            letterToGuess.setError("This letter has been chosen already. Please choose another.");
            return;
        }
        //remove letter from lettersNotYetChosen
        recordChosenLetters(sLetterToGuess);
        //check for whether the letter is in the phrase
        if(sPhrase.contains(sLetterToGuess)){
            //if yes add to prize
            increasePrize(sLetterToGuess);
        }
        else {
            //if no, subtract from guesses
            subtractGuesses();
        }

        hidePhrase();
    }

    protected void solvePuzzle(View view){
        EditText phrase = findViewById(R.id.tPhrase);
        String sPhrase = phrase.getText().toString().toLowerCase();
        TextView totalPrize = findViewById(R.id.tTotalPrize);

        //check to make sure the user didn't accidentally click solve puzzle
        if(sPhrase.contains("#")){
            phrase.setError("Please replace all occurances of # with a letter before solving the puzzle.");
            return;
        }
        if (sPhrase.equals(DEFAULT_PHRASE.toLowerCase())){
            increasePrize("WIN");
        }
        else{
            totalPrize.setText("0");
            phrase.setText("GAME OVER!");
            //set prize to 0 and end game
        }
    }

    protected void subtractGuesses(){
        //if end of guesses, end puzzle
        //else hide phrase
        //subtract from numofallowedwrong guesses
        TextView allowedGuesses = findViewById(R.id.tRemainingNumOfGuesses);
        int iAllowedGuesses = Integer.parseInt(allowedGuesses.getText().toString());
        iAllowedGuesses = iAllowedGuesses - 1;
        allowedGuesses.setText(Integer.toString(iAllowedGuesses));

    }

    protected int countConsonants(String letter){
        int oldPhraseSize = DEFAULT_PHRASE.length();
        String guessesRemoved = DEFAULT_PHRASE.replace(letter,"");
        int newPhraseSize = guessesRemoved.length();
        int numOfConsonants = oldPhraseSize - newPhraseSize;

        return numOfConsonants;
    }

    public void recordChosenLetters(String letter){
        EditText letterToGuess = findViewById(R.id.tLetterToGuess);
        TextView lettersNotYetChosen = findViewById(R.id.tLettersNotChosen);
        EditText phrase = findViewById(R.id.tPhrase);
        String sLettersNotYetChosen = lettersNotYetChosen.getText().toString().toLowerCase();
        String sLetterToGuess = letterToGuess.getText().toString().toLowerCase();
        String sPhrase = DEFAULT_PHRASE.toLowerCase();

        sLettersNotYetChosen = sLettersNotYetChosen.replace(sLetterToGuess,"");
        lettersNotYetChosen.setText(sLettersNotYetChosen);
    }
}
