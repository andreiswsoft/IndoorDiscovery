package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import java.util.TimerTask;

import ua.com.sweetsoft.indoordiscovery.wifi.ScanReceiver;

public class ScanSyncTimerTask extends TimerTask
{
    private static final boolean debugMode = true;
    private static final String debugIntent = "ua.com.sweetsoft.indoordiscovery.SCAN_RESULTS";

    private Context m_context;
    private ScanReceiver m_scanReceiver;

    public ScanSyncTimerTask(Context context)
    {
        m_context = context;
        m_scanReceiver = new ScanReceiver();
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
        m_scanReceiver = null;
    }

    @Override
    public void run()
    {
        if (debugMode)
        {
            m_context.sendBroadcast(new Intent(debugIntent));
        }
        else
        {
    //      if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))
            WifiManager manager = (WifiManager) m_context.getSystemService(Context.WIFI_SERVICE);
            if (ua.com.sweetsoft.indoordiscovery.apisafe.WifiManager.isScanAvailable(manager))
            {
                manager.startScan();
            }
        }
    }
}

