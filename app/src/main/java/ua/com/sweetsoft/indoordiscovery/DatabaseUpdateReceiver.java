package ua.com.sweetsoft.indoordiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;
import ua.com.sweetsoft.indoordiscovery.db.DatabaseChangeNotifier;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelper;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelperFactory;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSampleDao;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class DatabaseUpdateReceiver extends BroadcastReceiver
{
    public DatabaseUpdateReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        deleteExpiredData(context);

        DatabaseChangeNotifier.notifyChange(context);
    }

    private void deleteExpiredData(Context context)
    {
        SettingsManager settingsManager = SettingsManager.getInstance(context);
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
}
