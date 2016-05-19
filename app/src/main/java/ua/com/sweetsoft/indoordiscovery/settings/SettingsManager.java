package ua.com.sweetsoft.indoordiscovery.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.ScanService;
import ua.com.sweetsoft.indoordiscovery.common.ServiceMessageSender;
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;

public final class SettingsManager implements SharedPreferences.OnSharedPreferenceChangeListener, ServiceMessageSender.ISender
{
    private static volatile WeakReference<SettingsManager> m_instance = new WeakReference<SettingsManager>(null);

    private List<WeakReference<ISettingsListener>> m_listeners = new ArrayList<WeakReference<ISettingsListener>>();
    private Context m_context;
    private ServiceMessageSender m_messenger = null;
    private SharedPreferences m_preferences = null;
    // Persists settings
    private boolean m_scanOn;
    private int m_scanPeriod;
    private int m_dataStorageDuration;
    // Non persists settings
    private Fragment.FragmentType m_currentFragmentType = Fragment.FragmentType.Grid;
    private int m_focusNetworkId = 0;

    public static SettingsManager checkInstance()
    {
        return m_instance.get();
    }

    public static SettingsManager getInstance(Context context)
    {
        SettingsManager instance = m_instance.get();

        if (instance == null)
        {
            synchronized (SettingsManager.class)
            {
                instance = m_instance.get();
                if (instance == null)
                {
                    instance = new SettingsManager(context.getApplicationContext());
                    m_instance = new WeakReference<SettingsManager>(instance);
                }
            }
        }
        return instance;
    }

    private SettingsManager(Context context)
    {
        m_context = context;

        ReadSettings();
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();

        stopSyncChanges();
    }

    public void startSyncChanges()
    {
        if (m_messenger == null)
        {
            m_messenger = new ServiceMessageSender(this);
            m_messenger.bind(m_context, ScanService.class);
        }
        getPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void stopSyncChanges()
    {
        getPreferences().unregisterOnSharedPreferenceChangeListener(this);
        if (m_messenger != null)
        {
            m_messenger.unbind(m_context);
            m_messenger = null;
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        setSetting(key, sharedPreferences);
    }

    @Override
    public void onServiceMessageReceiverConnected()
    {
    }

    private void sendSetting(SettingId settingId)
    {
        if (m_messenger != null)
        {
            int value = 0;
            switch (settingId)
            {
                case ScanSwitch:
                    value = m_scanOn ? 1 : 0;
                    break;
                case ScanPeriod:
                    value = m_scanPeriod;
                    break;
                case DataStorageDuration:
                    value = m_dataStorageDuration;
                    break;
            }
            Message message = Message.obtain(null, ScanService.MessageCode.ReceiveSetting.toInt(), settingId.toInt(), value);
            m_messenger.send(message);
        }
    }

    public void receiveSetting(int id, int value)
    {
        switch (SettingId.fromInt(id))
        {
            case ScanSwitch:
                setScanOn(value != 0);
                break;
            case ScanPeriod:
                setScanPeriod(value);
                break;
            case DataStorageDuration:
                setDataStorageDuration(value);
                break;
        }
    }

    private void ReadSettings()
    {
        SharedPreferences preferences = getPreferences();
        m_scanOn = preferences.getBoolean(settingIdToKey(SettingId.ScanSwitch), isDefaultScanOn());
        m_scanPeriod = getScanPeriod(preferences);
        m_dataStorageDuration = getDataStorageDuration(preferences);
    }

    private String settingIdToKey(SettingId settingId)
    {
        return m_context.getString(settingId.toResId());
    }

    public SettingId keyToSettingId(String key)
    {
        SettingId settingId;
        do
        {
            settingId = SettingId.ScanSwitch;
            if (settingIdToKey(settingId).equals(key))
            {
                break;
            }
            settingId = SettingId.ScanPeriod;
            if (settingIdToKey(settingId).equals(key))
            {
                break;
            }
            settingId = SettingId.DataStorageDuration;
            if (settingIdToKey(settingId).equals(key))
            {
                break;
            }
            settingId = SettingId.undefined;
        } while(false);
        return settingId;
    }

    public long calculateExpirationTime()
    {
        return (System.currentTimeMillis()/1000L - getScanPeriod());
    }

    // Persists settings
    public boolean isScanOn()
    {
        return m_scanOn;
    }

    public void setScanOn(boolean scanOn)
    {
        if (m_scanOn != scanOn)
        {
            m_scanOn = scanOn;
            sendSetting(SettingId.ScanSwitch);
            notifyChange(SettingId.ScanSwitch);
        }
    }

    public int getScanPeriod()
    {
        return m_scanPeriod;
    }

    public void setScanPeriod(int scanPeriod)
    {
        if (m_scanPeriod != scanPeriod)
        {
            m_scanPeriod = scanPeriod;
            sendSetting(SettingId.ScanPeriod);
            notifyChange(SettingId.ScanPeriod);
        }
    }

    public int getDataStorageDuration()
    {
        return m_dataStorageDuration;
    }

    public void setDataStorageDuration(int dataStorageDuration)
    {
        if (m_dataStorageDuration != dataStorageDuration)
        {
            m_dataStorageDuration = dataStorageDuration;
            sendSetting(SettingId.DataStorageDuration);
            notifyChange(SettingId.DataStorageDuration);
        }
    }

    public String getString(String key)
    {
        return getPreferences().getString(key, "");
    }

    private int getScanPeriod(SharedPreferences preferences)
    {
        int period;
        String value = preferences.getString(settingIdToKey(SettingId.ScanPeriod), "");
        if (!value.isEmpty())
        {
            period = Integer.valueOf(value);
        }
        else
        {
            period = getDefaultScanPeriod();
        }
        if (period < 0)
        {
            period = 0;
        }
        return period;
    }

    private int getDataStorageDuration(SharedPreferences preferences)
    {
        int duration;
        String value = preferences.getString(settingIdToKey(SettingId.DataStorageDuration), "");
        if (!value.isEmpty())
        {
            duration = Integer.valueOf(value);
        }
        else
        {
            duration = getDefaultDataStorageDuration();
        }
        if (duration < 0)
        {
            duration = 0;
        }
        return duration;
    }

    private void setSetting(String key, SharedPreferences sharedPreferences)
    {
        switch (keyToSettingId(key))
        {
            case ScanSwitch:
                setScanOn(sharedPreferences.getBoolean(key, m_scanOn));
                break;
            case ScanPeriod:
                setScanPeriod(getScanPeriod(sharedPreferences));
                break;
            case DataStorageDuration:
                setDataStorageDuration(getDataStorageDuration(sharedPreferences));
                break;
        }
    }

    public boolean isDefaultScanOn()
    {
        return true;
    }

    private int getDefaultScanPeriod()
    {
        return Integer.valueOf(m_context.getString(R.string.pref_default_scan_period));
    }

    private int getDefaultDataStorageDuration()
    {
        return Integer.valueOf(m_context.getString(R.string.pref_default_data_storage_duration));
    }

    private SharedPreferences getPreferences()
    {
        if (m_preferences == null)
        {
            m_preferences = PreferenceManager.getDefaultSharedPreferences(m_context);
        }
        return m_preferences;
    }

    // Non persists settings
    public Fragment.FragmentType getCurrentFragmentType()
    {
        return m_currentFragmentType;
    }

    public void setCurrentFragmentType(Fragment.FragmentType type)
    {
        if (m_currentFragmentType != type)
        {
            m_currentFragmentType = type;
            sendSetting(SettingId.CurrentFragmentType);
            notifyChange(SettingId.CurrentFragmentType);
        }
    }

    public int getFocusNetworkId()
    {
        return m_focusNetworkId;
    }

    public void setFocusNetworkId(int networkId)
    {
        if (m_focusNetworkId != networkId)
        {
            m_focusNetworkId = networkId;
            sendSetting(SettingId.FocusNetworkId);
            notifyChange(SettingId.FocusNetworkId);
        }
    }

    public void registerSettingsListener(ISettingsListener listener)
    {
        for (WeakReference<ISettingsListener> listenerRef : m_listeners)
        {
            if (listenerRef.get() == listener)
                return;
        }
        m_listeners.add(new WeakReference<ISettingsListener>(listener));
    }

    public void unregisterSettingsListener(ISettingsListener listener)
    {
        for (WeakReference<ISettingsListener> listenerRef : m_listeners)
        {
            if (listenerRef.get() == listener)
            {
                m_listeners.remove(listenerRef);
                break;
            }
        }
    }

    private void notifyChange(SettingId settingId)
    {
        for (WeakReference<ISettingsListener> listenerRef : m_listeners)
        {
            ISettingsListener listener = listenerRef.get();
            if (listener != null)
            {
                listener.onSettingChanged(settingId);
            }
        }
    }
}
