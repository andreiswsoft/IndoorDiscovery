package ua.com.sweetsoft.indoordiscovery;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;

import ua.com.sweetsoft.indoordiscovery.apisafe.HandlerThread;
import ua.com.sweetsoft.indoordiscovery.common.Information;
import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.common.ServiceMessageReceiver;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;
import ua.com.sweetsoft.indoordiscovery.wifi.StateChangedReceiver;

public class ScanService extends Service implements Handler.Callback, ServiceMessageReceiver.IReceiver
{
    private static final int NOTIFICATION_ID = 1;
    private static final boolean debugMode = true;

    public enum MessageCode
    {
        ReceiveSetting(1),
        WiFiStateChanged(2),
        StopService(3);

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

    private final ServiceMessageReceiver m_messenger;
    private HandlerThread m_handlerThread;
    private SettingsManager m_settingsManager;
    private StateChangedReceiver m_stateChangeReceiver;
    private Timer m_syncTimer = null;
    private ScanSyncTimerTask m_syncTask = null;

    public ScanService()
    {
        m_messenger = new ServiceMessageReceiver(this);

        Logger.enable(true);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return m_messenger.onBind(intent);
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

        startForeground (NOTIFICATION_ID, createNotification());
    }

    @Override
    public void onDestroy()
    {
        stopScan();

        unregisterReceiver(m_stateChangeReceiver);

        m_settingsManager = null;

        m_handlerThread.quitSafely();

        super.onDestroy();
    }

/*    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        return ScanService.START_STICKY;
    }*/

    @Override
    public boolean handleMessage(Message msg)
    {
        boolean handled = true;

        switch (MessageCode.fromInt(msg.what))
        {
            case ReceiveSetting:
                if (m_settingsManager.receiveSetting(msg.arg1, msg.arg2))
                {
                    updateScan();
                }
                break;
            case WiFiStateChanged:
                if (!debugMode)
                {
                    checkWiFiState(msg.arg1);
                }
                break;
            case StopService:
                stopService();
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }

    private void stopService()
    {
        stopForeground(true);
        stopSelf();
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
            if (debugMode || ua.com.sweetsoft.indoordiscovery.apisafe.WifiManager.isScanAvailable(manager))
            {
                startScan();
            }
        }
    }

    private void startScan()
    {
        if (!isScannerStarted())
        {
            m_syncTask = new ScanSyncTimerTask(this, debugMode);
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

    private void updateScan()
    {
        if (isScannerStarted())
        {
            stopScan();
        }
        checkedStartScan();

        updateNotification();
    }

    private void checkWiFiState(int state)
    {
        switch (state)
        {
            case WifiManager.WIFI_STATE_DISABLED:
                if (isScannerStarted())
                {
                    stopScan();
                    updateNotification();
                    alertWiFiDisabled();
                }
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                checkedStartScan();
                updateNotification();
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

    private Notification createNotification()
    {
        boolean scannerStarted = isScannerStarted();

        Intent intentGoToApp = new Intent(this, MainActivity.class);
        Intent intentStopService = new Intent(this, ScanServiceStopper.class);
        PendingIntent pIntentGoToApp = PendingIntent.getActivity(this, 0, intentGoToApp, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntentStopService = PendingIntent.getActivity(this, 0, intentStopService, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pIntentGoToApp);
        builder.setSmallIcon(R.drawable.status_scan);
        builder.setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(false);
        builder.setContentTitle(res.getString(scannerStarted ? R.string.notify_title_scan_on : R.string.notify_title_scan_off));
        builder.setContentText(res.getString(R.string.notify_text));
        builder.addAction(R.drawable.status_scan, res.getString(R.string.notify_action_stop_service), pIntentStopService);

        Notification notification = builder.build();
        notification.iconLevel = scannerStarted ? 1 : 0;
        return notification;
    }

    private void updateNotification()
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, createNotification());
    }

}
