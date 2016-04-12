package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public abstract class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns
{
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final int COLUMN_ID_INDEX = 0;

    protected static final String DELETE_SCRIPT = "DROP TABLE IF EXISTS ";
    protected static final String CLEAN_SCRIPT = "DELETE FROM ";

    protected SQLiteDatabase m_database = null;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        NetworkDatabaseHelper.create(db);
        SignalLevelDatabaseHelper.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w("SQLite", "Upgrade from version " + oldVersion + " to version " + newVersion);

        NetworkDatabaseHelper.delete(db);
        SignalLevelDatabaseHelper.delete(db);

        NetworkDatabaseHelper.create(db);
        SignalLevelDatabaseHelper.create(db);
    }

    public boolean openForWrite() throws SQLException
    {
        close();
        m_database = getWritableDatabase();
        return (m_database != null);
    }

    public boolean openForRead() throws SQLException
    {
        close();
        m_database = getReadableDatabase();
        return (m_database != null);
    }

    @Override
    public synchronized void close()
    {
        if (m_database != null)
        {
            m_database.close();
            m_database = null;
        }
        super.close();
    }

    public synchronized void clean()
    {
        if (openForWrite())
        {
            m_database.execSQL(CLEAN_SCRIPT + getTableName());
            close();
        }
    }

    protected abstract String getTableName();

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) throws SQLException
    {
        Cursor cursor = null;
        if (m_database != null)
        {
            cursor = m_database.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy);
        }
        return cursor;
    }

    public long insert(String nullColumnHack, ContentValues values)
    {
        long rowId = -1;
        if (m_database != null)
        {
            rowId = m_database.insert(getTableName(), nullColumnHack, values);
        }
        return rowId;
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs)
    {
        int count = 0;
        if (m_database != null)
        {
            count = m_database.update(getTableName(), values, whereClause, whereArgs);
        }
        return count;
    }

    public int delete(String whereClause, String[] whereArgs)
    {
        int count = 0;
        if (m_database != null)
        {
            count = m_database.delete(getTableName(), whereClause, whereArgs);
        }
        return count;
    }
    public void delete(Data data)
    {
        delete(COLUMN_ID + "=" + Integer.toString(data.getId()), null);
    }
}
