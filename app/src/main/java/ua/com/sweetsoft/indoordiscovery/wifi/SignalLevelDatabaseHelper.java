package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SignalLevelDatabaseHelper extends DatabaseHelper
{
    public static final String TABLE = "levels";
    public static final String COLUMN_NETWORK_ID = "network";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final int COLUMN_NETWORK_ID_INDEX = 1;
    public static final int COLUMN_LEVEL_INDEX = 2;
    public static final int COLUMN_TIMESTAMP_INDEX = 3;

    private static final String CREATE_SCRIPT = "create table "
            + TABLE + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NETWORK_ID + " integer not null, "
            + COLUMN_LEVEL + " integer not null, "
            + COLUMN_TIMESTAMP + " timestamp not null, "
            + "FOREIGN KEY(" + COLUMN_NETWORK_ID + ") REFERENCES " + NetworkDatabaseHelper.TABLE + "(" + COLUMN_ID + "));";

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

    public long insert(SignalLevelData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NETWORK_ID, data.getNetworkId());
        values.put(COLUMN_LEVEL, data.getLevel());
        values.put(COLUMN_TIMESTAMP, data.getTimestamp().toString());

        return insert(null, values);
    }

    public void update(SignalLevelData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NETWORK_ID, data.getNetworkId());
        values.put(COLUMN_LEVEL, data.getLevel());
        values.put(COLUMN_TIMESTAMP, data.getTimestamp().toString());

        update(values, COLUMN_ID + "=" + Integer.toString(data.getId()), null);
    }
}
