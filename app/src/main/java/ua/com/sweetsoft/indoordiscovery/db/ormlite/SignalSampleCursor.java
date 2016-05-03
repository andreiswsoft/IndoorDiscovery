package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;

public class SignalSampleCursor extends Cursor<SignalSample, Integer>
{

    public SignalSampleCursor(SignalSampleDao dao)
    {
        super(dao.getDao());
    }

    public boolean select(int networkId, boolean ascending)
    {
        close();
        do
        {
            if (m_dao == null)
            {
                break;
            }
            QueryBuilder<SignalSample, Integer> queryBuilder = m_dao.queryBuilder();
            if (queryBuilder == null)
            {
                break;
            }
            Where<SignalSample, Integer> where = queryBuilder.where();
            try
            {
                where.eq(Config.COLUMN_SIGNALSAMPLE_NETWORK_ID, networkId);
            }
            catch (SQLException e)
            {
                Logger.logException(e, "Where.eq(\"" + Config.COLUMN_SIGNALSAMPLE_NETWORK_ID + "\", " + String.valueOf(networkId) + ")");
                break;
            }
            PreparedQuery<SignalSample> preparedQuery = null;
            try
            {
                preparedQuery = queryBuilder.orderBy(Config.COLUMN_SIGNALSAMPLE_TIME, ascending).prepare();
            }
            catch (SQLException e)
            {
                Logger.logException(e, "QueryBuilder.orderBy(\"" + Config.COLUMN_SIGNALSAMPLE_TIME + "\", " + String.valueOf(ascending) + ")");
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
