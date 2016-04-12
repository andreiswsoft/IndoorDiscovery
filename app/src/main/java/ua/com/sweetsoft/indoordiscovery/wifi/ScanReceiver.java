package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.sql.Timestamp;
import java.util.List;

public class ScanReceiver extends BroadcastReceiver
{
    NetworkDatabaseHelper m_networkDatabaseHelper = null;
    SignalLevelDatabaseHelper m_signalLevelDatabaseHelper = null;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        m_networkDatabaseHelper = new NetworkDatabaseHelper(context);
        m_signalLevelDatabaseHelper = new SignalLevelDatabaseHelper(context);

        if (m_networkDatabaseHelper.openForWrite())
        {
            if (m_signalLevelDatabaseHelper.openForWrite())
            {
                WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                List<ScanResult> scanResults = manager.getScanResults();
                for (ScanResult result : scanResults)
                {
                    int networkId = addNetwork(result.SSID);
                    if (networkId != 0)
                    {
                        addSignalLevel(networkId, result.level);
                    }
                }
                m_signalLevelDatabaseHelper.close();
            }
            m_networkDatabaseHelper.close();
        }
    }

    private int addNetwork(String name)
    {
        NetworkDatabaseCursorHelper cursorHelper = new NetworkDatabaseCursorHelper();
        cursorHelper.setCursor(m_networkDatabaseHelper.query(new String[]{DatabaseHelper.COLUMN_ID}, NetworkDatabaseHelper.COLUMN_SSID + " = '" + name + "'", null, null, null, null));
        if (!cursorHelper.moveToData())
        {
            m_networkDatabaseHelper.insert(new NetworkData(0, name));
            cursorHelper.setCursor(m_networkDatabaseHelper.query(new String[]{DatabaseHelper.COLUMN_ID}, NetworkDatabaseHelper.COLUMN_SSID + " = '" + name + "'", null, null, null, null));
            cursorHelper.moveToData();
        }
        return cursorHelper.getId();
    }

    private void addSignalLevel(int networkId, int level)
    {
        m_signalLevelDatabaseHelper.insert(new SignalLevelData(0, networkId, level, new Timestamp(System.currentTimeMillis())));
    }
}
