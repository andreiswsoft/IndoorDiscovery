package ua.com.sweetsoft.indoordiscovery.settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.common.Logger;

public class SettingsManager
{
    private Context m_context;
    private SharedPreferences m_preferences = null;

    public SettingsManager(Context context)
    {
        m_context = context;
    }

    public void setChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        getPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    public void resetChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener)
    {
        getPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    public String keyOf(int id)
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

    public int preferenceToInt(int id, SharedPreferences sharedPreferences)
    {
        int value = 0;
        String key = keyOf(id);
        switch (id)
        {
            case R.string.pref_key_scanner_switch:
                value = sharedPreferences.getBoolean(key, isDefaultScannerOn()) ? 1 : 0;
                break;
            case R.string.pref_key_scan_period:
                String val = sharedPreferences.getString(key, "");
                if (!val.isEmpty())
                {
                    value = Integer.valueOf(val);
                }
                else
                {
                    value = getDefaultScanPeriod();
                }
                break;
            case R.string.pref_key_data_storage_duration:
                val = sharedPreferences.getString(key, "");
                if (!val.isEmpty())
                {
                    value = Integer.valueOf(val);
                }
                else
                {
                    value = getDefaultDataStorageDuration();
                }
                break;
        }
        return value;
    }

    public Object intToObject(int id, int value)
    {
        Object object = null;
        switch (id)
        {
            case R.string.pref_key_scanner_switch:
                object = (value != 0);
                break;
            case R.string.pref_key_scan_period:
            case R.string.pref_key_data_storage_duration:
                object = value;
                break;
        }
        return object;
    }

    public String getString(String key)
    {
        return getPreferences().getString(key, "");
    }

    public boolean isDefaultScannerOn()
    {
        return true;
    }

    public boolean isScannerOn()
    {
        return getPreferences().getBoolean(keyOf(R.string.pref_key_scanner_switch), isDefaultScannerOn());
    }

    public int getDefaultScanPeriod()
    {
        return Integer.valueOf(m_context.getString(R.string.pref_default_scan_period));
    }

    public int getScanPeriod()
    {
        int value = getPreferences().getInt(keyOf(R.string.pref_key_scan_period), getDefaultScanPeriod());
        if (value < 0)
        {
            value = 0;
        }
        return value;
    }

    public int getDefaultDataStorageDuration()
    {
        return Integer.valueOf(m_context.getString(R.string.pref_default_data_storage_duration));
    }

    public int getDataStorageDuration()
    {
        int value = getPreferences().getInt(keyOf(R.string.pref_key_data_storage_duration), getDefaultDataStorageDuration());
        if (value < 0)
        {
            value = 0;
        }
        return value;
    }

    private SharedPreferences getPreferences()
    {
        if (m_preferences == null)
        {
            m_preferences = PreferenceManager.getDefaultSharedPreferences(m_context);
        }
        return m_preferences;
    }
}
