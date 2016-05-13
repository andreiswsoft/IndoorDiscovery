package ua.com.sweetsoft.indoordiscovery.common;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;

public class ServiceMessageReceiver extends Handler
{
    public interface IReceiver
    {
        boolean handleMessage(Message msg);
    }

    private IReceiver m_receiver = null;
    private Messenger m_messenger = null;

    public ServiceMessageReceiver(@NonNull IReceiver receiver)
    {
        m_receiver = receiver;
        m_messenger = new Messenger(this);
    }

    public IBinder onBind(Intent intent)
    {
        return m_messenger.getBinder();
    }

    @Override
    public void handleMessage(Message msg)
    {
        m_receiver.handleMessage(msg);
    }

}
