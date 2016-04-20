package ua.com.sweetsoft.indoordiscovery.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.XmlRes;
import android.view.MenuItem;

import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class PreferenceFragment extends android.preference.PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(getResourceId());
        setHasOptionsMenu(isOptionsMenu());
        for (Integer resId : getPreferenceKeyResourceIds())
        {
            SettingsActivity.bindPreference(findPreference(getString(resId)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isChild(String className)
    {
        boolean valid = false;

        Class<?> cls = null;
        try
        {
            cls = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
        }
        if (cls != null)
        {
            valid = cls.getSuperclass().getCanonicalName().equals(PreferenceFragment.class.getCanonicalName());
        }

        return valid;
    }

    protected abstract @XmlRes int getResourceId();
    protected abstract boolean isOptionsMenu();
    protected abstract List<Integer> getPreferenceKeyResourceIds();

}
