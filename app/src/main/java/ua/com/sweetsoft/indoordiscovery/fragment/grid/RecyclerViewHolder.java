package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import ua.com.sweetsoft.indoordiscovery.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder
{
    public int m_networkId;

    public final View m_view;
    public final TextView m_network;
    public final TextView m_signalLevel;

    public RecyclerViewHolder(View view, boolean focused)
    {
        super(view);

        m_networkId = 0;

        m_view = view;
        m_network = (TextView) view.findViewById(R.id.network);
        m_signalLevel = (TextView) view.findViewById(R.id.signalLevel);

        setStyle(focused);
    }

    @Override
    public String toString()
    {
        return super.toString() + " '" + m_network.getText() + " : " + m_signalLevel.getText() + "'";
    }

    private void setStyle(boolean focused)
    {
        setStyleForNetwork(focused);
        setStyleForSignalLevel(focused);
    }

    private void setStyleForNetwork(boolean focused)
    {
        @StyleRes int resId = R.style.Network_Default;
        if (focused)
        {
            resId = R.style.Network_Focused;
        }
        setStyleForTextView(m_network, resId);

/*        m_network.setSingleLine(true);
        m_network.setMaxLines(1);
        m_network.setHorizontalFadingEdgeEnabled(true);
        m_network.setHorizontallyScrolling(true);
        m_network.setEnabled(true);
        m_network.setFocusable(true);
        m_network.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        m_network.setMarqueeRepeatLimit(-1);*/
    }

    private void setStyleForSignalLevel(boolean focused)
    {
        @StyleRes int resId = R.style.SignalLevel_Default;
        if (focused)
        {
            resId = R.style.SignalLevel_Focused;
        }
        setStyleForTextView(m_signalLevel, resId);
    }

    @SuppressWarnings("ResourceType")
    private void setStyleForTextView(TextView textView, @StyleRes int resId)
    {
        int[] attrs = {
                android.R.attr.textColor,
                android.R.attr.background};
        TypedArray typedArray = m_view.getContext().obtainStyledAttributes(resId, attrs);
        if (typedArray != null)
        {
            textView.setTextColor(typedArray.getColor(0, 0));
            textView.setBackgroundColor(typedArray.getColor(1, 0));

            typedArray.recycle();
        }
    }

}
