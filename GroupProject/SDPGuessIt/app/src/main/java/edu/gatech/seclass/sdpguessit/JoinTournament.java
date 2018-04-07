package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JoinTournament extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DbHelper db;
    private String[] myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_tournament);

        db = new DbHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewJoinTournament);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        // TODO: Get the all the puzzle ids from Puzzle table to show in the list which player can use to create a tournament
        //puzzlesNotCreatedByUser : list of tournament names
        List puzzlesNotCreatedByUser = db.tournamentsNotCreatedByPlayer();
        myDataset = new String[puzzlesNotCreatedByUser.size()];
        //copy puzzlesNotCreatedByUser to myDataset!
        puzzlesNotCreatedByUser.toArray(myDataset);

        if (myDataset.length == 0) {
            String displayNoPuzzlesCreatedByPlayer = "Sorry, there are no tournaments created by other players";
            Toast.makeText(this, displayNoPuzzlesCreatedByPlayer, Toast.LENGTH_LONG).show();
        }

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String tournaMentName = myDataset[position];

                        Intent solveAPuzzle = new Intent(JoinTournament.this, SolvePuzzle.class);
                        //return the other player created puzzle id in the specific tournament
                        List puzzlesList = db.PuzzlesInTournament(tournaMentName);
                        String[] puzzlesArray = new String[puzzlesList.size()];
                        puzzlesList.toArray(puzzlesArray);
                        Log.i("puzzlesList length: ",Integer.toString(puzzlesArray.length));

                        //puzzlesList : puzzles ids
                        if (puzzlesArray.length > 0) {
                            //pass data puzzle id to solveAPuzzle activity
                            solveAPuzzle.putExtra( "kAllPuzzlesFromJoinTournament", puzzlesArray);
                            solveAPuzzle.putExtra( "kTournamentName", tournaMentName);
                            GlobalVariables.inTournament = true;
                            startActivity(solveAPuzzle);

                        } else {
                            Toast.makeText(JoinTournament.this, "No puzzles left in tournament to play. Exit", Toast.LENGTH_LONG).show();
                            db.Compute_Total_Prize_Tournament(tournaMentName,GlobalVariables.getInstance().globalUserName);

                        }

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void exitButtonPressed(View view) {
        finish();
    }
}
