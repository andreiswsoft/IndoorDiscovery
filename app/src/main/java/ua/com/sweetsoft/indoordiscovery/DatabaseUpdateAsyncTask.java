package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;
import ua.com.sweetsoft.indoordiscovery.db.DatabaseChangeNotifier;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelper;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelperFactory;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.NetworkDao;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSampleDao;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class DatabaseUpdateAsyncTask extends AsyncTask<Void, Void, Void>
{
    private Context m_context;

    public DatabaseUpdateAsyncTask(Context context)
    {
        m_context = context;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        deleteExpiredSignalSamples();
        deleteAbsentNetworks();

        DatabaseChangeNotifier.notifyChange(m_context);

        return null;
    }

    private void deleteExpiredSignalSamples()
    {
        SettingsManager settingsManager = SettingsManager.getInstance(m_context);
        if (settingsManager != null)
        {
            long expirationTime = System.currentTimeMillis() / 1000L - settingsManager.getDataStorageDuration();

            do
            {
                DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
                if (databaseHelper == null)
                {
                    break;
                }
                SignalSampleDao signalSampleDao = databaseHelper.getSignalSampleDao();
                if (signalSampleDao == null)
                {
                    break;
                }
                DeleteBuilder<SignalSample, Integer> deleteBuilder = signalSampleDao.getDao().deleteBuilder();
                if (deleteBuilder == null)
                {
                    break;
                }
                Where<SignalSample, Integer> where = deleteBuilder.where();
                try
                {
                    where.lt(Config.COLUMN_SIGNALSAMPLE_TIME, expirationTime);
                }
                catch (SQLException e)
                {
                    Logger.logException(e, "Where.lt(\"" + Config.COLUMN_SIGNALSAMPLE_TIME + "\", " + String.valueOf(expirationTime) + ")");
                    break;
                }
                PreparedDelete<SignalSample> preparedDelete = null;
                try
                {
                    preparedDelete = deleteBuilder.prepare();
                }
                catch (SQLException e)
                {
                    Logger.logException(e, "QueryBuilder.prepare()");
                    break;
                }
                signalSampleDao.delete(preparedDelete);
            } while (false);
        }
    }

    private void deleteAbsentNetworks()
    {
        do
        {
            DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
            if (databaseHelper == null)
            {
                break;
            }
            NetworkDao networkDao = databaseHelper.getNetworkDao();
            if (networkDao == null)
            {
                break;
            }
            List<Network> networks = networkDao.getAll();
            if (networks == null)
            {
                break;
            }
            for (Network network : networks)
            {
                if (network.getSignalSampleCount() == 0)
                {
                    networkDao.delete(network);
                }
            }
        } while (false);
    }
}
