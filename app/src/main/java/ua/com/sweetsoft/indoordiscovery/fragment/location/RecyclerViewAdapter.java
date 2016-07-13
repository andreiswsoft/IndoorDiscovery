package ua.com.sweetsoft.indoordiscovery.fragment.location;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.fragment.grid.IGridListener;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewHolder>
{
    private static final int ITEM_VIEW_TYPE_DEFAULT = 0;
    private static final int ITEM_VIEW_TYPE_FOCUSED = 1;

    private List<ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewData> m_viewDataList = new ArrayList<ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewData>();
    private int m_refreshCounter = 0;
    private final IGridListener m_listener;

    public RecyclerViewAdapter(/*IGridListener listener*/)
    {
        m_listener = null;//listener;
    }

    @Override
    public int getItemViewType(int position)
    {
        int viewType = ITEM_VIEW_TYPE_DEFAULT;

        SettingsManager settingsManager = SettingsManager.checkInstance();
        if (settingsManager != null)
        {
            ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewData viewData = m_viewDataList.get(position);
//            if (viewData.getNetworkId() == settingsManager.getFocusNetworkId())
//            {
//                viewType = ITEM_VIEW_TYPE_FOCUSED;
//            }
        }

        return viewType;
    }

    @Override
    public ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_location_item, parent, false);
        return new ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewHolder(view, viewType == ITEM_VIEW_TYPE_FOCUSED);
    }

    @Override
    public void onBindViewHolder(final ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewHolder holder, int position)
    {
        ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewData viewData = m_viewDataList.get(position);
        holder.m_locationId = viewData.getLocationId();
        holder.m_time.setText((new SimpleDateFormat("HH:mm:ss dd.MM.yyyy")).format(new Date(viewData.getTime() * 1000L)));
        holder.m_latitude.setText(String.valueOf(viewData.getLatitude()));
        holder.m_longitude.setText(String.valueOf(viewData.getLongitude()));
        holder.m_altitude.setText(String.valueOf(viewData.getAltitude()));

        holder.m_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_listener != null)
                {
                    //m_listener.onLocationClick(holder.m_locationId);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return m_viewDataList.size();
    }

    public void beginRefresh()
    {
        synchronized (RecyclerViewAdapter.class)
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

    public void endRefresh(List<RecyclerViewData> viewDataList)
    {
        synchronized (RecyclerViewAdapter.class)
        {
            m_viewDataList = viewDataList;
            m_refreshCounter--;
            if (m_refreshCounter != 0)
            {
                runRefreshTask();
            }
        }

        notifyDataSetChanged();
    }

    private void runRefreshTask()
    {
        RefreshAsyncTask task = new RefreshAsyncTask(this);
        task.execute();
    }

}
