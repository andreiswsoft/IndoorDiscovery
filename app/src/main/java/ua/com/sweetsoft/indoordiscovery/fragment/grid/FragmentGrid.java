package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class FragmentGrid extends Fragment implements IGridListener
{
    private static final String ARG_COLUMN_COUNT = "columnCount";

    private int m_columnCount = 1;
    private RecyclerViewAdapter m_adapter = null;

    public FragmentGrid()
    {
    }

    @SuppressWarnings("unused")
    public static FragmentGrid newInstance(int columnCount)
    {
        FragmentGrid fragment = new FragmentGrid();
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
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

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

            m_adapter = new RecyclerViewAdapter(this);
            recyclerView.setAdapter(m_adapter);
        }

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public void refresh()
    {
        m_adapter.beginRefresh();
    }

    @Override
    public boolean onNetworkClick(int networkId)
    {
        boolean updated = false;
        SettingsManager manager = SettingsManager.checkInstance();
        if (manager != null)
        {
            if (manager.getFocusNetworkId() != networkId)
            {
                manager.setFocusNetworkId(networkId);
                updated = true;
            }
        }
        return updated;
    }

}
