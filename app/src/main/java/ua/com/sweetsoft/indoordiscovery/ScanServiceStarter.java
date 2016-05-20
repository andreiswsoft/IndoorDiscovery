package ua.com.sweetsoft.indoordiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class ScanServiceStarter extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SettingsManager settingsManager = SettingsManager.getInstance(context);
        if (settingsManager.isScannerOn() || settingsManager.isDebugOn())
        {
            context.startService(new Intent(context, ScanService.class));
        }
    }
}
