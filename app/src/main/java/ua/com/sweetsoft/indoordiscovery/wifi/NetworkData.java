package ua.com.sweetsoft.indoordiscovery.wifi;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkData extends Data
{
    private String m_ssid;

    public NetworkData()
    {
        m_ssid = "";
    }

    public NetworkData(int id, String ssid)
    {
        super(id);

        m_ssid = ssid;
    }

    public String getSSID()
    {
        return m_ssid;
    }

    public void setSSID(String ssid)
    {
        m_ssid = ssid;
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

        parcel.writeString(m_ssid);
    }

    private NetworkData(Parcel parcel)
    {
        super(parcel);

        m_ssid = parcel.readString();
    }

    public static final Parcelable.Creator<NetworkData> CREATOR = new Parcelable.Creator<NetworkData>()
    {
        public NetworkData createFromParcel(Parcel parcel)
        {
            return new NetworkData(parcel);
        }

        public NetworkData[] newArray(int size)
        {
            return new NetworkData[size];
        }
    };
}
