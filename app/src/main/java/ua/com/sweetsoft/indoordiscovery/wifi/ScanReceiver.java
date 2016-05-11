package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;
import java.util.Random;

import ua.com.sweetsoft.indoordiscovery.ScanSyncTimerTask;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelperFactory;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.NetworkDao;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSampleDao;

public class ScanReceiver extends ua.com.sweetsoft.indoordiscovery.ScanReceiver
{
    public ScanReceiver()
    {
    }

    public ScanReceiver(ScanSyncTimerTask scanTask)
    {
        super(scanTask);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        long time = getScanTime(context);
        if (intent.getAction().compareTo(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) == 0)
        {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            List<ScanResult> scanResults = manager.getScanResults();
            if (scanResults != null)
            {
                saveScanResults(scanResults, time);
            }
        }
        else
        {
            // for debug purposes only
            generateScanResults(time);
        }
        super.onReceive(context, intent);
    }

    private void saveScanResults(List<ScanResult> scanResults, long time)
    {
        for (ScanResult result : scanResults)
        {
            Network network = addNetwork(new Network(result.SSID));
            if (network != null)
            {
                addSignalSample(new SignalSample(network, result.level, time));
            }
        }
    }

    private void generateScanResults(long time)
    {
        Random random = new Random();
        for (int cnt = 1; cnt <= 5; cnt++)
        {
            String name = "network " + String.valueOf(cnt);
            Network network = addNetwork(new Network(name));
            if (network != null)
            {
                int level = random.nextInt(100);
                addSignalSample(new SignalSample(network, level, time));
            }
        }
    }

    private Network addNetwork(Network net)
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

    private SignalSample addSignalSample(SignalSample sgnSample)
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
