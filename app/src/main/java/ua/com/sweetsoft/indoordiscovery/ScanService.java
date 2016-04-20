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
    private boolean m_scannerOn;
    private int m_scanPeriod;
    private ScanSyncTimerTask m_syncTask = null;
    private Timer m_syncTimer = null;

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

        m_settingsManager = new SettingsManager(this);
        m_scannerOn = m_settingsManager.isScannerOn();
        m_scanPeriod = m_settingsManager.getScanPeriod();
        if (m_scannerOn)
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
            case Update:
                if (setSetting(arg1, arg2))
                {
                    UpdateScan();
                }
                break;
        }
    }

    public boolean setSetting(int id, int value)
    {
        boolean set = false;
        switch (id)
        {
            case R.string.pref_key_scanner_switch:
                boolean scannerOn = (boolean)m_settingsManager.intToObject(id, value);
                if (m_scannerOn != scannerOn)
                {
                    m_scannerOn = scannerOn;
                    set = true;
                }
                break;
            case R.string.pref_key_scan_period:
                int scanPeriod = (int)m_settingsManager.intToObject(id, value);
                if (m_scanPeriod != scanPeriod)
                {
                    m_scanPeriod = scanPeriod;
                    set = true;
                }
                break;
        }
        return set;
    }

    private boolean isScannerOn()
    {
        return (m_syncTask != null);
    }

    private void startScan()
    {
        m_syncTask = new ScanSyncTimerTask(this);
        m_syncTimer = new Timer(true);
        m_syncTimer.scheduleAtFixedRate(m_syncTask, 0, m_scanPeriod*1000);
    }

    private void stopScan()
    {
        if (m_syncTimer != null)
        {
            m_syncTimer.cancel();
            m_syncTimer.purge();
            m_syncTimer = null;
        }
        if (m_syncTask != null)
        {
            m_syncTask.reset();
            m_syncTask = null;
        }
    }

    private void UpdateScan()
    {
        if (isScannerOn())
        {
            stopScan();
        }
        if (m_scannerOn)
        {
            startScan();
        }
    }
}
