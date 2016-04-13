package ua.com.sweetsoft.indoordiscovery.common;

import ua.com.sweetsoft.indoordiscovery.BuildConfig;

public class Logger
{
    private static boolean m_boEnabled = false;
    private static String m_source;

    public static void enable(boolean boEnable)
    {
        m_boEnabled = boEnable;
    }

    public static void logError(String error)
    {
        if (m_boEnabled)
        {
            android.util.Log.e(source(), error);
        }
    }

    public static void logWarning(String warning)
    {
        if (m_boEnabled)
        {
            android.util.Log.w(source(), warning);
        }
    }

    public static void logInformation(String information)
    {
        if (m_boEnabled)
        {
            android.util.Log.i(source(), information);
        }
    }

    public static void logDebug(String debug)
    {
        if (m_boEnabled)
        {
            android.util.Log.d(source(), debug);
        }
    }

    public static void logVerbose(String verbose)
    {
        if (m_boEnabled)
        {
            android.util.Log.v(source(), verbose);
        }
    }

    private static String source()
    {
        if (m_source == null)
        {
            m_source = BuildConfig.APPLICATION_ID;
            int index = m_source.lastIndexOf('.');
            if (index >= 0)
            {
                m_source = m_source.substring(index + 1);
            }
        }
        return m_source;
    }

    public static void logInfoCurrentThread()
    {
        logInformation("Current thread: " + Long.toHexString(Thread.currentThread().getId()));
    }

    public static void logException(Exception ex, String data)
    {
        String className = "?";
        String methodName = "?";
        StackTraceElement element = ex.getStackTrace()[0];
        if (element != null)
        {
            className = element.getClassName();
            methodName = element.getMethodName();
        }
        logError(className + "." + methodName + "(" + data + ") -> " + ex.getMessage());
    }
}
