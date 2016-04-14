package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.UriMatcher;
import android.net.Uri;

import ua.com.sweetsoft.indoordiscovery.common.Logger;

public abstract class ContentProvider extends android.content.ContentProvider
{
    protected static final String AUTHORITY = "ua.com.sweetsoft.indoordiscovery.provider";
    protected static final Uri AUTHORITY_URI(String authority) { return Uri.parse("content://" + authority); }

    protected static final int URI = 1;
    protected static final int URI_ID = 2;
    protected static final UriMatcher m_uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    protected static final String getContentType(String authority, String path)
    {
        return ("vnd.android.cursor.dir/vnd." + authority + "." + path);
    }
    protected static final String getContentItemType(String authority, String path)
    {
        return ("vnd.android.cursor.item/vnd." + authority + "." + path);
    }

    protected ContentProvider()
    {
        Logger.enable(true);
    }

}
