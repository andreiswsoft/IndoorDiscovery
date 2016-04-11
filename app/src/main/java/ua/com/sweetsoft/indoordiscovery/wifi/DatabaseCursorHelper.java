package ua.com.sweetsoft.indoordiscovery.wifi;

import android.database.Cursor;

public class DatabaseCursorHelper
{
    protected Cursor m_cursor;

    public DatabaseCursorHelper(Cursor cursor)
    {
        m_cursor = cursor;
    }

    public void setCursor(Cursor cursor)
    {
        m_cursor = cursor;
    }

    public int getId()
    {
        int id = 0;
        if (m_cursor != null)
        {
            String _id = m_cursor.getString(DatabaseHelper.COLUMN_ID_INDEX);
            id = Integer.parseInt(_id);
        }
        return id;
    }
}
