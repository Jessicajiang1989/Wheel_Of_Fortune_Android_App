package edu.gatech.seclass.sdpguessit;

/**
 * Created by Micar on 2/25/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DbHelper extends SQLiteOpenHelper {
    private static final String LOGTAG = "WHEELOFFORTUNE_DATABASE";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SDPGuessIt.db";
    private static final String TABLE_NAME1 = "tPuzzle";
    private static final String TABLE_NAME2 = "player";
    private static final String TABLE_NAME3 = "tTournament";
    private static final String TABLE_NAME4 = "tPuzzleStatistics";
    private static final String TABLE_NAME5 = "tPuzzlesInTournament";
    private static final String TABLE_NAME6 = "TournamentStatistics";

    private static final String SELECT_EVERYTHING_PLAYER = "SELECT * FROM player;";
    private static final String SELECT_EVERYTHING_TOURNAMENT = "SELECT * FROM tTournament;";
    private static final String SELECT_EVERYTHING_PUZZLE = "SELECT * FROM tPuzzle;";
    private static final String SELECT_EVERYTHING_PUZZLES_IN_TOUR = "SELECT * FROM tPuzzlesInTournament;";
    private static final String SELECT_EVERYTHING_PUZZLE_STATISTICS = "SELECT * FROM tPuzzleStatistics;";

    SQLiteDatabase db;//set it to global
    //store puzzles in table tPuzzle(id primary key, phrase, maxnumber of wrong guess, username)
    private static final String TABLE_tPuzzle_CREATE = "create table IF NOT EXISTS tPuzzle(id integer primary key ,phrase text not null ,maxNumOfAllowedGuesses int not null, username text not null);";
    //player(first name, last name, username primary key, email)
    private static final String CREATE_PLAYER_TABLE = "CREATE TABLE IF NOT EXISTS player (" + "FirstName varchar(32)," + "LastName varchar(32)," + "UserName varchar(32)," + "Email varchar(32)," + "primary key (UserName)" + ");";
    //tTournament(tournament name, username)
    private static final String TABLE_CREATE_TOURNAMENT = "create table IF NOT EXISTS tTournament( tournamentName text primary key not null, username text not null);";
    //tPuzzlesInTournament(tournamentName, phrase id)
    private static final String TABLE_CREATE_PuzzlesInTournament = "create table IF NOT EXISTS tPuzzlesInTournament(tournamentName text not null ,phraseId integer not null);";
    //tpuzzlestatistics(puzzleID, username, prize)
    private static final String TABLE_CREATE_PUZZLE_STATISTICS = "create table tPuzzleStatistics(puzzleId integer not null, username text not null, prize int, tournamentName text, primary key(puzzleId,username,tournamentName));";
    //TournamentStatistics(tournamentName,username,totalPrize)
    private static final String TABLE_CREATE_TOURNAMENT_STATISTICS = "create table TournamentStatistics(tournamentName text not null,username text not null, totalPrize int, primary key(tournamentName,username));";

    //this is the constructor
    //this is why all the java file can share the same DATABASE
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //onCreate is for creating database tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        String query3 = "DROP TABLE IF EXISTS " + TABLE_NAME3;
        String query4 = "DROP TABLE IF EXISTS " + TABLE_NAME4;
        String query5 = "DROP TABLE IF EXISTS " + TABLE_NAME5;

        db.execSQL(TABLE_tPuzzle_CREATE);
        db.execSQL(CREATE_PLAYER_TABLE);
        db.execSQL(TABLE_CREATE_TOURNAMENT);
        db.execSQL(TABLE_CREATE_PuzzlesInTournament);
        db.execSQL(TABLE_CREATE_PUZZLE_STATISTICS);
        db.execSQL(TABLE_CREATE_TOURNAMENT_STATISTICS);
        Log.i(LOGTAG, "CREATE PUZZLE, PLAYER, TOURNAMENT SUCCESSFULLY!");
        Log.i(LOGTAG, "CREATE PUZZLE, PLAYER, TOURNAMENT, PUZZLESINTOURNAMENT SUCCESSFULLY!");
        this.db = db;
    }

    //insert into Puzzle table
    public void insertPuzzle(String phrase, Integer maxNumOfAllowedGuesses, String username) {
        db = this.getWritableDatabase();
        String INSERT_PUZZLE_TABLE = "insert into tPuzzle(phrase,maxNumOfAllowedGuesses,username) values(\'" + phrase + "\'," + maxNumOfAllowedGuesses + ",\'" + username + "\');";
        db.execSQL(INSERT_PUZZLE_TABLE);
        Log.i(LOGTAG, "insert Puzzle successfully!");
    }

    public List puzzlesNotCreatedByPlayer() {
        db = this.getWritableDatabase();
        String SELECT_PUZZLE = "select distinct * from ( SELECT * FROM tPuzzle WHERE username IS \'" + GlobalVariables.getInstance().globalUserName
                + "' union all select t.* from tPuzzle t join tPuzzleStatistics p on t.id = p.puzzleId and p.username = \'"
                + GlobalVariables.getInstance().globalUserName + "\') q;"; //"SELECT * FROM tPuzzle WHERE username IS \'" + GlobalVariables.getInstance().globalUserName + "\';";

        Cursor cursor = db.rawQuery(SELECT_PUZZLE , null);

        List puzzleListId;
        puzzleListId = new ArrayList();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    // add the id into the puzzlelist

                    String phrase = cursor.getString(cursor.getColumnIndex("phrase"));

                    puzzleListId.add(id);
                    // move to next row
                } while (cursor.moveToNext());
            }
        }

        return puzzleListId;
    }

    public List tournamentsNotCreatedByPlayer() {
        db = this.getWritableDatabase();
        String SELECT_TOURNAMENTS = "SELECT * FROM tTournament WHERE username <> \'" + GlobalVariables.getInstance().globalUserName + "\';";

        Cursor cursor = db.rawQuery(SELECT_TOURNAMENTS , null);

        List tournamentListName;
        tournamentListName = new ArrayList();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    String name = cursor.getString(cursor.getColumnIndex("tournamentName"));
                    // add the id into the puzzlelist

                    tournamentListName.add(name);
                    // move to next row
                } while (cursor.moveToNext());
            }
        }

        return tournamentListName;
    }

    public List PuzzlesInTournament(String tournamentName) {
        db = this.getWritableDatabase();
        //return the other player created puzzle id in the specific tournament
        //we want to return puzzles in a tournament that the player has not yet played
        //we are using tPuzzleStatistics, with the username column and tournamentName column to determine
        //whether or not the user has played that puzzle in this particular tournament
        String SELECT_TOURNAMENTS = "select distinct tPuzzle.* " +
                "from tPuzzle " +
                "join tPuzzlesInTournament " +
                "on tpuzzle.id = tPuzzlesInTournament.phraseId " +
                "Left join tPuzzleStatistics on " +
                "tpuzzle.id = tPuzzleStatistics.puzzleId and tPuzzleStatistics.username = \'" + GlobalVariables.getInstance().globalUserName +
                "\' and tPuzzleStatistics.tournamentName = \'" + tournamentName + "\' WHERE tPuzzleStatistics.puzzleId IS null and " +
                "tPuzzle.username != \'" + GlobalVariables.getInstance().globalUserName + "\' and " +
                "tPuzzlesInTournament.tournamentName IS \'" +  tournamentName + "\';";

        Cursor cursor = db.rawQuery(SELECT_TOURNAMENTS , null);

        List tournamentListName;
        tournamentListName = new ArrayList();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    // add the id into the puzzlelist

                    tournamentListName.add(id);
                    // move to next row
                } while (cursor.moveToNext());
            }
        }

        return tournamentListName;
    }


    //insert values in player table
    public void insertPlayer(String firstname, String lastname, String username, String email) {
        Log.i(LOGTAG, "getWritableDatabase1");
        db = this.getWritableDatabase();
        Log.i(LOGTAG, "getWritableDatabase2");
        String INSERT_PLAYER_TABLE = "INSERT INTO player " + "(FirstName, LastName, UserName, Email) VALUES " + "(\'" + firstname + "\'" + ", \'" + lastname + "\'," + "\'" + username + "\'," + " \'" + email + "\');";
        db.execSQL(INSERT_PLAYER_TABLE);
        Log.i(LOGTAG, "getWritableDatabase3");

        showEverythingInPlayer();
    }

    //select the username from the player table
    public Boolean checkUserName(String username) {
        db = this.getReadableDatabase();
        String SELECT_USERNAME = "SELECT UserName FROM player WHERE UserName = \'" + username + "\';";
        Cursor cursor = db.rawQuery(SELECT_USERNAME, null);
        return cursor.moveToFirst();
    }

    //create a phrase and max number of wrong guess
    public ArrayList<String> selectRandom() {
        ArrayList<String> returnPhrase = new ArrayList();
        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tPuzzle p LEFT JOIN tPuzzleStatistics s on p.id = s.puzzleId and s.username = \'" + GlobalVariables.globalUserName + "\' WHERE p.username <>\'"+GlobalVariables.globalUserName+ "\' AND s.username is null ORDER BY RANDOM() LIMIT 1", null);
        //bookmark, need to join to tPuzzleStatistics and only show puzzles not completed by that player
        //need to not show puzzles created by that player

        if (cursor != null) {
            cursor.moveToFirst();
            returnPhrase.add(0, cursor.getString(0));
            returnPhrase.add(1, cursor.getString(1));
            returnPhrase.add(2, cursor.getString(2));
        }

        return returnPhrase;
    }

    public ArrayList<String> selectPuzzleId(Integer puzzleId) {
        ArrayList<String> returnPhrase = new ArrayList();
        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tPuzzle where id = '" + puzzleId + "\';" , null);
        //bookmark, need to join to tPuzzleStatistics and only show puzzles not completed by that player
        //need to not show puzzles created by that player
        //tPuzzle(id, phrase, maxnumber of wrong guess, username)

        if (cursor != null) {
            cursor.moveToFirst();
            returnPhrase.add(0, cursor.getString(0));
            returnPhrase.add(1, cursor.getString(1));
            returnPhrase.add(2, cursor.getString(2));
        }

        return returnPhrase;
    }

    public int selectid(){
        int id = 0;
        db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select id from tPuzzle ORDER BY id DESC LIMIT 1",null);
        if(c != null){
            c.moveToFirst();
            id = Integer.parseInt(c.getString(0));
        }
        return id;
    }

    // TOURNAMENT Insert

    public void insertInTournament(String tournamentName, String userName) {
        db = this.getWritableDatabase();
        String INSERT_STRING = "insert into tTournament(tournamentName, username) values(\'" + tournamentName + "\', \'" + userName + "\');";
        db.execSQL(INSERT_STRING);
        Log.i(LOGTAG, "insert Tournament successfully!");
    }

    public Boolean checkTournaMentNameDB(String tourName) {
        db = this.getWritableDatabase();
        String SELECT_USERNAME = "SELECT tournamentName FROM tTournament WHERE tournamentName = \'" + tourName + "\';";
        Cursor cursor = db.rawQuery(SELECT_USERNAME, null);
        return cursor.moveToFirst();
    }


    public void insertPuzzlesInTournament(String tournamentName, Integer phraseId) {
        db = this.getWritableDatabase();
        String INSERT_STRING = "insert into tPuzzlesInTournament(tournamentName, phraseId) values(\'" + tournamentName + "\', \'" + phraseId + "\');";
        db.execSQL(INSERT_STRING);
        Log.i(LOGTAG, "insert puzzles in tournament successfully!");
    }

    // Show all Contents Databases
    public void showEverythingInTournament() {
        Cursor showeverything = db.rawQuery(SELECT_EVERYTHING_TOURNAMENT, null);
        Log.v("Cursor_showeverything", DatabaseUtils.dumpCursorToString(showeverything));
    }

    public void showEverythingInPlayer() {
        Cursor showeverything = db.rawQuery(SELECT_EVERYTHING_PLAYER, null);
        Log.v("Cursor_showeverything", DatabaseUtils.dumpCursorToString(showeverything));
    }

    public void showEverythingInPuzzle() {
        Cursor showeverything = db.rawQuery(SELECT_EVERYTHING_PUZZLE, null);
        Log.v("Cursor_showeverything", DatabaseUtils.dumpCursorToString(showeverything));
    }

    public void showEverythingInPuzzlesInTournament() {
        Cursor showeverything = db.rawQuery(SELECT_EVERYTHING_PUZZLES_IN_TOUR, null);
        Log.v("Cursor_showeverything", DatabaseUtils.dumpCursorToString(showeverything));
    }

    //statistics
    //puzzle ID, username, prize
    public void saveStatistics(int puzzleId, String username, int prize, String tournamentName){
        db = this.getWritableDatabase();
        String INSERT_STRING = "insert into tPuzzleStatistics(puzzleId,username,prize,tournamentName)  values('" + puzzleId + "\'," + "\'" + GlobalVariables.globalUserName + "\'" + ",\'" + prize + "\'," + "\'" + tournamentName + "\');";
        db.execSQL(INSERT_STRING);
        Log.i(LOGTAG,"Statistics Saved!");
    }

    public ArrayList<String> viewAllPuzzleStatistics(){
        ArrayList<String> statistics = new ArrayList<String>();
        //String statistics = "Puzzle ID|Number of Users|Max Prize";
        String QUERY = "select puzzleId, count(username) as countofUsers,max(prize) as maxPrize, (select username from tPuzzleStatistics where prize = (select max(prize) from tPuzzleStatistics where puzzleId = t.puzzleId)) as username from tPuzzleStatistics t group by puzzleId order by puzzleId";
        //bookmark, this currently does not show the username of the max prize, phase 2
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY,null);
        statistics.add("PuzzleID");
        statistics.add("PlayerCount");
        statistics.add("TopPrize");
        statistics.add("TopUser");
        if(c != null){
            while (c.moveToNext()) {
                statistics.add(c.getString(0));
                statistics.add(c.getString(1));
                statistics.add(c.getString(2));
                statistics.add(c.getString(3));
            }
        }
        return statistics;
    }

    public void Compute_Total_Prize_Tournament(String tournaMentName, String username)
    {
        //TournamentStatistics(tournamentName,username,totalPrize)
        //tpuzzlestatistics(puzzleID, username, prize)
    /*
    String get_each_puzzle_prize_in_one_tournament = "SELECT tPuzzlesInTournament.phraseId,tPuzzleStatistics.prize,tPuzzleStatistics.username,tPuzzlesInTournament.tournamentName " +
            "FROM tPuzzlesInTournament INNER JOIN tPuzzleStatistics ON tPuzzlesInTournament.phraseId = tPuzzleStatistics.puzzleId" +
            " WHERE tPuzzleStatistics.username = " + "\'" + username + "\'" + " AND " +"tPuzzlesInTournament.tournamentName = " + "\'" + tournaMentName + "\'";
    */
        if (tournaMentName != null)
        {
            String check_row = "SELECT * FROM TournamentStatistics " + "Where username = " + "\'" + username + "\'" + " AND " + "tournamentName = " + "\'" + tournaMentName + "\'";
            String check_all = "SELECT * FROM TournamentStatistics ";
            String check_tPuzzleStatistics = "SELECT * FROM tPuzzleStatistics ";
            String get_each_puzzle_prize_in_one_tournament = "SELECT * FROM tPuzzleStatistics " + "Where username = " + "\'" + username + "\'" + " AND " + "tournamentName = " + "\'" + tournaMentName + "\'";
            db = this.getWritableDatabase();
            System.out.println(get_each_puzzle_prize_in_one_tournament);

            Cursor c = db.rawQuery(get_each_puzzle_prize_in_one_tournament,null);
            Cursor check_duplicate = db.rawQuery(check_row,null);
            Cursor check_a = db.rawQuery(check_all,null);
            Cursor check_t = db.rawQuery(check_tPuzzleStatistics,null);
            Log.v("C==TournamentStatistics", DatabaseUtils.dumpCursorToString(c));

            int tournamentPrize = 0;
            if(c != null){
                while (c.moveToNext()) {
                    String puzzle_id = c.getString(0);
                    String puzzle_prize = c.getString(2);
                    Log.v("puzzle_prize==", puzzle_prize);
                    if (puzzle_prize.isEmpty() || puzzle_prize.length() == 0)
                        puzzle_prize = "0";
                    int each_puzzle_prize = Integer.parseInt(puzzle_prize);
                    tournamentPrize = tournamentPrize + each_puzzle_prize;
                    Log.i("each_puzzle_prize",Integer.toString(each_puzzle_prize));
                    Log.i("tournamentPrize",Integer.toString(tournamentPrize));
                }
            }
            String INSERT_TOUTNAMENT_STATISTICS_TABLE = "insert into TournamentStatistics(tournamentName,username,totalPrize) values(\'" + tournaMentName + "\'," + "\'" + username + "\',\'" + tournamentPrize + "\');";
            Log.i(LOGTAG,INSERT_TOUTNAMENT_STATISTICS_TABLE);
            //insert into TournamentStatistics
            //check whether the same data was in the database or not
            Log.v("check_duplicate:===1==", DatabaseUtils.dumpCursorToString(check_duplicate));

            if(check_duplicate.moveToNext() == false)
            {
                Log.v("check_duplicate:==2===", DatabaseUtils.dumpCursorToString(check_duplicate));
                Log.i("check_duplicate===3====", check_duplicate.toString());

                db.execSQL(INSERT_TOUTNAMENT_STATISTICS_TABLE);
                Log.i(LOGTAG,"INSERT_TOUTNAMENT_STATISTICS_TABLE Saved!");

            }

            Log.v("ALLtPuzzleStatistics", DatabaseUtils.dumpCursorToString(check_t));
            Log.v("ALLTOUTNAMENTSTATISTICS", DatabaseUtils.dumpCursorToString(check_a));

        }



    }


    public ArrayList<String> getTournamentStatistics(){
        ArrayList<String> statistics = new ArrayList<String>();
        //bookmark, need to get the username for the maxprize, not just max username
        String QUERY = "\n" +
                "select tournamentName,count(username), max(totalPrize) as TopPrize, (select username from TournamentStatistics where totalPrize = (select max(totalPrize) from TournamentStatistics where tournamentName = t.tournamentName)) from TournamentStatistics t group by tournamentName order by tournamentName";
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY,null);
        statistics.add("Name");
        statistics.add("PlayerCount");
        statistics.add("TopPrize");
        statistics.add("TopUser");

        if(c != null){
            while (c.moveToNext()) {
                statistics.add(c.getString(0));
                statistics.add(c.getString(1));
                statistics.add(c.getString(2));
                statistics.add(c.getString(3));
            }
        }
        return statistics;
    }

    public ArrayList<String> getPlayerTournamentStatistics(){
        ArrayList<String> statistics = new ArrayList<String>();
        //bookmark, need to get the username for the maxprize, not just max username
        String QUERY = "select tournamentName,username, totalPrize from TournamentStatistics t WHERE username = \'" + GlobalVariables.globalUserName + "\'group by tournamentName order by tournamentName";
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY,null);
        statistics.add("Tournament");
        statistics.add("User");
        statistics.add("Prize");

        if(c != null){
            while (c.moveToNext()) {
                statistics.add(c.getString(0));
                statistics.add(c.getString(1));
                statistics.add(c.getString(2));
            }
        }
        return statistics;
    }

    public ArrayList<String> viewPlayerStatistics(){
        ArrayList<String> statistics = new ArrayList<String>();
        String QUERY = "select puzzleId, prize from tPuzzleStatistics t WHERE username = \'" + GlobalVariables.globalUserName + "\' group by puzzleId order by puzzleId";
        //bookmark, this currently does not show the username of the max prize, phase 2
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY,null);
        statistics.add("PuzzleID");
        statistics.add("Prize");
        if(c != null){
            while (c.moveToNext()) {
                statistics.add(c.getString(0));
                statistics.add(c.getString(1));
            }
        }
        return statistics;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        String query3 = "DROP TABLE IF EXISTS " + TABLE_NAME3;
        String query4 = "DROP TABLE IF EXISTS " + TABLE_NAME4;
        String query5 = "DROP TABLE IF EXISTS " + TABLE_NAME5;
        String query6 = "DROP TABLE IF EXISTS" + TABLE_NAME6;

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);


        this.onCreate(db);
    }
}
