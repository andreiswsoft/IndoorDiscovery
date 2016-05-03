package ua.com.sweetsoft.indoordiscovery.db;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.wifi.NetworkContentProvider;

public class DatabaseChangeNotifier implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int LOADER_ID = 1;

    private Context m_context;
    private LoaderManager m_loaderManager;
    private List<IDatabaseChangeListener> m_listeners = new ArrayList<IDatabaseChangeListener>();

    public DatabaseChangeNotifier(Activity activity)
    {
        m_context = activity;
        m_loaderManager = activity.getLoaderManager();
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();

        stop();
    }

    public void add(IDatabaseChangeListener listener)
    {
        if (!m_listeners.contains(listener))
        {
            m_listeners.add(listener);
        }
    }

    public void remove(IDatabaseChangeListener listener)
    {
        if (m_listeners.contains(listener))
        {
            m_listeners.remove(listener);
        }
    }

    public void start()
    {
        m_loaderManager.initLoader(LOADER_ID, null, this);
    }

    public void stop()
    {
        m_loaderManager.destroyLoader(LOADER_ID);
    }

    public static void notifyChange(Context context)
    {
        context.getContentResolver().notifyChange(NetworkContentProvider.NETWORKS_URI, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(m_context, NetworkContentProvider.NETWORKS_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        if (loader.getId() == LOADER_ID)
        {
            for (IDatabaseChangeListener listener : m_listeners)
            {
                listener.onDatabaseChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        if (loader.getId() == LOADER_ID)
        {
            for (IDatabaseChangeListener listener : m_listeners)
            {
                listener.onDatabaseChanging();
            }
        }
    }
}
