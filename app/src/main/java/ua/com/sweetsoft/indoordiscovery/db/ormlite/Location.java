package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.db.Config;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class Location implements Serializable
{
    private static final long serialVersionUID = -4218218696108091731L;

    @DatabaseField(generatedId = true, columnName = Config.COLUMN_ID)
    private int m_id;
    @DatabaseField(canBeNull = false, columnName = Config.COLUMN_LOCATION_LATITUDE)
    private double m_latitude;
    @DatabaseField(canBeNull = false, columnName = Config.COLUMN_LOCATION_LONGITUDE)
    private double m_longitude;
    @DatabaseField(canBeNull = true, columnName = Config.COLUMN_LOCATION_ALTITUDE)
    private double m_altitude;
    @DatabaseField(canBeNull = false, columnName = Config.COLUMN_LOCATION_TIME)
    private long m_time;

    public Location()
    {
    }

    public Location(final double latitude, final double longitude, final double altitude, final long time)
    {
        m_id = 0;
        m_latitude = latitude;
        m_longitude = longitude;
        m_altitude = altitude;
        m_time = time;
    }

    public int getId()
    {
        return m_id;
    }

    public double getLatitude()
    {
        return m_latitude;
    }

    public double getLongitude()
    {
        return m_longitude;
    }

    public double getAltitude()
    {
        return m_altitude;
    }

    public long getTime()
    {
        return m_time;
    }

    public static LocationCursor getCursor()
    {
        LocationCursor locationCursor = null;

        DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
        if (databaseHelper != null)
        {
            LocationDao locationDao = databaseHelper.getLocationDao();
            if (locationDao != null)
            {
                LocationCursor cursor = new LocationCursor(locationDao);
                if (cursor.select())
                {
                    locationCursor = cursor;
                }
                else
                {
                    cursor.close();
                }
            }
        }
        return locationCursor;
    }

    public static List<Location> getAll()
    {
        List<Location> locations = null;

        DatabaseHelper databaseHelper = DatabaseHelperFactory.getHelper();
        if (databaseHelper != null)
        {
            LocationDao locationDao = databaseHelper.getLocationDao();
            if (locationDao != null)
            {
                locations = locationDao.getAll();
            }
        }
        return locations;
    }

    public static List<Location> getAllCurrent()
    {
        List<Location> samples = null;

        SettingsManager manager = SettingsManager.checkInstance();
        if (manager != null)
        {
            LocationCursor cursor = getCursor();
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

    private static List<Location> read(LocationCursor cursor)
    {
        List<Location> samples = new ArrayList<Location>();
        while (cursor.hasNext())
        {
            Location sample = cursor.getNext();
            samples.add(sample);
        }
        return samples;
    }

}
