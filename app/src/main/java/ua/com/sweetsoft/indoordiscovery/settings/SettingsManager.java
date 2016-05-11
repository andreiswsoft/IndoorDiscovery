package ua.com.sweetsoft.indoordiscovery.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.ScanServiceMessenger;
import ua.com.sweetsoft.indoordiscovery.common.Information;
import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;

public final class SettingsManager implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static volatile SettingsManager m_instance = null;

    private Context m_context;
    private ScanServiceMessenger m_serviceMessenger = null;
    private SharedPreferences m_preferences = null;
    // Persists settings
    private boolean m_scannerOn;
    private int m_scanPeriod;
    private int m_dataStorageDuration;
    // Non persists settings
    private Fragment.FragmentType m_currentFragmentType = Fragment.FragmentType.Grid;

    public static SettingsManager getInstance(Context context)
    {
        if (m_instance == null)
        {
            synchronized (SettingsManager.class)
            {
                if (m_instance == null)
                {
                    try
                    {
                        m_instance = new SettingsManager(context);
                    }
                    catch (RuntimeException e)
                    {
                        Logger.logException(e, "new SettingsManager(...)");
                    }
                }
            }
        }
        return m_instance;
    }

    private SettingsManager(Context context)
    {
        m_context = context;

        ReadSettings();

        if (Information.isApplicationMainProcess(context))
        {
            startTrackChanges();
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();

        stopTrackChanges();
    }

    private void startTrackChanges()
    {
        m_serviceMessenger = new ScanServiceMessenger();
        m_serviceMessenger.bind(m_context);
        getPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void stopTrackChanges()
    {
        if (m_serviceMessenger != null)
        {
            getPreferences().unregisterOnSharedPreferenceChangeListener(this);
            m_serviceMessenger.unbind(m_context);
            m_serviceMessenger = null;
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (m_serviceMessenger != null)
        {
            sendSetting(key, sharedPreferences);
        }
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
        m_serviceMessenger.send(ScanServiceMessenger.MessageCode.ReceiveSetting, id, value);
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
