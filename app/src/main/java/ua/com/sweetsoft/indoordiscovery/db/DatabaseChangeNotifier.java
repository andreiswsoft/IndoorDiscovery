package ua.com.sweetsoft.indoordiscovery.db;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class DatabaseChangeNotifier extends ContentObserver
{
    private IDatabaseChangeListener m_listener;

    public DatabaseChangeNotifier(IDatabaseChangeListener listener)
    {
        super(new Handler());

        m_listener = listener;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();

        stop();
    }

    public void start()
    {
        m_listener.getContext().getContentResolver().registerContentObserver(ContentProvider.getAuthorityUri(), true, this);
    }

    public void stop()
    {
        m_listener.getContext().getContentResolver().unregisterContentObserver(this);
    }

    public static void notifyChange(Context context)
    {
        context.getContentResolver().notifyChange(ContentProvider.getAuthorityUri(), null);
    }

    @Override
    public void onChange(boolean selfChange)
    {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri)
    {
        m_listener.onDatabaseChanged();
    }

}
