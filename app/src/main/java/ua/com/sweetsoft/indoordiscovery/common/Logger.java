package ua.com.sweetsoft.indoordiscovery.common;

import ua.com.sweetsoft.indoordiscovery.BuildConfig;

public class Logger
{
    private static boolean m_enabled = false;
    private static String m_source;

    public static void enable(boolean boEnable)
    {
        m_enabled = boEnable;
    }

    public static void logError(String error)
    {
        if (m_enabled)
        {
            android.util.Log.e(source(), error);
        }
    }

    public static void logWarning(String warning)
    {
        if (m_enabled)
        {
            android.util.Log.w(source(), warning);
        }
    }

    public static void logInformation(String information)
    {
        if (m_enabled)
        {
            logInformation(source(), information);
        }
    }

    private static void logInformation(String tag, String msg)
    {
        android.util.Log.i(tag, msg);
    }

    public static void logDebug(String debug)
    {
        if (m_enabled)
        {
            android.util.Log.d(source(), debug);
        }
    }

    public static void logVerbose(String verbose)
    {
        if (m_enabled)
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
        if (m_enabled)
        {
            logInformation("Current thread: " + Long.toHexString(Thread.currentThread().getId()));
        }
    }

    public static void logTrace()
    {
        if (m_enabled)
        {
            String className = "?";
            String methodName = "?";
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if (stackTraceElements != null && stackTraceElements.length >= 4)
            {
                className = stackTraceElements[3].getClassName();
                int index = className.lastIndexOf('.');
                if (index >= 0)
                {
                    className = className.substring(index + 1);
                }
                methodName = stackTraceElements[3].getMethodName();
            }
            logInformation(source() + ".trace", className + "." + methodName);
        }
    }

    public static void logException(Exception ex, String data)
    {
        if (m_enabled)
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
}
