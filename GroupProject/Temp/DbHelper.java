package edu.gatech.seclass.sdpguessit;

/**
 * Created by Micar on 2/25/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SDPGuessIt.db";
    private static final String TABLE_NAME = "tPuzzle";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PHRASE = "phrase";
    private static final String COLUMN_MAXGUESS = "maxNumOfAllowedGuesses";
    private static final String COLUMN_USERNAME = "username";

    SQLiteDatabase db;

    private static final String TABLE_CREATE = "create table tPuzzle(id integer primary key ,phrase text not null ,maxNumOfAllowedGuesses int not null, username text not null);";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }
    public void insertPuzzle(String phrase, Integer maxNumOfAllowedGuesses,String username){
        db = this.getWritableDatabase();
        String INSERT_STRING = "insert into tPuzzle(phrase,maxNumOfAllowedGuesses,username) values(\'"+phrase+"\',"+maxNumOfAllowedGuesses+",\'"+username+"\');";
        db.execSQL(INSERT_STRING);
    }

    public ArrayList<String> selectRandom(){
        ArrayList<String> returnPhrase = new ArrayList();
        db = this.getReadableDatabase();
        //long randomRow = db.getMaximumSize();
        //int rand = ThreadLocalRandom.current().nextInt(1,Integer.parseInt(randomRow));
        //String SELECT_STRING = "SELECT * FROM tPuzzle ORDER BY RANDOM() LIMIT 1";//later this should have a where clause to say WHERE username <> "user"
        Cursor cursor = db.rawQuery("SELECT * FROM tPuzzle ORDER BY RANDOM() LIMIT 1", null);
                //db.query(TABLE_NAME,new String[]{"phrase"},"row_id = ?",null,null,null,null,"1");
        if (cursor != null) {
            cursor.moveToFirst();
            returnPhrase.add(0,cursor.getString(1));
            returnPhrase.add(1,cursor.getString(2));
        }

        return returnPhrase;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
}
