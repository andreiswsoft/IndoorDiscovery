package ua.com.sweetsoft.indoordiscovery.db.ormlite;

public class NetworkCursor extends Cursor<Network, Integer>
{

    public NetworkCursor(NetworkDao dao)
    {
        super(dao.getDao());
    }

    public boolean select()
    {
        return (getIterator() != null);
    }
}
