package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DatabaseHelperFactory
{
    private static DatabaseHelper m_databaseHelper;

    public static DatabaseHelper getHelper()
    {
        return m_databaseHelper;
    }

    public static void setHelper(Context context)
    {
        m_databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void releaseHelper()
    {
        OpenHelperManager.releaseHelper();
        m_databaseHelper = null;
    }

}
