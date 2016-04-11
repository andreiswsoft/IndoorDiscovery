package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SignalLevelDatabaseHelper extends DatabaseHelper
{
    public static final String TABLE = "levels";
    public static final String COLUMN_ACCESS_POINT_ID = "accesspoint";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final int COLUMN_ACCESS_POINT_ID_INDEX = 1;
    public static final int COLUMN_LEVEL_INDEX = 2;
    public static final int COLUMN_TIMESTAMP_INDEX = 3;

    private static final String CREATE_SCRIPT = "create table "
            + TABLE + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ACCESS_POINT_ID + " integer not null, "
            + COLUMN_LEVEL + " integer not null, "
            + COLUMN_TIMESTAMP + " timestamp not null, "
            + "FOREIGN KEY(" + COLUMN_ACCESS_POINT_ID + ") REFERENCES " + AccessPointDatabaseHelper.TABLE + "(" + COLUMN_ID + "));";

    public SignalLevelDatabaseHelper(Context context)
    {
        super(context);
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL(CREATE_SCRIPT);
    }

    public static void delete(SQLiteDatabase db)
    {
        db.execSQL(DELETE_SCRIPT + TABLE);
    }

    @Override
    protected String getTableName()
    {
        return TABLE;
    }

    public void insert(SignalLevelData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCESS_POINT_ID, data.getAccessPointId());
        values.put(COLUMN_LEVEL, data.getLevel());
        values.put(COLUMN_TIMESTAMP, data.getTimestamp().toString());

        insert(null, values);
    }

    public void update(SignalLevelData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCESS_POINT_ID, data.getAccessPointId());
        values.put(COLUMN_LEVEL, data.getLevel());
        values.put(COLUMN_TIMESTAMP, data.getTimestamp().toString());

        update(values, COLUMN_ID + "=" + Integer.toString(data.getId()), null);
    }
}
