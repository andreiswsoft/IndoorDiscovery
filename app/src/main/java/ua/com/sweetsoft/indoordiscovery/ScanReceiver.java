package ua.com.sweetsoft.indoordiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class ScanReceiver extends BroadcastReceiver
{
    private static final String databaseUpdateIntent = "ua.com.sweetsoft.indoordiscovery.action.databaseUpdate";

    private ScanSyncTimerTask m_scanTask;

    protected ScanReceiver()
    {
        m_scanTask = null;
    }

    protected ScanReceiver(ScanSyncTimerTask scanTask)
    {
        m_scanTask = scanTask;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (m_scanTask != null)
        {
            m_scanTask.endScan();
        }

        context.sendBroadcast(new Intent(databaseUpdateIntent));
    }

    protected long getScanTime(Context context)
    {
        SettingsManager manager = SettingsManager.getInstance(context);
        long scanPeriod = manager.getScanPeriod();
        long scanTime = System.currentTimeMillis()/1000L;
        return (scanTime - (scanTime % scanPeriod));
    }

}
