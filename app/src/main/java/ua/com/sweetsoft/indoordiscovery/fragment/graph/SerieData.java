package ua.com.sweetsoft.indoordiscovery.fragment.graph;

import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;

public class SerieData
{
    private int m_networkId;
    private String m_networkSsid;
    private List<Integer> m_signalLevels;

    public SerieData(Network network, List<SignalSample> signalSamples)
    {
        m_networkId = network.getId();
        m_networkSsid = network.getSsid();
        m_signalLevels = new ArrayList<Integer>();
        for (SignalSample sample : signalSamples)
        {
            m_signalLevels.add(Integer.valueOf(sample.getLevel()));
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

    public List<Integer> getSignalLevels()
    {
        return m_signalLevels;
    }

    public static List<SerieData> getList()
    {
        List<SerieData> list = new ArrayList<SerieData>();

        List<Network> networks = Network.getAll();
        if (networks != null)
        {
            for (Network network : networks)
            {
                List<SignalSample> samples = SignalSample.getAllForNetwork(network, true);
                list.add(new SerieData(network, samples));
            }
        }
        return list;
    }
}
