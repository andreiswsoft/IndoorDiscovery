package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;

public class RecyclerViewData
{
    private static final String undefinedSignalLevel = "-";

    private int m_networkId;
    private String m_networkSsid;
    private String m_signalLevel;

    public RecyclerViewData(Network network, SignalSample signalSample)
    {
        m_networkId = network.getId();
        m_networkSsid = network.getSsid();
        if (signalSample != null)
        {
            m_signalLevel = String.valueOf(signalSample.getLevel());
        }
        else
        {
            m_signalLevel = undefinedSignalLevel;
        }
    }

    public int getNetworkId()
    {
        return m_networkId;
    }

    public String getNetworkSsid()
    {
        return m_networkSsid;
    }

    public String getSignalLevel()
    {
        return m_signalLevel;
    }

    public static List<RecyclerViewData> getList()
    {
        List<RecyclerViewData> list = new ArrayList<RecyclerViewData>();

        List<Network> networks = Network.getAll();
        if (networks != null)
        {
            List<SignalSample> samples = SignalSample.getAllCurrent();
            for (Network network : networks)
            {
                SignalSample signalSample = null;
                for (SignalSample ss : samples)
                {
                    if (ss.getNetwork().getId() == network.getId())
                    {
                        signalSample = ss;
                        break;
                    }
                }
                list.add(new RecyclerViewData(network, signalSample));
            }
        }
        return list;
    }
}
