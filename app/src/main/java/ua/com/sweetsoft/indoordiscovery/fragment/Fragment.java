package ua.com.sweetsoft.indoordiscovery.fragment;

import android.content.Context;

import ua.com.sweetsoft.indoordiscovery.fragment.graph.FragmentGraph;
import ua.com.sweetsoft.indoordiscovery.fragment.grid.FragmentGrid;
import ua.com.sweetsoft.indoordiscovery.fragment.location.LocationFragment;
import ua.com.sweetsoft.indoordiscovery.settings.ISettingsListener;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public abstract class Fragment extends android.support.v4.app.Fragment implements ISettingsListener
{
    public enum FragmentType
    {
        Grid(1),
        Graph(2),
        Location(3);

        private int m_i;

        FragmentType(int i)
        {
            m_i = i;
        }

        public int toInt()
        {
            return m_i;
        }

        public static FragmentType fromInt(int i)
        {
            for (FragmentType type : FragmentType.values())
            {
                if (type.toInt() == i)
                {
                    return type;
                }
            }
            return null;
        }

        public String toTag()
        {
            String tag = "";
            switch (this)
            {
                case Grid:
                    tag = "fragmentGrid";
                    break;
                case Graph:
                    tag = "fragmentGraph";
                    break;
                case Location:
                    tag = "fragmentLocation";
                    break;
            }
            return tag;
        }

    }

    protected SettingsManager m_settingsManager = null;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        m_settingsManager = SettingsManager.getInstance(context);
        m_settingsManager.registerSettingsListener(this);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        m_settingsManager.unregisterSettingsListener(this);
        m_settingsManager = null;
    }

    public static Fragment create(FragmentType type)
    {
        Fragment fragment = null;
        switch (type)
        {
            case Grid:
                fragment = new FragmentGrid();
                break;
            case Graph:
                fragment = new FragmentGraph();
                break;
            case Location:
                fragment = new LocationFragment();
                break;
        }
        return fragment;
    }

    public abstract void refresh();
}
