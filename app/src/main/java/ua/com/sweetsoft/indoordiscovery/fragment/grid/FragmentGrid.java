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

public class FragmentGrid extends Fragment
{
    private static final String ARG_COLUMN_COUNT = "columnCount";

    private int m_columnCount = 1;
    private IGridListener m_listener;
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

            m_adapter = new RecyclerViewAdapter(m_listener);
            recyclerView.setAdapter(m_adapter);
        }

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof IGridListener)
        {
            m_listener = (IGridListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnGridListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        m_listener = null;
    }

    @Override
    public void refresh()
    {
        m_adapter.refresh();
    }

    @Override
    public void reset()
    {
        m_adapter.reset();
    }

}
