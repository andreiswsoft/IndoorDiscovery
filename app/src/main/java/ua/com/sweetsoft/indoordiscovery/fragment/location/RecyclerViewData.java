package ua.com.sweetsoft.indoordiscovery.fragment.location;

import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.db.ormlite.Location;

public class RecyclerViewData
{
    private int m_locationId;
    private double m_latitude;
    private double m_longitude;
    private double m_altitude;
    private long m_time;

    public RecyclerViewData(Location location)
    {
        m_locationId = location.getId();
        m_latitude = location.getLatitude();
        m_longitude = location.getLongitude();
        m_altitude = location.getAltitude();
        m_time = location.getTime();
    }

    public int getLocationId()
    {
        return m_locationId;
    }

    public double getLongitude()
    {
        return m_longitude;
    }

    public double getLatitude()
    {
        return m_latitude;
    }

    public double getAltitude()
    {
        return m_altitude;
    }

    public long getTime()
    {
        return m_time;
    }

    public static List<RecyclerViewData> getList()
    {
        List<RecyclerViewData> list = new ArrayList<RecyclerViewData>();

        List<Location> locations = Location.getAll();
        if (locations != null)
        {
            for (Location location : locations)
            {
                list.add(new RecyclerViewData(location));
            }
        }
        return list;
    }
}
