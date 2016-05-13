package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.Config;

public class NetworkDao extends ua.com.sweetsoft.indoordiscovery.db.ormlite.Dao<Network>
{
    public NetworkDao(Dao<Network, Integer> dao)
    {
        super(dao);
    }

    public Network read(Network net)
    {
        Network network = null;
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Config.COLUMN_NETWORK_SSID, net.getSsid());
            map.put(Config.COLUMN_NETWORK_BSSID, net.getBssid());
            List<Network> nets = m_dao.queryForFieldValues(map);
            if (nets != null && !nets.isEmpty())
            {
                network = nets.get(0);
            }
        }
        catch (SQLException e)
        {
            Logger.logException(e, "dao.queryForFieldValues(-" + net.getSsid() + "-, -" + net.getBssid() + "-)");
        }
        return network;
    }
}
