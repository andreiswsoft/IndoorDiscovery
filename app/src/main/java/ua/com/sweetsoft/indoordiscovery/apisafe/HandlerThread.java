package ua.com.sweetsoft.indoordiscovery.apisafe;

import java.lang.reflect.Method;

import ua.com.sweetsoft.indoordiscovery.common.Reflection;

public class HandlerThread extends android.os.HandlerThread
{
    public HandlerThread(String name, int priority)
    {
        super(name, priority);
    }

    @Override
    public boolean quitSafely()
    {
        if (Reflection.isExistsMethodInClass("quitSafely", "android.os.HandlerThread"))
        {
            return super.quitSafely();
        }
        else
        {
            return super.quit();
        }
    }
}
