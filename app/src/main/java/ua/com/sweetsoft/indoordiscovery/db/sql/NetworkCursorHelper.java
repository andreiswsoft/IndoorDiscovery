package ua.com.sweetsoft.indoordiscovery.db.sql;

import android.database.Cursor;

import ua.com.sweetsoft.indoordiscovery.db.Config;

public class NetworkCursorHelper extends CursorHelper
{
    public NetworkCursorHelper(Cursor cursor)
    {
        super(cursor);
    }

    public String getSSID()
    {
        String ssid = null;
        if (m_cursor != null)
        {
            ssid = m_cursor.getString(Config.COLUMN_NETWORK_SSID_INDEX);
        }
        return ssid;
    }

    public String getBSSID()
    {
        String bssid = null;
        if (m_cursor != null)
        {
            bssid = m_cursor.getString(Config.COLUMN_NETWORK_BSSID_INDEX);
        }
        return bssid;
    }
}
