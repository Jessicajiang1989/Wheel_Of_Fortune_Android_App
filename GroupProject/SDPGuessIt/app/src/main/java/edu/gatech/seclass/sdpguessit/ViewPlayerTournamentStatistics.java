package edu.gatech.seclass.sdpguessit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class ViewPlayerTournamentStatistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_player_tournament_statistics);

        DbHelper db = new DbHelper(this);

        GridView statistics = findViewById(R.id.tStatistics);
        ArrayList<String> puzzleStats = new ArrayList<String>();
        puzzleStats = db.getPlayerTournamentStatistics();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, puzzleStats);

        statistics.setAdapter(adapter);
    }
}
