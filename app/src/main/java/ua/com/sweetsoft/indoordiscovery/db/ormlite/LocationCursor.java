package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;

public class LocationCursor extends Cursor<Location, Integer>
{

    public LocationCursor(LocationDao dao)
    {
        super(dao.getDao());
    }

    public boolean select()
    {
        return (getIterator() != null);
    }

    public boolean selectAfterTime(long time)
    {
        close();
        do
        {
            if (m_dao == null)
            {
                break;
            }
            QueryBuilder<Location, Integer> queryBuilder = m_dao.queryBuilder();
            if (queryBuilder == null)
            {
                break;
            }
            Where<Location, Integer> where = queryBuilder.where();
            try
            {
                where.gt(Config.COLUMN_LOCATION_TIME, time);
            }
            catch (SQLException e)
            {
                Logger.logException(e, "Where.gt(\"" + Config.COLUMN_LOCATION_TIME + "\", " + String.valueOf(time) + ")");
                break;
            }
            PreparedQuery<Location> preparedQuery = null;
            try
            {
                preparedQuery = queryBuilder.orderBy(Config.COLUMN_LOCATION_TIME, false).prepare();
            }
            catch (SQLException e)
            {
                Logger.logException(e, "QueryBuilder.orderBy(\"" + Config.COLUMN_LOCATION_TIME + "\", " + String.valueOf(false) + ")");
                break;
            }
            try
            {
                m_iterator = m_dao.iterator(preparedQuery);
                m_position = 0;
            }
            catch (SQLException e)
            {
                Logger.logException(e, "Dao.iterator(...)");
            }
        } while (false);

        return (m_iterator != null);
    }
}
