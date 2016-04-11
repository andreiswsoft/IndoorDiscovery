package ua.com.sweetsoft.indoordiscovery.wifi;

import android.database.Cursor;

public class AccessPointDatabaseCursorHelper extends DatabaseCursorHelper
{
    public AccessPointDatabaseCursorHelper(Cursor cursor)
    {
        super(cursor);
    }

    public String getName()
    {
        String name = null;
        if (m_cursor != null)
        {
            name = m_cursor.getString(AccessPointDatabaseHelper.COLUMN_NAME_INDEX);
        }
        return name;
    }
}
