package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import ua.com.sweetsoft.indoordiscovery.ScanSyncTimerTask;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;

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
        if (intent.getAction().compareTo(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) == 0)
        {
            long time = getScanTime(context);
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            List<ScanResult> scanResults = manager.getScanResults();
            if (scanResults != null)
            {
                saveScanResults(scanResults, time);
            }
        }
        else
        {
            // debug is on
            generateScanResults(context);
        }
        super.onReceive(context, intent);
    }

    private void saveScanResults(List<ScanResult> scanResults, long time)
    {
        for (ScanResult result : scanResults)
        {
            Network network = addNetwork(new Network(result.SSID, result.BSSID));
            if (network != null)
            {
                addSignalSample(new SignalSample(network, result.level, time));
            }
        }
    }

}
