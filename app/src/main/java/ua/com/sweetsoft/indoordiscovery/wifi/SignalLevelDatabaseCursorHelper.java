package ua.com.sweetsoft.indoordiscovery.wifi;

import android.database.Cursor;

import java.sql.Timestamp;

public class SignalLevelDatabaseCursorHelper extends DatabaseCursorHelper
{
    public SignalLevelDatabaseCursorHelper(Cursor cursor)
    {
        super(cursor);
    }

    public int getNetworkId()
    {
        int id = 0;
        if (m_cursor != null)
        {
            id = m_cursor.getInt(SignalLevelDatabaseHelper.COLUMN_NETWORK_ID_INDEX);
        }
        return id;
    }

    public int getLevel()
    {
        int level = 0;
        if (m_cursor != null)
        {
            level = m_cursor.getInt(SignalLevelDatabaseHelper.COLUMN_LEVEL_INDEX);
        }
        return level;
    }

    public Timestamp getTimestamp()
    {
        Timestamp timestamp = null;
        if (m_cursor != null)
        {
            timestamp = Timestamp.valueOf(m_cursor.getString(SignalLevelDatabaseHelper.COLUMN_TIMESTAMP_INDEX));
        }
        return timestamp;
    }
}
