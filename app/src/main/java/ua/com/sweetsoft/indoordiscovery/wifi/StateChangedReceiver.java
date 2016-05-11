package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import ua.com.sweetsoft.indoordiscovery.ScanServiceMessenger;

public class StateChangedReceiver extends BroadcastReceiver
{
    private Handler m_handler;

    public StateChangedReceiver()
    {
    }
    public StateChangedReceiver(Handler handler)
    {
        m_handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Message message = new Message();
        message.arg1 = ScanServiceMessenger.MessageCode.WiFiStateChanged.toInt();
        message.arg2 = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        m_handler.sendMessage(message);
    }
}
