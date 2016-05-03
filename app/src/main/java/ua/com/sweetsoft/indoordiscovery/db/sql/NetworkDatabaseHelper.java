package ua.com.sweetsoft.indoordiscovery.db.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ua.com.sweetsoft.indoordiscovery.db.Config;

public class NetworkDatabaseHelper extends DatabaseHelper
{
    private static final String CREATE_SCRIPT = "create table "
            + Config.TABLE_NETWORK + " ("
            + Config.COLUMN_ID + " integer primary key autoincrement, "
            + Config.COLUMN_NETWORK_SSID + " text not null);";

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
        db.execSQL(DELETE_SCRIPT + Config.TABLE_NETWORK);
    }

    @Override
    protected String getTableName()
    {
        return Config.TABLE_NETWORK;
    }

    public long insert(NetworkData data)
    {
        ContentValues values = new ContentValues();
        values.put(Config.COLUMN_NETWORK_SSID, data.getSSID());

        return insert(null, values);
    }

    public void update(NetworkData data)
    {
        ContentValues values = new ContentValues();
        values.put(Config.COLUMN_NETWORK_SSID, data.getSSID());

        update(values, Config.COLUMN_ID + "=" + Integer.toString(data.getId()), null);
    }
}
