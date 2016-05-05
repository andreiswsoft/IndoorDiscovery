package ua.com.sweetsoft.indoordiscovery.db;

import android.content.Context;
import android.os.Handler;

public interface IDatabaseChangeListener
{
    Context getContext();
    void onDatabaseChanged();
}
