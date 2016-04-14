package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Timestamp;

import ua.com.sweetsoft.indoordiscovery.R;
import ua.com.sweetsoft.indoordiscovery.wifi.NetworkDatabaseCursorHelper;
import ua.com.sweetsoft.indoordiscovery.wifi.SignalLevelDatabaseCursorHelper;
import ua.com.sweetsoft.indoordiscovery.wifi.SignalLevelDatabaseHelper;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
{
    private final OnFragmentInteractionListener m_listener;
    Cursor m_cursor = null;

    public RecyclerViewAdapter(OnFragmentInteractionListener listener)
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
        m_cursor.moveToPosition(position);
        NetworkDatabaseCursorHelper networkCursorHelper = new NetworkDatabaseCursorHelper(m_cursor);

        holder.m_networkId = networkCursorHelper.getId();
        holder.m_network.setText(networkCursorHelper.getSSID());
        holder.m_signalLevel.setText(getSignalLevel(holder.m_view.getContext(), holder.m_networkId));

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
        return (m_cursor != null ? m_cursor.getCount() : 0);
    }

    public Cursor swapCursor(Cursor cursorNew)
    {
        Cursor cursorOld = m_cursor;

        if (m_cursor != cursorNew)
        {
            m_cursor = cursorNew;
            if (cursorNew != null)
            {
                notifyDataSetChanged();
            }
        }

        return cursorOld;
    }

    private String getSignalLevel(Context context, int networkId)
    {
        String value = "-";

        SignalLevelDatabaseHelper databaseHelper = new SignalLevelDatabaseHelper(context);
        if (databaseHelper.openForRead())
        {
            String selection = SignalLevelDatabaseHelper.COLUMN_NETWORK_ID + " = " + String.valueOf(networkId);
            String orderBy = SignalLevelDatabaseHelper.COLUMN_TIMESTAMP + " DESC";
            Cursor cursor = databaseHelper.query(null, selection, null, null, null, orderBy);
            SignalLevelDatabaseCursorHelper cursorHelper = new SignalLevelDatabaseCursorHelper(cursor);
            if (cursorHelper.moveToData())
            {
                Timestamp valueTimestamp = cursorHelper.getTimestamp();
                if (valueTimestamp.after(getLastScanTimestamp()))
                {
                    value = String.valueOf(cursorHelper.getLevel());
                }
            }
            cursorHelper.close();
            databaseHelper.close();
        }

        return value;
    }

    private Timestamp getLastScanTimestamp()
    {
        long scanTime = 0;//System.currentTimeMillis() - 'период сканирования';
        return new Timestamp(scanTime);
    }

}
