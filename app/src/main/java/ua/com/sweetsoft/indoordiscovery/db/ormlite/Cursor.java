package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import ua.com.sweetsoft.indoordiscovery.common.Logger;

public class Cursor<T, ID>
{
    protected Dao<T, ID> m_dao = null;
    protected CloseableIterator<T> m_iterator = null;
    protected int m_position = -1;

    public Cursor(Dao<T, ID> dao)
    {
        m_dao = dao;
    }

    @Override
    protected void finalize() throws Throwable
    {
        try
        {
            close();
        }
        finally
        {
            super.finalize();
        }
    }

    public void close()
    {
        if (m_iterator != null)
        {
            m_iterator.closeQuietly();
            m_iterator = null;
            m_position = -1;
        }
    }

    public long getCount()
    {
        long count = 0;
        if (m_dao != null)
        {
            try
            {
                count = m_dao.countOf();
            }
            catch (SQLException e)
            {
                Logger.logException(e, "DAO.countOf()");
            }
        }
        return count;
    }

    public boolean hasNext()
    {
        return (getIterator() != null && getIterator().hasNext());
    }

    public T getNext()
    {
        T t = null;
        if (getIterator() != null)
        {
            t = getIterator().next();
            m_position++;
        }
        return t;
    }

    public T moveToPosition(int position)
    {
        T t = null;
        if (getIterator() != null)
        {
            int offset = position - m_position;
            try
            {
                t = getIterator().moveRelative(offset);
                m_position = position;
            }
            catch (SQLException e)
            {
                Logger.logException(e, "CloseableIterator.moveRelative(" + String.valueOf(offset) + ")");
            }
        }
        return t;
    }

    protected CloseableIterator<T> getIterator()
    {
        if (m_iterator == null && m_dao != null)
        {
            m_iterator = m_dao.iterator();
            m_position = 0;
        }
        return m_iterator;
    }

}
