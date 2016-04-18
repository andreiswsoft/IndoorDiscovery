package ua.com.sweetsoft.indoordiscovery.fragment.graph;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.fragment.IFragment;
import ua.com.sweetsoft.indoordiscovery.wifi.NetworkDatabaseCursorHelper;
import ua.com.sweetsoft.indoordiscovery.wifi.SignalLevelDatabaseCursorHelper;
import ua.com.sweetsoft.indoordiscovery.wifi.SignalLevelDatabaseHelper;

public class FragmentGraph extends android.support.v4.app.Fragment implements IFragment
{
    private OnGraphListener m_listener;
    private XYPlot m_graph;

    public FragmentGraph()
    {
    }

    public static FragmentGraph newInstance(String param1, String param2)
    {
        FragmentGraph fragment = new FragmentGraph();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = (View) inflater.inflate(R.layout.fragment_graph, container, false);

        m_graph = (XYPlot) view.findViewById(R.id.graph);
        m_graph.setTicksPerRangeLabel(3);

        return view;
    }

    /*public void onButtonPressed(Uri uri)
    {
        if (m_listener != null)
        {
            m_listener.onGraph(uri);
        }
    }*/

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof OnGraphListener)
        {
            m_listener = (OnGraphListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnGraphListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        m_listener = null;
    }

    @Override
    public void setCursor(Cursor cursor)
    {
        m_graph.clear();

        NetworkDatabaseCursorHelper networkCursorHelper = new NetworkDatabaseCursorHelper(cursor);
        SignalLevelDatabaseHelper signalLevelDbHelper = new SignalLevelDatabaseHelper(getContext());
        if (signalLevelDbHelper.openForRead())
        {
            for (boolean net = networkCursorHelper.moveToFirst(); net; net = networkCursorHelper.moveToNext())
            {
                int networkId = networkCursorHelper.getId();

                List<Integer> levels = new ArrayList<Integer>();
                String selection = SignalLevelDatabaseHelper.COLUMN_NETWORK_ID + " = " + String.valueOf(networkId);
                String orderBy = SignalLevelDatabaseHelper.COLUMN_TIMESTAMP + " ASC";
                SignalLevelDatabaseCursorHelper signalLevelCursorHelper = new SignalLevelDatabaseCursorHelper(signalLevelDbHelper.query(null, selection, null, null, null, orderBy));
                for (boolean lvl = signalLevelCursorHelper.moveToFirst(); lvl; lvl = signalLevelCursorHelper.moveToNext())
                {
                    levels.add(signalLevelCursorHelper.getLevel());
                }
                signalLevelCursorHelper.close();

                XYSeries series = new SimpleXYSeries(levels, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, networkCursorHelper.getSSID());
                int lineColor = Color.rgb(10 * networkId, 26 * networkId, 42 * networkId);
                int pointColor = Color.rgb(5 * networkId, 12 * networkId, 21 * networkId);
                LineAndPointFormatter seriesFormat = new LineAndPointFormatter(lineColor, pointColor, null, null);
                m_graph.addSeries(series, seriesFormat);
            }
            m_graph.redraw();
            signalLevelDbHelper.close();
        }
    }

    @Override
    public void resetCursor()
    {
        m_graph.clear();
    }

}
