package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VIewStatistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);
    }

    public void viewPlayerStatistics(View view){
        Intent newPlayerIntent = new Intent(this, ViewPlayerStatistics.class);
        startActivity(newPlayerIntent);
    }

    public void viewPuzzleStatistics(View view){
        Intent newPlayerIntent = new Intent(this, ViewPuzzleStatistics.class);
        startActivity(newPlayerIntent);
    }

    public void viewTournamentStatistics(View view){
        Intent newPlayerIntent = new Intent(this, ViewTournamentStatistics.class);
        startActivity(newPlayerIntent);
    }

    public void viewPlayerTournamentStatistics(View view){
        Intent newPlayerIntent = new Intent(this, ViewPlayerTournamentStatistics.class);
        startActivity(newPlayerIntent);
    }
}

