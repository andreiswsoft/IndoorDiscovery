package ua.com.sweetsoft.indoordiscovery;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Handler;

import ua.com.sweetsoft.indoordiscovery.apisafe.HandlerThread;
import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.wifi.ScanReceiver;

public class ScanService extends Service
{
    private final ScanServiceMessenger m_messenger;
    private HandlerThread m_handlerThread;
    private ScanSyncReceiver m_syncReceiver = null;
    private ScanReceiver m_scanReceiver = null;

    public ScanService()
    {
        m_messenger = new ScanServiceMessenger(this);
        Logger.enable(true);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return m_messenger.getBinder();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        m_handlerThread = new HandlerThread("scanner", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        m_handlerThread.start();

        startScan();
    }

    @Override
    public void onDestroy()
    {
        stopScan();

        m_handlerThread.quitSafely();

        super.onDestroy();
    }

    public void handleMessage(ScanServiceMessenger.MessageCode mc, int arg1, int arg2)
    {
        switch (mc)
        {
            case Update:
                UpdateScan();
                break;
        }
    }

    private void startScan()
    {
        m_scanReceiver = new ScanReceiver();
        m_syncReceiver = new ScanSyncReceiver();
        if (true)
        {
            registerReceiver(m_scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            registerReceiver(m_syncReceiver, new IntentFilter("android.intent.action.TIME_TICK"), null, new Handler(m_handlerThread.getLooper()));
        }
        else
        {
            registerReceiver(m_scanReceiver, new IntentFilter("android.intent.action.TIME_TICK"), null, new Handler(m_handlerThread.getLooper()));
        }
    }

    private void stopScan()
    {
        if (m_syncReceiver != null)
        {
            unregisterReceiver(m_syncReceiver);
            m_syncReceiver = null;
        }
        if (m_scanReceiver != null)
        {
            unregisterReceiver(m_scanReceiver);
            m_scanReceiver = null;
        }
    }

    private void UpdateScan()
    {

    }
}
