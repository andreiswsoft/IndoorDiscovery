package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.common.ServiceMessageSender;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class ScanServiceStopper extends AppCompatActivity implements ServiceMessageSender.ISender
{
    private ServiceMessageSender m_messenger = null;
    private Handler m_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        m_messenger = new ServiceMessageSender(this);
        m_messenger.bind(this, ScanService.class);
    }

    @Override
    protected void onDestroy()
    {
        m_messenger.unbind(this);
        m_messenger = null;

        super.onDestroy();
    }

    @Override
    public void onServiceMessageReceiverConnected()
    {
        SettingsManager settingsManager = SettingsManager.checkInstance();
        if (settingsManager != null)
        {
            settingsManager.unbindFromService();
        }

        Message message = Message.obtain(null, ScanService.MessageCode.StopService.toInt(), 0, 0);
        m_messenger.send(message);
        finish();
    }

}
