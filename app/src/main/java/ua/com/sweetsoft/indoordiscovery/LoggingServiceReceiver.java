package ua.com.sweetsoft.indoordiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import ua.com.sweetsoft.indoordiscovery.wifi.ScanReceiver;

public class LoggingServiceReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
//      if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))

        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (ua.com.sweetsoft.indoordiscovery.apisafe.WifiManager.isScanAvailable(manager))
        {
            context.registerReceiver(new ScanReceiver(), new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            manager.startScan();
        }
    }

}
