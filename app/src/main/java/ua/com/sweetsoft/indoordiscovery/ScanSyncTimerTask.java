package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import java.util.TimerTask;

import ua.com.sweetsoft.indoordiscovery.wifi.ScanReceiver;

public class ScanSyncTimerTask extends TimerTask
{
    private static final boolean debugMode = false;
    private static final String debugIntent = "ua.com.sweetsoft.indoordiscovery.SCAN_RESULTS";

    private boolean m_scanning = false;
    private boolean m_autorunScan = false;
    private Context m_context;
    private ScanReceiver m_scanReceiver;

    public ScanSyncTimerTask(Context context)
    {
        m_context = context;
        m_scanReceiver = new ScanReceiver(this);
        if (debugMode)
        {
            m_context.registerReceiver(m_scanReceiver, new IntentFilter(debugIntent));
        }
        else
        {
            m_context.registerReceiver(m_scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }
    }

    public void reset()
    {
        m_context.unregisterReceiver(m_scanReceiver);
    }

    @Override
    public void run()
    {
        beginScan();
    }

    private synchronized void beginScan()
    {
        if (!m_scanning)
        {
            m_autorunScan = false;

            if (debugMode)
            {
                m_context.sendBroadcast(new Intent(debugIntent));
            }
            else
            {
                WifiManager manager = (WifiManager) m_context.getSystemService(Context.WIFI_SERVICE);
                manager.startScan();
            }
            m_scanning = true;
        }
        else
        {
            m_autorunScan = true;
        }
    }

    public synchronized void endScan()
    {
        m_scanning = false;

        if (m_autorunScan)
        {
            beginScan();
        }
    }
}

