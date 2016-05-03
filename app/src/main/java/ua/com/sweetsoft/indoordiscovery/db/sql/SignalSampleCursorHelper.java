package ua.com.sweetsoft.indoordiscovery.db.sql;

import android.database.Cursor;

import java.sql.Timestamp;

import ua.com.sweetsoft.indoordiscovery.db.Config;
import ua.com.sweetsoft.indoordiscovery.db.sql.CursorHelper;

public class SignalSampleCursorHelper extends CursorHelper
{
    public SignalSampleCursorHelper(Cursor cursor)
    {
        super(cursor);
    }

    public int getNetworkId()
    {
        int id = 0;
        if (m_cursor != null)
        {
            id = m_cursor.getInt(Config.COLUMN_SIGNALSAMPLE_NETWORK_ID_INDEX);
        }
        return id;
    }

    public int getLevel()
    {
        int level = 0;
        if (m_cursor != null)
        {
            level = m_cursor.getInt(Config.COLUMN_SIGNALSAMPLE_LEVEL_INDEX);
        }
        return level;
    }

    public Timestamp getTimestamp()
    {
        Timestamp timestamp = null;
        if (m_cursor != null)
        {
            timestamp = new Timestamp(m_cursor.getLong(Config.COLUMN_SIGNALSAMPLE_TIME_INDEX));
        }
        return timestamp;
    }
}
