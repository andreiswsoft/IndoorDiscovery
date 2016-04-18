package ua.com.sweetsoft.indoordiscovery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.com.sweetsoft.indoordiscovery.fragment.IFragment;
import ua.com.sweetsoft.indoordiscovery.fragment.graph.OnGraphListener;
import ua.com.sweetsoft.indoordiscovery.fragment.grid.OnGridListener;
import ua.com.sweetsoft.indoordiscovery.wifi.NetworkContentProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnGridListener, OnGraphListener
{
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        startService(new Intent(this, ScanService.class));
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
}
