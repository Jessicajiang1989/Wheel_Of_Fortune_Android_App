package edu.gatech.seclass.sdpguessit;

/**
 * Created by Rohit on 3/2/18.
 */

public class GlobalVariables {

    private static GlobalVariables mInstance = null;

    public static String globalUserName;
    public static boolean inTournament = false;

    protected GlobalVariables() {
    }
    //why need synchronize? we don't need threads
    public static synchronized GlobalVariables getInstance() {
        if (null == mInstance) {
            mInstance = new GlobalVariables();
        }
        return mInstance;
    }
}
