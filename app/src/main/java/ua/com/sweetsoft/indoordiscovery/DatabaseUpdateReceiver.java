package ua.com.sweetsoft.indoordiscovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.sql.Timestamp;

import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;
import ua.com.sweetsoft.indoordiscovery.wifi.NetworkContentProvider;
import ua.com.sweetsoft.indoordiscovery.wifi.SignalLevelDatabaseCursorHelper;
import ua.com.sweetsoft.indoordiscovery.wifi.SignalLevelDatabaseHelper;

public class DatabaseUpdateReceiver extends BroadcastReceiver
{
    public DatabaseUpdateReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        deleteExpiredData(context);

        context.getContentResolver().notifyChange(NetworkContentProvider.NETWORKS_URI, null);
    }

    private void deleteExpiredData(Context context)
    {
        SettingsManager manager = new SettingsManager(context);
        long expirationTime = System.currentTimeMillis() - manager.getDataStorageDurationMillis();
        Timestamp expirationTimestamp = new Timestamp(expirationTime);

        SignalLevelDatabaseHelper databaseHelper = new SignalLevelDatabaseHelper(context);
        if (databaseHelper.openForWrite())
        {
            String whereClause = SignalLevelDatabaseHelper.COLUMN_TIMESTAMP + " < '" + expirationTimestamp + "'";
            databaseHelper.delete(whereClause, null);
            databaseHelper.close();
        }
    }
}
