package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhaoxiaopo on 2018/5/14.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DB_NAME = "test.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DBValues.ValueColumns.TABLE_NAME + " (" + DBValues
                .ValueColumns.COLUMN_NAME_USER_NAME + " text," + DBValues.ValueColumns
                .COLUMN_NAME_USER_AGE + " integer, " + DBValues.ValueColumns.COLUMN_NAME_USER_SEX
                + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
