package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ua.com.sweetsoft.indoordiscovery.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
{
    private List<RecyclerViewData> m_viewDataList = new ArrayList<RecyclerViewData>();
    private int m_refreshCounter = 0;
    private final IGridListener m_listener;

    public RecyclerViewAdapter(IGridListener listener)
    {
        m_listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_grid_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position)
    {
        RecyclerViewData viewData = m_viewDataList.get(position);
        holder.m_networkId = viewData.getNetworkId();
        holder.m_network.setText(viewData.getNetworkSsid());
        holder.m_signalLevel.setText(viewData.getSignalLevel());

        holder.m_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_listener != null)
                {
                    m_listener.onNetworkClick(holder.m_networkId);
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
