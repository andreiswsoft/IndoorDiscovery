package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import ua.com.sweetsoft.indoordiscovery.common.Logger;

public class ScanReceiver extends BroadcastReceiver
{
    NetworkDatabaseHelper m_networkDatabaseHelper = null;
    SignalLevelDatabaseHelper m_signalLevelDatabaseHelper = null;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        m_networkDatabaseHelper = new NetworkDatabaseHelper(context);
        if (m_networkDatabaseHelper.openForWrite())
        {
            m_signalLevelDatabaseHelper = new SignalLevelDatabaseHelper(context);
            if (m_signalLevelDatabaseHelper.openForWrite())
            {
                if (intent.getAction().compareTo(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) == 0)
                {
                    WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    saveScanResults(manager.getScanResults());
                }
                else
                {
                    // for debug purposes only
                    generateScanResults();
                }
                m_signalLevelDatabaseHelper.close();
            }
            m_networkDatabaseHelper.close();
        }
    }

    private void saveScanResults(List<ScanResult> scanResults)
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (ScanResult result : scanResults)
        {
            int networkId = addNetwork(result.SSID);
            if (networkId != 0)
            {
                addSignalLevel(networkId, result.level, timestamp);
            }
        }
    }

    private void generateScanResults()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Random random = new Random();
        for (int network = 1; network <= 5; network++)
        {
            String name = "network " + String.valueOf(network);
            int networkId = addNetwork(name);
            if (networkId != 0)
            {
                int level = random.nextInt(100);
                Logger.logInformation(name + " : " + String.valueOf(level));
                addSignalLevel(networkId, level, timestamp);
            }
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

    private void addSignalLevel(int networkId, int level, Timestamp timestamp)
    {
        m_signalLevelDatabaseHelper.insert(new SignalLevelData(0, networkId, level, timestamp));
    }
}
