package ua.com.sweetsoft.indoordiscovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.fragment.IFragment;
import ua.com.sweetsoft.indoordiscovery.fragment.graph.OnGraphListener;
import ua.com.sweetsoft.indoordiscovery.fragment.grid.OnGridListener;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsActivity;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;
import ua.com.sweetsoft.indoordiscovery.wifi.NetworkContentProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnGridListener, OnGraphListener, SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final int LOADER_ID = 1;
    ScanServiceMessenger m_serviceMessenger;
    SettingsManager m_settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Logger.enable(true);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        startService(new Intent(this, ScanService.class));

        m_serviceMessenger = new ScanServiceMessenger(null);
        m_serviceMessenger.bind(this);
        m_settingsManager = new SettingsManager(this);
        m_settingsManager.setChangeListener(this);
    }

    @Override
    protected void onDestroy()
    {
        m_serviceMessenger.unbind(this);
        m_settingsManager.resetChangeListener(this);

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean boResult = true;

        switch (item.getItemId())
        {
            case R.id.main_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                boResult = super.onOptionsItemSelected(item);
                break;
        }

        return boResult;
    }

    public void onNetworkClick(int networkId)
    {

    }

    public void onGraph(Uri uri)
    {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this, NetworkContentProvider.NETWORKS_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        switch (loader.getId())
        {
            case LOADER_ID:
                IFragment fragment = findFragment(R.id.fragmentGraph);
                if (fragment != null)
                {
                    fragment.setCursor(cursor);
                }
                fragment = findFragment(R.id.fragmentGrid);
                if (fragment != null)
                {
                    fragment.setCursor(cursor);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        switch (loader.getId())
        {
            case LOADER_ID:
                IFragment fragment = findFragment(R.id.fragmentGraph);
                if (fragment != null)
                {
                    fragment.resetCursor();
                }
                fragment = findFragment(R.id.fragmentGrid);
                if (fragment != null)
                {
                    fragment.resetCursor();
                }
                break;
        }
    }

    private IFragment findFragment(int id)
    {
        IFragment fragment = (IFragment) getFragmentManager().findFragmentById(id);
        if (fragment == null)
        {
            fragment = (IFragment) getSupportFragmentManager().findFragmentById(id);
        }
        return fragment;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        int id = m_settingsManager.idOf(key);
        int value = m_settingsManager.preferenceToInt(id, sharedPreferences);
        m_serviceMessenger.send(ScanServiceMessenger.MessageCode.Update, id, value);
    }
}
