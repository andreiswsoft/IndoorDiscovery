package ua.com.sweetsoft.indoordiscovery.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;

public class ServiceMessageSender implements android.content.ServiceConnection
{
    public interface ISender
    {
        void onServiceMessageReceiverConnected();
    }

    private ISender m_sender = null;
    private Messenger m_messenger = null;

    public ServiceMessageSender(@NonNull ISender sender)
    {
        m_sender = sender;
    }

    public void bind(Context context, Class<?> cls)
    {
        context.bindService(new Intent(context, cls), this, Context.BIND_AUTO_CREATE);
    }

    public void unbind(Context context)
    {
        if (m_messenger != null)
        {
            context.unbindService(this);
        }
    }

    public void send(Message msg)
    {
        if (m_messenger != null)
        {
            try
            {
                m_messenger.send(msg);
            }
            catch (RemoteException ex)
            {
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder service)
    {
        m_messenger = new Messenger(service);

        m_sender.onServiceMessageReceiverConnected();
    }

    @Override
    public void onServiceDisconnected(ComponentName className)
    {
        m_messenger = null;
    }

}
