package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.sql.SQLException;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;

public class SignalSample implements Serializable
{
    private static final long serialVersionUID = 852615821590063697L;

    @DatabaseField(generatedId = true, columnName = Config.COLUMN_ID)
    private int m_id;
    @DatabaseField(canBeNull = false, foreign = true, columnName = Config.COLUMN_SIGNALSAMPLE_NETWORK_ID)
    private Network m_network;
    @DatabaseField(canBeNull = false, columnName = Config.COLUMN_SIGNALSAMPLE_LEVEL)
    private int m_level;
    @DatabaseField(canBeNull = false, columnName = Config.COLUMN_SIGNALSAMPLE_TIME)
    private long m_time;

    public SignalSample()
    {
    }

    public SignalSample(Network network, final int level, final long time)
    {
        m_id = 0;
        m_network = network;
        m_level = level;
        m_time = time;
    }

    public int getId()
    {
        return m_id;
    }

    public Network getNetwork()
    {
        return m_network;
    }

    public int getLevel()
    {
        return m_level;
    }

    public long getTime()
    {
        return m_time;
    }

    public static SignalSampleCursor getCursorForNetwork(Network network, boolean ascending)
    {
        SignalSampleCursor signalSampleCursor = null;

        DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
        if (databaseHelper != null)
        {
            SignalSampleDao signalSampleDao = databaseHelper.getSignalSampleDao();
            if (signalSampleDao != null)
            {
                SignalSampleCursor cursor = new SignalSampleCursor(signalSampleDao);
                if (cursor.select(network.getId(), ascending))
                {
                    signalSampleCursor = cursor;
                }
                else
                {
                    cursor.close();
                }
            }
        }
        return signalSampleCursor;
    }

}
