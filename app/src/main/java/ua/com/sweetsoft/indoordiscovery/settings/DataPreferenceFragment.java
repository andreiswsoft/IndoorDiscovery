package ua.com.sweetsoft.indoordiscovery.settings;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.XmlRes;

import java.util.Arrays;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DataPreferenceFragment extends PreferenceFragment
{
    private static final List<Integer> m_prefKeyResIds = Arrays.asList(R.string.pref_key_data_storage_duration);

    @Override
    protected @XmlRes int getResourceId()
    {
        return R.xml.pref_data;
    }

    @Override
    protected boolean isOptionsMenu()
    {
        return true;
    }

    @Override
    protected List<Integer> getPreferenceKeyResourceIds()
    {
        return m_prefKeyResIds;
    }
}
