package ua.com.sweetsoft.indoordiscovery.db.sql;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class SignalSampleData extends Data
{
    private int m_networkId;
    private int m_level;
    private long m_time;

    public SignalSampleData()
    {
        m_networkId = 0;
        m_level = 0;
        m_time = 0;
    }

    public SignalSampleData(int id, int networkId, int level, long time)
    {
        super(id);

        m_networkId = networkId;
        m_level = level;
        m_time = time;
    }

    public int getNetworkId()
    {
        return m_networkId;
    }

    public void setNetworkId(int id)
    {
        m_networkId = id;
    }

    public int getLevel()
    {
        return m_level;
    }

    public void setLevel(int level)
    {
        m_level = level;
    }

    public long getTime()
    {
        return m_time;
    }

    public void setTime(long time) { m_time = time; }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        super.writeToParcel(parcel, flags);

        parcel.writeInt(m_networkId);
        parcel.writeInt(m_level);
        parcel.writeLong(m_time);
    }

    private SignalSampleData(Parcel parcel)
    {
        super(parcel);

        m_networkId = parcel.readInt();
        m_level = parcel.readInt();
        m_time = parcel.readLong();
    }

    public static final Parcelable.Creator<SignalSampleData> CREATOR = new Parcelable.Creator<SignalSampleData>()
    {
        public SignalSampleData createFromParcel(Parcel parcel)
        {
            return new SignalSampleData(parcel);
        }

        public SignalSampleData[] newArray(int size)
        {
            return new SignalSampleData[size];
        }
    };
}
