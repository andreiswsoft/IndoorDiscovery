package ua.com.sweetsoft.indoordiscovery.fragment.location;

import android.content.res.TypedArray;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ua.com.sweetsoft.indoordiscovery.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder
{
    public int m_locationId;

    public final View m_view;
    public final TextView m_time;
    public final TextView m_latitude;
    public final TextView m_longitude;
    public final TextView m_altitude;

    public RecyclerViewHolder(View view, boolean focused)
    {
        super(view);

        m_locationId = 0;

        m_view = view;
        m_time = (TextView) view.findViewById(R.id.time);
        m_latitude = (TextView) view.findViewById(R.id.latitude);
        m_longitude = (TextView) view.findViewById(R.id.longitude);
        m_altitude = (TextView) view.findViewById(R.id.altitude);

        setStyle(focused);
    }

    @Override
    public String toString()
    {
        return super.toString() + " '" + m_time.getText() + " : " + m_latitude.getText() + " : " + m_longitude.getText() + " : " + m_altitude.getText() + "'";
    }

    private void setStyle(boolean focused)
    {
        setStyleForTime(focused);
        setStyleForLatitude(focused);
        setStyleForLongitude(focused);
        setStyleForAltitude(focused);
    }

    private void setStyleForTime(boolean focused)
    {
        @StyleRes int resId = R.style.Time_Default;
        if (focused)
        {
            resId = R.style.Time_Focused;
        }
        setStyleForTextView(m_time, resId);
    }

    private void setStyleForLatitude(boolean focused)
    {
        @StyleRes int resId = R.style.Latitude_Default;
        if (focused)
        {
            resId = R.style.Latitude_Focused;
        }
        setStyleForTextView(m_latitude, resId);
    }

    private void setStyleForLongitude(boolean focused)
    {
        @StyleRes int resId = R.style.Longitude_Default;
        if (focused)
        {
            resId = R.style.Longitude_Focused;
        }
        setStyleForTextView(m_longitude, resId);
    }

    private void setStyleForAltitude(boolean focused)
    {
        @StyleRes int resId = R.style.Altitude_Default;
        if (focused)
        {
            resId = R.style.Altitude_Focused;
        }
        setStyleForTextView(m_altitude, resId);
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
