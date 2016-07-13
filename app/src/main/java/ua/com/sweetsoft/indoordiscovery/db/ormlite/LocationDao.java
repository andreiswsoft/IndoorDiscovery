package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;

public class LocationDao extends ua.com.sweetsoft.indoordiscovery.db.ormlite.Dao<Location>
{
    public LocationDao(com.j256.ormlite.dao.Dao<Location, Integer> dao)
    {
        super(dao);
    }

    public Location read(Location loc)
    {
        Location location = null;
        try
        {
            Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put(Config.COLUMN_LOCATION_LATITUDE, loc.getLatitude());
            fieldValues.put(Config.COLUMN_LOCATION_LONGITUDE, loc.getLongitude());
            fieldValues.put(Config.COLUMN_LOCATION_ALTITUDE, loc.getAltitude());
            fieldValues.put(Config.COLUMN_LOCATION_TIME, loc.getTime());
            List<Location> locs = m_dao.queryForFieldValues(fieldValues);
            if (locs != null && !locs.isEmpty())
            {
                location = locs.get(0);
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e, "dao.queryForFieldValues(-" + String.valueOf(loc.getLatitude()) + "-, -" + String.valueOf(loc.getLongitude()) + "-, -" + String.valueOf(loc.getAltitude()) + "-, -" + String.valueOf(loc.getTime()) + "-)");
        }
        return location;
    }
}
