package ua.com.sweetsoft.indoordiscovery.wifi;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class SignalLevelData extends Data
{
    private int m_accessPointId;
    private int m_level;
    private Timestamp m_timestamp;

    public SignalLevelData()
    {
        m_accessPointId = 0;
        m_level = 0;
        m_timestamp = new Timestamp(0);
    }

    public SignalLevelData(int id, int accessPointId, int level, Timestamp timestamp)
    {
        super(id);

        m_accessPointId = accessPointId;
        m_level = level;
        m_timestamp = timestamp;
    }

    public int getAccessPointId()
    {
        return m_accessPointId;
    }

    public void setAccessPointId(int id)
    {
        m_accessPointId = id;
    }

    public int getLevel()
    {
        return m_level;
    }

    public void setLevel(int level)
    {
        m_level = level;
    }

    public Timestamp getTimestamp()
    {
        return m_timestamp;
    }

    public void setTimestamp(Timestamp timestamp) { m_timestamp = timestamp; }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        super.writeToParcel(parcel, flags);

        parcel.writeInt(m_accessPointId);
        parcel.writeInt(m_level);
        parcel.writeLong(m_timestamp.getTime());
    }

    private SignalLevelData(Parcel parcel)
    {
        super(parcel);

        m_accessPointId = parcel.readInt();
        m_level = parcel.readInt();
        m_timestamp.setTime(parcel.readLong());
    }

    public static final Parcelable.Creator<SignalLevelData> CREATOR = new Parcelable.Creator<SignalLevelData>()
    {
        public SignalLevelData createFromParcel(Parcel parcel)
        {
            return new SignalLevelData(parcel);
        }

        public SignalLevelData[] newArray(int size)
        {
            return new SignalLevelData[size];
        }
    };
}
