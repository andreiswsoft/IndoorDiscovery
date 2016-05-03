package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.stmt.PreparedDelete;

import java.sql.SQLException;

import ua.com.sweetsoft.indoordiscovery.common.Logger;

public abstract class Dao<T>
{
    protected com.j256.ormlite.dao.Dao<T, Integer> m_dao;

    public Dao(com.j256.ormlite.dao.Dao<T, Integer> dao)
    {
        m_dao = dao;
    }

    public com.j256.ormlite.dao.Dao<T, Integer> getDao()
    {
        return m_dao;
    }

    public T create(T t)
    {
        try
        {
            m_dao.create(t);
        }
        catch (SQLException e)
        {
            Logger.logException(e, "dao.create(-\"" + t.getClass().getName() + "\"-");
        }
        return read(t);
    }

    public abstract T read(T t);

    public void delete(PreparedDelete<T> t)
    {
        if (t != null)
        {
            try
            {
                m_dao.delete(t);
            }
            catch (SQLException e)
            {
                Logger.logException(e, "dao.delete(-PreparedDelete-)");
            }
        }
    }
}
