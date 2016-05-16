package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class Network implements Serializable
{
    private static final long serialVersionUID = 4381793870119177359L;

    @DatabaseField(generatedId = true, columnName = Config.COLUMN_ID)
    private int m_id;
    @DatabaseField(canBeNull = false, columnName = Config.COLUMN_NETWORK_SSID)
    private String m_ssid;
    @DatabaseField(canBeNull = false, columnName = Config.COLUMN_NETWORK_BSSID)
    private String m_bssid;

    public Network()
    {
    }

    public Network(final String ssid, final String bssid)
    {
        m_id = 0;
        m_ssid = ssid;
        m_bssid = bssid;
    }

    public int getId()
    {
        return m_id;
    }

    public String getSsid()
    {
        return m_ssid;
    }

    public String getBssid()
    {
        return m_bssid;
    }

    public List<SignalSample> getSignalSamples(boolean ascending)
    {
        List<SignalSample> list = new ArrayList<SignalSample>();

        SignalSampleCursor cursor = SignalSample.getCursorForNetwork(this, ascending);
        if (cursor != null)
        {
            while (cursor.hasNext())
            {
                list.add(cursor.getNext());
            }
            cursor.close();
        }
        return list;
    }

    public SignalSample getCurrentSignalSample(Context context)
    {
        SignalSample signalSample = null;

        SettingsManager manager = SettingsManager.getInstance(context);
        long scanTime = System.currentTimeMillis()/1000L - manager.getScanPeriod();

        SignalSampleCursor cursor = SignalSample.getCursorForNetwork(this, false);
        if (cursor != null)
        {
            if (cursor.hasNext())
            {
                SignalSample sample = cursor.getNext();
                if (sample.getTime() > scanTime)
                {
                    signalSample = sample;
                }
            }
            cursor.close();
        }
        return signalSample;
    }

    public long getSignalSampleCount()
    {
        long count = 0;

        SignalSampleCursor cursor = SignalSample.getCursorForNetwork(this, true);
        if (cursor != null)
        {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public static NetworkCursor getCursor()
    {
        NetworkCursor networkCursor = null;

        DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
        if (databaseHelper != null)
        {
            NetworkDao networkDao = databaseHelper.getNetworkDao();
            if (networkDao != null)
            {
                NetworkCursor cursor = new NetworkCursor(networkDao);
                if (cursor.select())
                {
                    networkCursor = cursor;
                }
                else
                {
                    cursor.close();
                }
            }
        }
        return networkCursor;
    }
}
