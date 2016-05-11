package ua.com.sweetsoft.indoordiscovery;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;

import ua.com.sweetsoft.indoordiscovery.apisafe.HandlerThread;
import ua.com.sweetsoft.indoordiscovery.common.Information;
import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;
import ua.com.sweetsoft.indoordiscovery.wifi.StateChangedReceiver;

public class ScanService extends Service implements Handler.Callback
{
    private final ScanServiceMessenger m_messenger;
    private HandlerThread m_handlerThread;
    private SettingsManager m_settingsManager;
    private StateChangedReceiver m_stateChangeReceiver;
    private Timer m_syncTimer = null;
    private ScanSyncTimerTask m_syncTask = null;

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

        m_settingsManager = SettingsManager.getInstance(this);

        Handler handler = new Handler(m_handlerThread.getLooper(), this);
        m_stateChangeReceiver = new StateChangedReceiver(handler);
        registerReceiver(m_stateChangeReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));

        checkedStartScan();
    }

    @Override
    public void onDestroy()
    {
        stopScan();

        unregisterReceiver(m_stateChangeReceiver);

        m_handlerThread.quitSafely();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        return ScanService.START_STICKY;
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        return handleMessage(ScanServiceMessenger.MessageCode.fromInt(msg.arg1), msg.arg2, 0);
    }

    public boolean handleMessage(ScanServiceMessenger.MessageCode mc, int arg1, int arg2)
    {
        boolean handled = true;

        switch (mc)
        {
            case ReceiveSetting:
                if (m_settingsManager.receiveSetting(arg1, arg2))
                {
                    UpdateScan();
                }
                break;
            case WiFiStateChanged:
                CheckWiFiState(arg1);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }

    private boolean isScannerStarted()
    {
        return (m_syncTimer != null);
    }

    private void checkedStartScan()
    {
        if (m_settingsManager.isScannerOn())
        {
            //if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI))
            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if (ua.com.sweetsoft.indoordiscovery.apisafe.WifiManager.isScanAvailable(manager))
            {
                startScan();
            }
        }
    }

    private void startScan()
    {
        if (!isScannerStarted())
        {
            m_syncTask = new ScanSyncTimerTask(this);
            m_syncTimer = new Timer(true);
            m_syncTimer.scheduleAtFixedRate(m_syncTask, 0, m_settingsManager.getScanPeriod() * 1000);
        }
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
        if (isScannerStarted())
        {
            stopScan();
        }
        checkedStartScan();
    }

    private void CheckWiFiState(int state)
    {
        switch (state)
        {
            case WifiManager.WIFI_STATE_DISABLED:
                if (isScannerStarted())
                {
                    stopScan();
                    alertWiFiDisabled();
                }
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                checkedStartScan();
                break;
            case WifiManager.WIFI_STATE_DISABLING:
            case WifiManager.WIFI_STATE_ENABLING:
                break;
        }
    }

    private void alertWiFiDisabled()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppTheme_ScanServiceTheme);
        alertDialogBuilder.setTitle(R.string.alert_title_scan);
        alertDialogBuilder.setMessage(R.string.alert_wifi_disabled);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        Window dialogWindow = alertDialog.getWindow();
        int type;
        if (Information.getApiLevel() >= 19)
        {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        else
        {
            type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        dialogWindow.setType(type);
        alertDialog.show();
    }

}
