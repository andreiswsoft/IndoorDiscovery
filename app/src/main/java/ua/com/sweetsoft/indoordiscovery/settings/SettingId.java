package ua.com.sweetsoft.indoordiscovery.settings;

import android.support.annotation.StringRes;

import ua.com.sweetsoft.indoordiscovery.R;

public enum SettingId
{
    undefined(0),
    // persists
    ScanSwitch(1),
    ScanPeriod(2),
    DataStorageDuration(3),
    // non persists
    CurrentFragmentType(100),
    FocusNetworkId(110);

    private int m_i;

    SettingId(int i)
    {
        m_i = i;
    }

    public int toInt()
    {
        return m_i;
    }

    public static SettingId fromInt(int i)
    {
        for (SettingId mc : SettingId.values())
        {
            if (mc.toInt() == i)
            {
                return mc;
            }
        }
        return null;
    }

    public @StringRes int toResId ()
    {
        @StringRes int resId = R.string.pref_key_undefined;
        switch (this)
        {
            case ScanSwitch:
                resId = R.string.pref_key_scan_switch;
                break;
            case ScanPeriod:
                resId = R.string.pref_key_scan_period;
                break;
            case DataStorageDuration:
                resId = R.string.pref_key_data_storage_duration;
                break;
        }
        return resId;
    }

}
