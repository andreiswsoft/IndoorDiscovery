package ua.com.sweetsoft.indoordiscovery.common;

public class Information
{
    public static int getApiLevel()
    {
        if (Reflection.isExistsFieldInClass("SDK_INT", "android.os.Build$VERSION"))
        {
            return android.os.Build.VERSION.SDK_INT;
        }
        else
        {
            return Integer.valueOf(android.os.Build.VERSION.SDK);
        }
    }
}
