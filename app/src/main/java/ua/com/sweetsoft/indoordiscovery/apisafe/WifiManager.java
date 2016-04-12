package ua.com.sweetsoft.indoordiscovery.apisafe;

import android.os.Build;

import ua.com.sweetsoft.indoordiscovery.common.Information;

public class WifiManager
{
    public static boolean isScanAvailable(android.net.wifi.WifiManager manager)
    {
        boolean available = false;

        if (manager != null)
        {
            if (manager.isWifiEnabled())
            {
                available = true;
            }
            else if (Information.getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
                available = manager.isScanAlwaysAvailable();
            }
        }

        return available;
    }
}
