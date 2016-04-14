package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ua.com.sweetsoft.indoordiscovery.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder
{
    public int m_networkId;

    public final View m_view;
    public final TextView m_network;
    public final TextView m_signalLevel;

    public RecyclerViewHolder(View view)
    {
        super(view);

        m_networkId = 0;

        m_view = view;
        m_network = (TextView) view.findViewById(R.id.network);
        m_signalLevel = (TextView) view.findViewById(R.id.signalLevel);
    }

    @Override
    public String toString()
    {
        return super.toString() + " '" + m_network.getText() + " : " + m_signalLevel.getText() + "'";
    }
}
