package ua.com.sweetsoft.indoordiscovery.db.sql;

import android.database.Cursor;

import ua.com.sweetsoft.indoordiscovery.db.Config;

public class CursorHelper
{
    protected Cursor m_cursor;

    public CursorHelper(Cursor cursor)
    {
        m_cursor = cursor;
    }

    public void setCursor(Cursor cursor)
    {
        m_cursor = cursor;
    }

    public void close()
    {
        if (m_cursor != null)
        {
            m_cursor.close();
            m_cursor = null;
        }
    }

    public boolean moveToFirst()
    {
        return (m_cursor != null && m_cursor.moveToFirst());
    }
    public boolean moveToNext()
    {
        return (m_cursor != null && m_cursor.moveToNext());
    }

    public int getId()
    {
        int id = 0;
        if (m_cursor != null)
        {
            String _id = m_cursor.getString(Config.COLUMN_ID_INDEX);
            id = Integer.parseInt(_id);
        }
        return id;
    }
}
