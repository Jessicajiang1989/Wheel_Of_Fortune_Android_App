package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NoPuzzlesAvailable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_puzzles_available);
    }

    public void clickOk(View view){
        Intent exitIntent = new Intent(NoPuzzlesAvailable.this, MenuActivity.class);
        startActivity(exitIntent);
    }
}
