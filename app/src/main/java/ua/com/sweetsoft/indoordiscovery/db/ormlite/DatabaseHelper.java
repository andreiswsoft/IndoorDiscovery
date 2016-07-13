package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.common.Logger;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 3;

    private NetworkDao m_networkDao;
    private SignalSampleDao m_signalSampleDao;
    private LocationDao m_locationDao;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, Network.class);
            TableUtils.createTable(connectionSource, SignalSample.class);
            TableUtils.createTable(connectionSource, Location.class);
        }
        catch (SQLException e)
        {
            Logger.logException(e, "");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer)
    {
        try
        {
            TableUtils.dropTable(connectionSource, Network.class, true);
            TableUtils.dropTable(connectionSource, SignalSample.class, true);
            TableUtils.dropTable(connectionSource, Location.class, true);
            onCreate(sqliteDatabase, connectionSource);
        }
        catch (SQLException e)
        {
            Logger.logException(e, "version " + String.valueOf(oldVer) + " -> " + String.valueOf(newVer));
        }
    }

    @Override
    public void close()
    {
        super.close();

        m_networkDao = null;
        m_signalSampleDao = null;
        m_locationDao = null;
    }

    public NetworkDao getNetworkDao()
    {
        if (m_networkDao == null)
        {
            try
            {
                Dao<Network, Integer> dao = getDao(Network.class);
                if (dao != null)
                {
                    m_networkDao = new NetworkDao(dao);
                }
            }
            catch (SQLException e)
            {
                Logger.logException(e, "DatabaseHelper.getNetworkDao()");
            }
        }
        return m_networkDao;
    }

    public SignalSampleDao getSignalSampleDao()
    {
        if (m_signalSampleDao == null)
        {
            try
            {
                Dao<SignalSample, Integer> dao = getDao(SignalSample.class);
                if (dao != null)
                {
                    m_signalSampleDao = new SignalSampleDao(dao);
                }
            }
            catch (SQLException e)
            {
                Logger.logException(e, "DatabaseHelper.getSignalSampleDao()");
            }
        }
        return m_signalSampleDao;
    }

    public LocationDao getLocationDao()
    {
        if (m_locationDao == null)
        {
            try
            {
                Dao<Location, Integer> dao = getDao(Location.class);
                if (dao != null)
                {
                    m_locationDao = new LocationDao(dao);
                }
            }
            catch (SQLException e)
            {
                Logger.logException(e, "DatabaseHelper.getLocationDao()");
            }
        }
        return m_locationDao;
    }

}
