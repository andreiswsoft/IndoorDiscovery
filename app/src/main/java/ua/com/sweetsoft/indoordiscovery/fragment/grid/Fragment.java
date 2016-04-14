package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.wifi.NetworkContentProvider;

public class Fragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int LOADER_ID = 1;
    private static final String ARG_COLUMN_COUNT = "columnCount";

    private int m_columnCount = 1;
    private OnFragmentInteractionListener m_listener;
    private RecyclerViewAdapter m_adapter = null;

    public Fragment()
    {
    }

    @SuppressWarnings("unused")
    public static Fragment newInstance(int columnCount)
    {
        Fragment fragment = new Fragment();
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

        getLoaderManager().initLoader(LOADER_ID, null, this);
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

        if (context instanceof OnFragmentInteractionListener)
        {
            m_listener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        m_listener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(getContext(), NetworkContentProvider.NETWORKS_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        switch (loader.getId())
        {
            case LOADER_ID:
                m_adapter.swapCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        switch (loader.getId())
        {
            case LOADER_ID:
                m_adapter.swapCursor(null);
                break;
        }
    }
}
