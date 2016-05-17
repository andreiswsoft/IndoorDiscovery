package ua.com.sweetsoft.indoordiscovery.fragment;

import android.content.Context;

import ua.com.sweetsoft.indoordiscovery.fragment.graph.FragmentGraph;
import ua.com.sweetsoft.indoordiscovery.fragment.grid.FragmentGrid;

public abstract class Fragment extends android.support.v4.app.Fragment
{
    public enum FragmentType
    {
        Grid(1),
        Graph(2);

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
            }
            return tag;
        }

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
        }
        return fragment;
    }

    public abstract void refresh();
}
