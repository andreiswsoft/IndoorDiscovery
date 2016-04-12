package ua.com.sweetsoft.indoordiscovery.common;

import java.lang.reflect.Method;

public class Reflection
{
    public static boolean isExistsMethodInClass(String methodName, String className)
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

    public static boolean isExistsFieldInClass(String fieldName, String className)
    {
        boolean exists = false;

        try
        {
            Class<?> cls = Class.forName(className);
            cls.getField(fieldName);
            exists = true;
        }
        catch (ClassNotFoundException ex)
        {
        }
        catch (NoSuchFieldException ex)
        {
        }

        return exists;
    }
}
