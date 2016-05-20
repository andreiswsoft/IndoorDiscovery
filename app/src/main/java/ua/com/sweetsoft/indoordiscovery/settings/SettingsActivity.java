package ua.com.sweetsoft.indoordiscovery.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.RingtonePreference;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.ScanService;

public class SettingsActivity extends AppCompatPreferenceActivity implements ISettingsListener
{
    private static SettingsManager m_settingsManager = null;

    private static Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener()
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value)
        {
            String stringValue = value.toString();

            if (preference instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            }
            else if (preference instanceof RingtonePreference)
            {
                if (TextUtils.isEmpty(stringValue))
                {
                    //preference.setSummary(R.string.pref_ringtone_silent);
                }
                else
                {
                    Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null)
                    {
                        preference.setSummary(null);
                    }
                    else
                    {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            }
            else
            {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static boolean isXLargeTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static void bindPreference(Preference preference)
    {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);

        switch (m_settingsManager.keyToSettingId(preference.getKey()))
        {
            case ScanPeriod:
            case DataStorageDuration:
            case DebugGeneratedNetworkNumber:
                EditTextPreference editTextPreference = (EditTextPreference)preference;
                EditText editText = editTextPreference.getEditText();
                List<InputFilter> filterList = new ArrayList<InputFilter>();
                for (InputFilter filter : editText.getFilters())
                {
                    filterList.add(filter);
                }
                filterList.add(new PositiveDecimalInputFilter(editTextPreference));
                InputFilter[] filers = new InputFilter[filterList.size()];
                editText.setFilters(filterList.toArray(filers));
                break;
        }

        preferenceChangeListener.onPreferenceChange(preference, m_settingsManager.getString(preference.getKey()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        m_settingsManager = SettingsManager.getInstance(this);
        m_settingsManager.registerSettingsListener(this);
        m_settingsManager.startListenChanges();
        if (ScanService.isServiceStarted(this))
        {
            m_settingsManager.startSyncChanges();
        }

        setupActionBar();
    }

    @Override
    protected void onDestroy()
    {
        m_settingsManager.stopSyncChanges();
        m_settingsManager.stopListenChanges();
        m_settingsManager.unregisterSettingsListener(this);

        super.onDestroy();
    }

    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            if (!super.onMenuItemSelected(featureId, item))
            {
                NavUtils.navigateUpFromSameTask(this);
                /*Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);*/
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onIsMultiPane()
    {
        return isXLargeTablet(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return PreferenceFragment.isChild(fragmentName);
    }

    @Override
    public void onSettingChanged(SettingId settingId)
    {
        switch (settingId)
        {
            case ScannerSwitch:
            case DebugSwitch:
                if (m_settingsManager.isScannerOn() || m_settingsManager.isDebugOn())
                {
                    m_settingsManager.startSyncChanges();
                }
                break;
        }
    }

}
