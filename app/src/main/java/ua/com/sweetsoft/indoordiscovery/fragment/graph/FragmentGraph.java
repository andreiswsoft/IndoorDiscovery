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
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;
import ua.com.sweetsoft.indoordiscovery.settings.SettingId;

public class FragmentGraph extends Fragment
{
    private List<SerieData> m_dataList = new ArrayList<SerieData>();
    private int m_refreshCounter = 0;
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
        beginRefresh();
    }

    public void beginRefresh()
    {
        synchronized (FragmentGraph.class)
        {
            if (m_refreshCounter == 0)
            {
                runRefreshTask();
            }
            if (m_refreshCounter < 2)
            {
                m_refreshCounter++;
            }
        }
    }

    public void endRefresh(List<SerieData> viewDataList)
    {
        synchronized (FragmentGraph.class)
        {
            m_dataList = viewDataList;
            m_refreshCounter--;
            if (m_refreshCounter != 0)
            {
                runRefreshTask();
            }
        }

        refreshGraph();
    }

    private void runRefreshTask()
    {
        RefreshAsyncTask task = new RefreshAsyncTask(this);
        task.execute();
    }

    private void refreshGraph()
    {
        m_graph.clear();

 // missed samples
        for (SerieData data : m_dataList)
        {
            XYSeries series = new SimpleXYSeries(data.getSignalLevels(), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, data.getNetworkSsid());
            int lineColor = Color.rgb(10 * data.getNetworkId(), 26 * data.getNetworkId(), 42 * data.getNetworkId());
            int pointColor = Color.rgb(5 * data.getNetworkId(), 12 * data.getNetworkId(), 21 * data.getNetworkId());
            LineAndPointFormatter seriesFormat = new LineAndPointFormatter(lineColor, pointColor, null, null);
            m_graph.addSeries(series, seriesFormat);
        }
        m_graph.redraw();
    }

    @Override
    public void onSettingChanged(SettingId settingId)
    {
        switch (settingId)
        {
            case FocusNetworkId:

                break;
        }
    }

}
