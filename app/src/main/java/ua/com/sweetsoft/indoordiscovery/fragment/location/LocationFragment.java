package ua.com.sweetsoft.indoordiscovery.fragment.location;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.settings.SettingId;

public class LocationFragment extends ua.com.sweetsoft.indoordiscovery.fragment.Fragment// implements IGridListener
{
    private static final String ARG_COLUMN_COUNT = "columnCount";

    private int m_columnCount = 1;
    private ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewAdapter m_adapter = null;

    public LocationFragment()
    {
    }

    @SuppressWarnings("unused")
    public static LocationFragment newInstance(int columnCount)
    {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            m_columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        if (view instanceof RecyclerView)
        {
            RecyclerView recyclerView = (RecyclerView) view;
            Context context = view.getContext();
            if (m_columnCount <= 1)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(context, m_columnCount));
            }

            m_adapter = new ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewAdapter(/*this*/);
            recyclerView.setAdapter(m_adapter);
        }

        return view;
    }

    @Override
    public void refresh()
    {
        m_adapter.beginRefresh();
    }

//    @Override
//    public void onNetworkClick(int networkId)
//    {
//        m_settingsManager.setFocusNetworkId(networkId);
//    }

    @Override
    public void onSettingChanged(SettingId settingId)
    {
        switch (settingId)
        {
            case FocusNetworkId:
                m_adapter.notifyDataSetChanged();
                break;
        }
    }

}
