package ua.com.sweetsoft.indoordiscovery.db.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ua.com.sweetsoft.indoordiscovery.db.Config;

public class SignalSampleDatabaseHelper extends DatabaseHelper
{
    private static final String CREATE_SCRIPT = "create table "
            + Config.TABLE_SIGNALSAMPLE + " ("
            + Config.COLUMN_ID + " integer primary key autoincrement, "
            + Config.COLUMN_SIGNALSAMPLE_NETWORK_ID + " integer not null, "
            + Config.COLUMN_SIGNALSAMPLE_LEVEL + " integer not null, "
            + Config.COLUMN_SIGNALSAMPLE_TIME + " long not null, "
            + "FOREIGN KEY(" + Config.COLUMN_SIGNALSAMPLE_NETWORK_ID + ") REFERENCES " + Config.TABLE_NETWORK + "(" + Config.COLUMN_ID + "));";

    public SignalSampleDatabaseHelper(Context context)
    {
        super(context);
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL(CREATE_SCRIPT);
    }

    public static void delete(SQLiteDatabase db)
    {
        db.execSQL(DELETE_SCRIPT + Config.TABLE_SIGNALSAMPLE);
    }

    @Override
    protected String getTableName()
    {
        return Config.TABLE_SIGNALSAMPLE;
    }

    public long insert(SignalSampleData data)
    {
        ContentValues values = new ContentValues();
        values.put(Config.COLUMN_SIGNALSAMPLE_NETWORK_ID, data.getNetworkId());
        values.put(Config.COLUMN_SIGNALSAMPLE_LEVEL, data.getLevel());
        values.put(Config.COLUMN_SIGNALSAMPLE_TIME, data.getTime());

        return insert(null, values);
    }

    public void update(SignalSampleData data)
    {
        ContentValues values = new ContentValues();
        values.put(Config.COLUMN_SIGNALSAMPLE_NETWORK_ID, data.getNetworkId());
        values.put(Config.COLUMN_SIGNALSAMPLE_LEVEL, data.getLevel());
        values.put(Config.COLUMN_SIGNALSAMPLE_TIME, data.getTime());

        update(values, Config.COLUMN_ID + "=" + Integer.toString(data.getId()), null);
    }
}
