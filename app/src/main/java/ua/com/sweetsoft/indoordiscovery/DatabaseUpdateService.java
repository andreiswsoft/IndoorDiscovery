package ua.com.sweetsoft.indoordiscovery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

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
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Location;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.LocationDao;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.NetworkDao;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSampleDao;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class DatabaseUpdateService extends IntentService
{
    public DatabaseUpdateService()
    {
        super("DatabaseUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        SettingsManager settingsManager = SettingsManager.getInstance(this);
        if (settingsManager != null)
        {
            long expirationTime = System.currentTimeMillis() / 1000L - settingsManager.getDataStorageDuration();
            deleteExpiredLocations(expirationTime);
            deleteExpiredSignalSamples(expirationTime);
        }
        deleteAbsentNetworks();

        DatabaseChangeNotifier.notifyChange(this);

        WakefulReceiver.completeWakefulIntent(intent);
    }

    private void deleteExpiredLocations(long expirationTime)
    {
        do
        {
            DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
            if (databaseHelper == null)
            {
                break;
            }
            LocationDao locationDao = databaseHelper.getLocationDao();
            if (locationDao == null)
            {
                break;
            }
            DeleteBuilder<Location, Integer> deleteBuilder = locationDao.getDao().deleteBuilder();
            if (deleteBuilder == null)
            {
                break;
            }
            Where<Location, Integer> where = deleteBuilder.where();
            try
            {
                where.lt(Config.COLUMN_LOCATION_TIME, expirationTime);
            }
            catch (SQLException e)
            {
                Logger.logException(e, "Where.lt(\"" + Config.COLUMN_LOCATION_TIME + "\", " + String.valueOf(expirationTime) + ")");
                break;
            }
            PreparedDelete<Location> preparedDelete = null;
            try
            {
                preparedDelete = deleteBuilder.prepare();
            }
            catch (SQLException e)
            {
                Logger.logException(e, "QueryBuilder.prepare()");
                break;
            }
            locationDao.delete(preparedDelete);
        } while (false);
    }

    private void deleteExpiredSignalSamples(long expirationTime)
    {
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
