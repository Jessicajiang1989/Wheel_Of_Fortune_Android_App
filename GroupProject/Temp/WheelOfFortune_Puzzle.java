package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WheelOfFortune_Puzzle extends AppCompatActivity {

    DbHelper db = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_solve_puzzle);

    }

    public void openActivity(View view){
        Intent intent = new Intent(WheelOfFortune_Puzzle.this, SolvePuzzle.class);
        startActivity(intent);
    }

    public void openCreatePuzzle(View view){
        Intent intent = new Intent(WheelOfFortune_Puzzle.this, CreateAPuzzle.class);
        startActivity(intent);
    }
}
