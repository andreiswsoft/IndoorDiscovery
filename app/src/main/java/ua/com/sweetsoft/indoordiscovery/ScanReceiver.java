package ua.com.sweetsoft.indoordiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Random;

import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelperFactory;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.NetworkDao;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSampleDao;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class ScanReceiver extends BroadcastReceiver
{
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

        DatabaseUpdateAsyncTask task = new DatabaseUpdateAsyncTask(context.getApplicationContext());
        task.execute();
    }

    protected long getScanTime(Context context)
    {
        SettingsManager manager = SettingsManager.getInstance(context);
        long scanPeriod = manager.getScanPeriod();
        long scanTime = System.currentTimeMillis()/1000L;
        return (scanTime - (scanTime % scanPeriod));
    }

    protected void generateScanResults(Context context)
    {
        long time = getScanTime(context);
        SettingsManager settingsManager = SettingsManager.getInstance(context);
        int networkNumber = settingsManager.getDebugGeneratedNetworkNumber();
        Random random = new Random();
        for (int cnt = 1; cnt <= networkNumber; cnt++)
        {
            String ssid = "WiFi network with " + String.valueOf(cnt) + " Service Set Identifier";
            String bssid = String.valueOf(cnt);
            Network network = addNetwork(new Network(ssid, bssid));
            if (network != null)
            {
                int level = random.nextInt(100);
                addSignalSample(new SignalSample(network, level, time));
            }
        }
    }

    protected Network addNetwork(Network net)
    {
        Network network = null;

        NetworkDao networkDao = DatabaseHelperFactory.getHelper().getNetworkDao();
        if (networkDao != null)
        {
            network = networkDao.read(net);
            if (network == null)
            {
                network = networkDao.create(net);
            }
        }
        return network;
    }

    protected SignalSample addSignalSample(SignalSample sgnSample)
    {
        SignalSample signalSample = null;

        SignalSampleDao signalSampleDao = DatabaseHelperFactory.getHelper().getSignalSampleDao();
        if (signalSampleDao != null)
        {
            signalSample = signalSampleDao.create(sgnSample);
        }
        return signalSample;
    }

}
