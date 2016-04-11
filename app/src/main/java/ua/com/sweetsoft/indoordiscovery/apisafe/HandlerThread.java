package ua.com.sweetsoft.indoordiscovery.apisafe;

import java.lang.reflect.Method;

public class HandlerThread extends android.os.HandlerThread
{
    public HandlerThread(String name, int priority)
    {
        super(name, priority);
    }

    @Override
    public boolean quitSafely()
    {
        if (isExistsMethodInClass("quitSafely", "android.os.HandlerThread"))
        {
            return super.quitSafely();
        }
        else
        {
            return super.quit();
        }
    }

    public boolean isExistsMethodInClass(String methodName, String className)
    {
        boolean exists = false;

        try
        {
            Class<?> cls = Class.forName(className);
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals(methodName))
                {
                    exists = true;
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
        }

        return exists;
    }
}
