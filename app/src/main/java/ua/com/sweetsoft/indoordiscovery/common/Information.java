package ua.com.sweetsoft.indoordiscovery.common;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

import ua.com.sweetsoft.indoordiscovery.BuildConfig;

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

    public static String getProcessName(Context context)
    {
        String name = null;

        int pid = android.os.Process.myPid();

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos != null)
        {
            for (ActivityManager.RunningAppProcessInfo info : infos)
            {
                if (info.pid == pid)
                {
                    name = info.processName;
                    break;
                }
            }
        }
        return name;
    }

    public static String getApplicationName()
    {
        return BuildConfig.APPLICATION_ID;
    }

    public static boolean isApplicationMainProcess(Context context)
    {
        return getApplicationName().equals(getProcessName(context));
    }
}
