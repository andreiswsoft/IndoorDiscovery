package ua.com.sweetsoft.indoordiscovery.wifi;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Data implements Parcelable
{
    protected int m_id;

    protected Data()
    {
        m_id = 0;
    }

    protected Data(int id)
    {
        m_id = id;
    }

    public int getId()
    {
        return m_id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeInt(m_id);
    }

    protected Data(Parcel parcel)
    {
        m_id = parcel.readInt();
    }
}
