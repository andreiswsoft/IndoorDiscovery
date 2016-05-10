package ua.com.sweetsoft.indoordiscovery;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;

import ua.com.sweetsoft.indoordiscovery.apisafe.HandlerThread;
import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class ScanService extends Service
{
    private final ScanServiceMessenger m_messenger;
    private HandlerThread m_handlerThread;
    private SettingsManager m_settingsManager;
    private Timer m_syncTimer = null;
    private final ScanSyncTimerTask m_syncTask;

    public ScanService()
    {
        m_messenger = new ScanServiceMessenger(this);
        m_syncTask = new ScanSyncTimerTask(this);

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

        m_settingsManager = SettingsManager.getInstance(this);
        if (m_settingsManager.isScannerOn())
        {
            startScan();
        }
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
            case ReceiveSetting:
                if (m_settingsManager.receiveSetting(arg1, arg2))
                {
                    UpdateScan();
                }
                break;
        }
    }

    private boolean isScannerOn()
    {
        return (m_syncTimer != null);
    }

    private void startScan()
    {
        m_syncTask.init();

        m_syncTimer = new Timer(true);
        m_syncTimer.scheduleAtFixedRate(m_syncTask, 0, m_settingsManager.getScanPeriod()*1000);
    }

    private void stopScan()
    {
        if (m_syncTimer != null)
        {
            m_syncTimer.cancel();
            m_syncTimer.purge();
            m_syncTimer = null;
        }

        m_syncTask.reset();
    }

    private void UpdateScan()
    {
        if (isScannerOn())
        {
            stopScan();
        }
        if (m_settingsManager.isScannerOn())
        {
            startScan();
        }
    }

}
