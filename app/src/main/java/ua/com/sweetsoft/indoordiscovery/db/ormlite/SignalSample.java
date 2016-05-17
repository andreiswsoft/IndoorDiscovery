package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

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

        SignalSampleCursor cursor = getCursor();
        if (cursor != null)
        {
            if (cursor.selectNetwork(network.getId(), ascending))
            {
                signalSampleCursor = cursor;
            }
            else
            {
                cursor.close();
            }
        }
        return signalSampleCursor;
    }

    public static List<SignalSample> getAllForNetwork(Network network, boolean ascending)
    {
        List<SignalSample> samples = null;

        SignalSampleCursor cursor = getCursorForNetwork(network, ascending);
        if (cursor != null)
        {
            samples = read(cursor);
            cursor.close();
        }
        return samples;
    }

    public static List<SignalSample> getAllCurrent()
    {
        List<SignalSample> samples = null;

        SettingsManager manager = SettingsManager.checkInstance();
        if (manager != null)
        {
            SignalSampleCursor cursor = getCursor();
            if (cursor != null)
            {
                long expirationTime = manager.calculateExpirationTime();
                if (cursor.selectAfterTime(expirationTime))
                {
                    samples = read(cursor);
                }
                cursor.close();
            }
        }
        return samples;
    }

    private static SignalSampleCursor getCursor()
    {
        SignalSampleCursor signalSampleCursor = null;

        DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
        if (databaseHelper != null)
        {
            SignalSampleDao signalSampleDao = databaseHelper.getSignalSampleDao();
            if (signalSampleDao != null)
            {
                signalSampleCursor = new SignalSampleCursor(signalSampleDao);
            }
        }
        return signalSampleCursor;
    }

    private static List<SignalSample> read(SignalSampleCursor cursor)
    {
        List<SignalSample> samples = new ArrayList<SignalSample>();
        while (cursor.hasNext())
        {
            SignalSample sample = cursor.getNext();
            samples.add(sample);
        }
        return samples;
    }

}
