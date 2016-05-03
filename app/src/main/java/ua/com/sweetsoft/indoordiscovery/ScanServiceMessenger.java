package ua.com.sweetsoft.indoordiscovery;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class ScanServiceMessenger extends Handler implements android.content.ServiceConnection
{
    public enum MessageCode
    {
        ReceiveSetting(1);

        private int m_i;

        MessageCode(int i)
        {
            m_i = i;
        }

        public int toInt()
        {
            return m_i;
        }

        public static MessageCode fromInt(int i)
        {
            for (MessageCode mc : MessageCode.values())
            {
                if (mc.toInt() == i)
                {
                    return mc;
                }
            }
            return null;
        }
    }

    private ScanService m_service = null;
    private Messenger m_messenger = null;

    public ScanServiceMessenger(ScanService service)
    {
        m_service = service;
        if (isHandler())
        {
            m_messenger = new Messenger(this);
        }
    }

    private boolean isHandler()
    {
        return (m_service != null);
    }

    @Override
    public void handleMessage(Message msg)
    {
        MessageCode mc = MessageCode.fromInt(msg.what);
        if (mc != null)
        {
            m_service.handleMessage(mc, msg.arg1, msg.arg2);
        }
        else
        {
            super.handleMessage(msg);
        }
    }

    public IBinder getBinder()
    {
        if (isHandler())
        {
            return m_messenger.getBinder();
        }
        return null;
    }

    @Override
    public void onServiceConnected(ComponentName className, IBinder service)
    {
        m_messenger = new Messenger(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName className)
    {
        m_messenger = null;
    }

    public void bind(Context context)
    {
        if (!isHandler())
        {
            context.bindService(new Intent(context, ScanService.class), this, Context.BIND_AUTO_CREATE);
        }
    }

    public void unbind(Context context)
    {
        if (!isHandler() && m_messenger != null)
        {
            context.unbindService(this);
        }
    }

    public void send(MessageCode mc, int arg1, int arg2)
    {
        if (!isHandler() && m_messenger != null)
        {
            Message msg = Message.obtain(null, mc.toInt(), arg1, arg2);
            try
            {
                m_messenger.send(msg);
            }
            catch (RemoteException ex)
            {
            }
        }
    }
}
