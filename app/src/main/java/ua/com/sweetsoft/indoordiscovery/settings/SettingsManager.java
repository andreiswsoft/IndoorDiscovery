package ua.com.sweetsoft.indoordiscovery.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.ScanService;
import ua.com.sweetsoft.indoordiscovery.common.ServiceMessageSender;
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;

public final class SettingsManager implements SharedPreferences.OnSharedPreferenceChangeListener, ServiceMessageSender.ISender
{
    private static volatile WeakReference<SettingsManager> m_instance = new WeakReference<SettingsManager>(null);

    private Context m_context;
    private ServiceMessageSender m_messenger = null;
    private SharedPreferences m_preferences = null;
    // Persists settings
    private boolean m_scannerOn;
    private int m_scanPeriod;
    private int m_dataStorageDuration;
    // Non persists settings
    private Fragment.FragmentType m_currentFragmentType = Fragment.FragmentType.Grid;

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
        if (m_messenger != null)
        {
            sendSetting(key, sharedPreferences);
        }
    }

    @Override
    public void onServiceMessageReceiverConnected()
    {
    }

    private void sendSetting(String key, SharedPreferences sharedPreferences)
    {
        int value = 0;
        int id = idOf(key);
        switch (id)
        {
            case R.string.pref_key_scanner_switch:
                m_scannerOn = sharedPreferences.getBoolean(key, m_scannerOn);
                value = m_scannerOn ? 1 : 0;
                break;
            case R.string.pref_key_scan_period:
                m_scanPeriod = getScanPeriod(sharedPreferences);
                value = m_scanPeriod;
                break;
            case R.string.pref_key_data_storage_duration:
                m_dataStorageDuration = getDataStorageDuration(sharedPreferences);
                value = m_dataStorageDuration;
                break;
        }
        Message message = Message.obtain(null, ScanService.MessageCode.ReceiveSetting.toInt(), id, value);
        m_messenger.send(message);
    }

    public boolean receiveSetting(int id, int value)
    {
        boolean changed = false;
        switch (id)
        {
            case R.string.pref_key_scanner_switch:
                boolean scannerOn = (value != 0);
                if (m_scannerOn != scannerOn)
                {
                    m_scannerOn = scannerOn;
                    changed = true;
                }
                break;
            case R.string.pref_key_scan_period:
                int scanPeriod = value;
                if (m_scanPeriod != scanPeriod)
                {
                    m_scanPeriod = scanPeriod;
                    changed = true;
                }
                break;
            case R.string.pref_key_data_storage_duration:
                int dataStorageDuration = value;
                if (m_dataStorageDuration != dataStorageDuration)
                {
                    m_dataStorageDuration = dataStorageDuration;
                    changed = true;
                }
                break;
        }
        return changed;
    }

    private void ReadSettings()
    {
        SharedPreferences preferences = getPreferences();
        m_scannerOn = preferences.getBoolean(keyOf(R.string.pref_key_scanner_switch), isDefaultScannerOn());
        m_scanPeriod = getScanPeriod(preferences);
        m_dataStorageDuration = getDataStorageDuration(preferences);
    }

    private String keyOf(int id)
    {
        return m_context.getString(id);
    }

    public int idOf(String key)
    {
        int id;
        do
        {
            id = R.string.pref_key_scanner_switch;
            if (keyOf(id).equals(key))
            {
                break;
            }
            id = R.string.pref_key_scan_period;
            if (keyOf(id).equals(key))
            {
                break;
            }
            id = R.string.pref_key_data_storage_duration;
            if (keyOf(id).equals(key))
            {
                break;
            }
            id = 0;
        } while(false);
        return id;
    }

    public boolean isScannerOn()
    {
        return m_scannerOn;
    }

    public int getScanPeriod()
    {
        return m_scanPeriod;
    }

    public int getDataStorageDuration()
    {
        return m_dataStorageDuration;
    }

    public String getString(String key)
    {
        return getPreferences().getString(key, "");
    }

    private int getScanPeriod(SharedPreferences preferences)
    {
        int period;
        String value = preferences.getString(keyOf(R.string.pref_key_scan_period), "");
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
        String value = preferences.getString(keyOf(R.string.pref_key_data_storage_duration), "");
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

    public boolean isDefaultScannerOn()
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

    public Fragment.FragmentType getCurrentFragmentType()
    {
        return m_currentFragmentType;
    }

    public void setCurrentFragmentType(Fragment.FragmentType type)
    {
        m_currentFragmentType = type;
    }

}
