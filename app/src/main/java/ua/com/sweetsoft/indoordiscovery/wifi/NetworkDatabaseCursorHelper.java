package ua.com.sweetsoft.indoordiscovery.wifi;

import android.database.Cursor;

public class NetworkDatabaseCursorHelper extends DatabaseCursorHelper
{
    public NetworkDatabaseCursorHelper(Cursor cursor)
    {
        super(cursor);
    }

    public String getSSID()
    {
        String ssid = null;
        if (m_cursor != null)
        {
            ssid = m_cursor.getString(NetworkDatabaseHelper.COLUMN_SSID_INDEX);
        }
        return ssid;
    }
}
