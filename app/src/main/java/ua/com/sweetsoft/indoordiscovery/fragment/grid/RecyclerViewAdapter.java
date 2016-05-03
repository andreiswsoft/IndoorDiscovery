package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Network;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.NetworkCursor;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.SignalSample;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
{
    private NetworkCursor m_cursor = null;
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
        Network network = m_cursor.moveToPosition(position);
        holder.m_networkId = network.getId();
        holder.m_network.setText(network.getSsid());
        String signalLevel = "-";
        SignalSample signalSample = network.getCurrentSignalSample(holder.m_view.getContext());
        if (signalSample != null)
        {
            signalLevel = String.valueOf(signalSample.getLevel());
        }
        holder.m_signalLevel.setText(signalLevel);

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
        return (m_cursor != null ? (int) m_cursor.getCount() : 0);
    }

    public void refresh()
    {
        if (m_cursor != null)
        {
            m_cursor.close();
            m_cursor = null;
        }
        m_cursor = Network.getCursor();
        if (m_cursor != null)
        {
            notifyDataSetChanged();
        }
    }

    public void reset()
    {
        if (m_cursor != null)
        {
            m_cursor.close();
            m_cursor = null;
        }
    }

}
