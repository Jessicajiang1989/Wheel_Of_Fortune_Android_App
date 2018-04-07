package edu.gatech.seclass.sdpguessit;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MenuActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void openCreatePuzzle(View view){
        Intent intent = new Intent(this, CreateAPuzzle.class);
        startActivity(intent);
    }

    public void openSolvePuzzle(View view){
        Intent intent = new Intent(this, SolvePuzzle.class);
        startActivity(intent);
    }

    public void createTournamentClicked(View view) {
        Intent createATourInent = new Intent(this, CreateTournament.class);
        startActivity(createATourInent);
    }

    public void joinOrContinueTournamentClicked(View view) {
        Intent joinOrContinueTournamentIn = new Intent(this, JoinTournament.class);
        startActivity(joinOrContinueTournamentIn);
    }

    public void viewStatistics(View view){
        Intent newPlayerIntent = new Intent(this, VIewStatistics.class);
        startActivity(newPlayerIntent);
    }

    public void exit(View view){
        Intent newintent = new Intent(this, WheelOfFortune.class);
        startActivity(newintent);
    }
}
