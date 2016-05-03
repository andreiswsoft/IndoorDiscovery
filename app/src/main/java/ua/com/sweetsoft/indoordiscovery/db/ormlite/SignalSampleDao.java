package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;

public class SignalSampleDao extends ua.com.sweetsoft.indoordiscovery.db.ormlite.Dao<SignalSample>
{
    public SignalSampleDao(Dao<SignalSample, Integer> dao)
    {
        super(dao);
    }

    @Override
    public SignalSample read(SignalSample sgnSample)
    {
        SignalSample signalSample = null;
        try
        {
            Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put(Config.COLUMN_SIGNALSAMPLE_NETWORK_ID, sgnSample.getNetwork().getId());
            fieldValues.put(Config.COLUMN_SIGNALSAMPLE_TIME, sgnSample.getTime());

            List<SignalSample> signalSamples = m_dao.queryForFieldValues(fieldValues);
            if (signalSamples != null && !signalSamples.isEmpty())
            {
                signalSample = signalSamples.get(0);
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e, "dao.queryForFieldValues(\"" + Config.COLUMN_SIGNALSAMPLE_NETWORK_ID + "\"; \"" + Config.COLUMN_SIGNALSAMPLE_TIME + "\")");
        }
        return signalSample;
    }

}
