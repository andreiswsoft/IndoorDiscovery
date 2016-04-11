package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AccessPointDatabaseHelper extends DatabaseHelper
{
    public static final String TABLE = "accesspoints";
    public static final String COLUMN_NAME = "name";
    public static final int COLUMN_NAME_INDEX = 1;

    private static final String CREATE_SCRIPT = "create table "
            + TABLE + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null);";

    public AccessPointDatabaseHelper(Context context)
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

    public void insert(AccessPointData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, data.getName());

        insert(null, values);
    }

    public void update(AccessPointData data)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, data.getName());

        update(values, COLUMN_ID + "=" + Integer.toString(data.getId()), null);
    }
}
