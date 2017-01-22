package dgapmipt.pda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "MainDB";
    public static String TABLE_CHAT = "chatHistory";
    public static String TABLE_NFC = "nfcStrings";

    // for chat table
    public static String KEY_ID = "_id";
    public static String KEY_MESSAGE = "message";
    public static String KEY_USER_SENDER = "userSender";
    public static String KEY_DATE = "date";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CHAT + " (" + KEY_ID + " integer primary key,"
                + KEY_MESSAGE + " text," + KEY_DATE + " integer," + KEY_USER_SENDER + " integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CHAT);

        onCreate(db);
    }
}
