package ua.com.sweetsoft.indoordiscovery.wifi;

import android.os.Parcel;
import android.os.Parcelable;

public class AccessPointData extends Data
{
    private String m_name;

    public AccessPointData()
    {
        m_name = "";
    }

    public AccessPointData(int id, String name)
    {
        super(id);

        m_name = name;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String name)
    {
        m_name = name;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        super.writeToParcel(parcel, flags);

        parcel.writeString(m_name);
    }

    private AccessPointData(Parcel parcel)
    {
        super(parcel);

        m_name = parcel.readString();
    }

    public static final Parcelable.Creator<AccessPointData> CREATOR = new Parcelable.Creator<AccessPointData>()
    {
        public AccessPointData createFromParcel(Parcel parcel)
        {
            return new AccessPointData(parcel);
        }

        public AccessPointData[] newArray(int size)
        {
            return new AccessPointData[size];
        }
    };
}
