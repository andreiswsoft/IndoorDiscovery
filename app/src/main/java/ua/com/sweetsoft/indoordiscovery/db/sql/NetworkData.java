package ua.com.sweetsoft.indoordiscovery.db.sql;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkData extends Data
{
    private String m_ssid;
    private String m_bssid;

    public NetworkData()
    {
        m_ssid = "";
        m_bssid = "";
    }

    public NetworkData(int id, String ssid, String bssid)
    {
        super(id);

        m_ssid = ssid;
        m_bssid = bssid;
    }

    public String getSSID()
    {
        return m_ssid;
    }

    public void setSSID(String ssid)
    {
        m_ssid = ssid;
    }

    public String getBSSID()
    {
        return m_bssid;
    }

    public void setBSSID(String bssid)
    {
        m_bssid = bssid;
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
        parcel.writeString(m_bssid);
    }

    private NetworkData(Parcel parcel)
    {
        super(parcel);

        m_ssid = parcel.readString();
        m_bssid = parcel.readString();
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
