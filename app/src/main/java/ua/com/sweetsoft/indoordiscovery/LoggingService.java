package ua.com.sweetsoft.indoordiscovery;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Handler;

import ua.com.sweetsoft.indoordiscovery.apisafe.HandlerThread;

public class LoggingService extends Service
{
    private final LoggingServiceMessenger m_messenger;
    private HandlerThread m_handlerThread;
    private LoggingServiceReceiver m_receiver = null;

    public LoggingService()
    {
        m_messenger = new LoggingServiceMessenger(this);
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

        m_handlerThread = new HandlerThread("receiver", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        m_handlerThread.start();

        startReceiver();
    }

    @Override
    public void onDestroy()
    {
        stopReceiver();

        m_handlerThread.quitSafely();

        super.onDestroy();
    }

    public void handleMessage(LoggingServiceMessenger.MessageCode mc, int arg1, int arg2)
    {
        switch (mc)
        {
            case Update:
                UpdateReceiver();
                break;
        }
    }

    private void startReceiver()
    {
        m_receiver = new LoggingServiceReceiver();
        registerReceiver(m_receiver, new IntentFilter("android.intent.action.TIME_TICK"), null, new Handler(m_handlerThread.getLooper()));
    }

    private void stopReceiver()
    {
        while (m_receiver != null)
        {
            unregisterReceiver(m_receiver);
            m_receiver = null;
        }
    }

    private void UpdateReceiver()
    {

    }
}
