package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.stmt.PreparedDelete;

import java.sql.SQLException;
import java.util.List;

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

    public void delete(T t)
    {
        if (t != null)
        {
            try
            {
                m_dao.delete(t);
            }
            catch (SQLException e)
            {
                Logger.logException(e, "dao.delete(-T-)");
            }
        }
    }

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

    public List<T> getAll()
    {
        List<T> list = null;
        if (m_dao != null)
        {
            try
            {
                list = m_dao.queryForAll();
            }
            catch (SQLException e)
            {
                Logger.logException(e, "DAO.queryForAll()");
            }
        }
        return list;
    }

}
