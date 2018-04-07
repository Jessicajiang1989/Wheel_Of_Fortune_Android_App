package edu.gatech.seclass.sdpguessit;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateTournament extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText tournamentName;
    private DbHelper db;
    private String[] myDataset;
    Set< String > selectedPuzzles = new HashSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

        tournamentName = (EditText) findViewById(R.id.tourName);

        db = new DbHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        // TODO: Get the all the puzzle ids from Puzzle table to show in the list which player can use to create a tournament
        List puzzlesNotCreatedByUser = db.puzzlesNotCreatedByPlayer();
        //the
        myDataset = new String[puzzlesNotCreatedByUser.size()];
        puzzlesNotCreatedByUser.toArray(myDataset);

        if (myDataset.length == 0) {
            String displayNoPuzzlesCreatedByPlayer = "Sorry, there are no puzzles created by you";
            Toast.makeText(this, displayNoPuzzlesCreatedByPlayer, Toast.LENGTH_LONG).show();
        }

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //store all selected puzzles


                        if (selectedPuzzles.contains(myDataset[position])) {
                            selectedPuzzles.remove(myDataset[position]);
                            Drawable d = view.getBackground();
                            view.setBackgroundColor(Color.LTGRAY);
                        } else {
                            view.setBackgroundColor(0xFF00FF00);
                            selectedPuzzles.add(myDataset[position]);
                        }

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    public void exitPressed(View view) {
        System.out.println("Exit a Tournament here");
        finish();
    }

    public void createTournamentClicked(View view) {
        System.out.println("create a Tournament here");

        boolean check = false;
        String tourName = tournamentName.getText().toString();

        if (tourName.length() < 2) {
            tournamentName.setError("Please make sure the length of Tournament is at least 2");
            check = true;
        }

        if (selectedPuzzles.isEmpty()) {
            check = true;
            Toast.makeText(CreateTournament.this, "No Puzzles Selected. Select Puzzle (Should turn GREEN)", Toast.LENGTH_LONG).show();
        }

        if (db.checkTournaMentNameDB(tourName))
        {
            check = true;
            tournamentName.setError("Duplicated Tournament Name!Please another one!");
        }

        if (check == false) {
            // TODO: We need player Id of the current player here
            for (String singlePuzzle : selectedPuzzles) {
                //tournament name , puzzle id
                db.insertPuzzlesInTournament(tourName, Integer.parseInt(singlePuzzle));
            }
            //tournament name, username
            db.insertInTournament(tourName, GlobalVariables.getInstance().globalUserName);
            //"SELECT * FROM tPuzzlesInTournament;"
            db.showEverythingInPuzzlesInTournament();
            //SELECT * FROM tTournament
            db.showEverythingInTournament();

            String display = "Tournament created: " + tourName;
            Toast.makeText(this, display, Toast.LENGTH_LONG).show();
        }
    }

    public void exitButtonPressed(View view) {
        finish();
    }
}
