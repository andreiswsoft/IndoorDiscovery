package ua.com.sweetsoft.indoordiscovery.settings;

import android.support.annotation.StringRes;

import ua.com.sweetsoft.indoordiscovery.R;

public enum SettingId
{
    undefined(0),
    // persists
    ScannerSwitch(1),
    ScanSwitch(100),
    ScanPeriod(101),
    DataStorageDuration(200),
    DebugSwitch(900),
    DebugGeneratedNetworkNumber(901),
    // non persists
    CurrentFragmentType(1000),
    FocusNetworkId(1010);

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

    public @StringRes int toResId()
    {
        @StringRes int resId = R.string.pref_key_undefined;
        switch (this)
        {
            case ScannerSwitch:
                resId = R.string.pref_key_scanner_switch;
                break;
            case ScanSwitch:
                resId = R.string.pref_key_scan_switch;
                break;
            case ScanPeriod:
                resId = R.string.pref_key_scan_period;
                break;
            case DataStorageDuration:
                resId = R.string.pref_key_data_storage_duration;
                break;
            case DebugSwitch:
                resId = R.string.pref_key_debug_switch;
                break;
            case DebugGeneratedNetworkNumber:
                resId = R.string.pref_key_debug_generated_network_number;
                break;
        }
        return resId;
    }

}
