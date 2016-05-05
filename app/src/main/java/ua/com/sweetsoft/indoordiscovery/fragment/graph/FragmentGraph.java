package ua.com.sweetsoft.indoordiscovery.fragment.graph;

import android.content.Context;
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
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.NetworkCursor;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;

public class FragmentGraph extends Fragment
{
    private IGraphListener m_listener;
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

        if (context instanceof IGraphListener)
        {
            m_listener = (IGraphListener) context;
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
    public void refresh()
    {
        m_graph.clear();

        NetworkCursor networkCursor = Network.getCursor();
        if (networkCursor != null)
        {
            while (networkCursor.hasNext())
            {
                Network network = networkCursor.getNext();
                List<SignalSample> signalSamples = network.getSignalSamples(true);
                if (signalSamples != null)
                {
                    List<Integer> levels = new ArrayList<Integer>();
// missed samples
                    for (SignalSample signalSample : signalSamples)
                    {
                        levels.add(signalSample.getLevel());
                    }
                    XYSeries series = new SimpleXYSeries(levels, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, network.getSsid());
                    int lineColor = Color.rgb(10 * network.getId(), 26 * network.getId(), 42 * network.getId());
                    int pointColor = Color.rgb(5 * network.getId(), 12 * network.getId(), 21 * network.getId());
                    LineAndPointFormatter seriesFormat = new LineAndPointFormatter(lineColor, pointColor, null, null);
                    m_graph.addSeries(series, seriesFormat);
                }
            }
            networkCursor.close();
        }
        m_graph.redraw();
    }

}
