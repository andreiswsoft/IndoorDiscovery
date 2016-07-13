package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.content.Intent;

public class LocatorReceiver extends WakefulReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent serviceIntent = new Intent(context, LocatorService.class);
        serviceIntent.fillIn(intent, Intent.FILL_IN_DATA);
        startWakefulService(context, serviceIntent);
    }
}