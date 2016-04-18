package ua.com.sweetsoft.indoordiscovery.fragment;

import android.database.Cursor;

public interface IFragment
{
    void setCursor(Cursor cursor);
    void resetCursor();
}
