package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class NetworkDatabaseHelper extends DatabaseHelper
{
    public static final String TABLE = "networks";
    public static final String COLUMN_SSID = "ssid";
    public static final int COLUMN_SSID_INDEX = 1;

    private static final String CREATE_SCRIPT = "create table "
            + TABLE + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SSID + " text not null);";

    public NetworkDatabaseHelper(Context context)
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

    public long insert(NetworkData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SSID, data.getSSID());

        return insert(null, values);
    }

    public void update(NetworkData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SSID, data.getSSID());

        update(values, COLUMN_ID + "=" + Integer.toString(data.getId()), null);
    }
}
