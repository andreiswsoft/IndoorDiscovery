package ua.com.sweetsoft.indoordiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import ua.com.sweetsoft.indoordiscovery.wifi.ScanReceiver;

public class ScanSyncReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
//      if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))

        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (ua.com.sweetsoft.indoordiscovery.apisafe.WifiManager.isScanAvailable(manager))
        {
            manager.startScan();
        }
    }

}
