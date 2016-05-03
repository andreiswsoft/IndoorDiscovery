package ua.com.sweetsoft.indoordiscovery.wifi;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import ua.com.sweetsoft.indoordiscovery.db.Config;
import ua.com.sweetsoft.indoordiscovery.db.sql.DatabaseHelper;
import ua.com.sweetsoft.indoordiscovery.db.sql.SignalSampleDatabaseHelper;

public class SignalSampleContentProvider extends ContentProvider
{
    private static final String AUTHORITY = ua.com.sweetsoft.indoordiscovery.wifi.ContentProvider.AUTHORITY + ".signalsample";
    private static final Uri AUTHORITY_URI = ua.com.sweetsoft.indoordiscovery.wifi.ContentProvider.AUTHORITY_URI(AUTHORITY);

    private static final String PATH = Config.TABLE_SIGNALSAMPLE;

    static
    {
        m_uriMatcher.addURI(AUTHORITY, PATH, URI);
        m_uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_ID);
    }
    private static final String CONTENT_TYPE = getContentType(AUTHORITY, PATH);
    private static final String CONTENT_ITEM_TYPE = getContentItemType(AUTHORITY, PATH);

    public static final Uri LEVELS_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);

    DatabaseHelper m_databaseHelper;

    public SignalSampleContentProvider()
    {
        super();
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs)
    {
        switch (m_uriMatcher.match(uri))
        {
            case URI:
                break;
            case URI_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                {
                    selection = Config.COLUMN_ID + " = " + id;
                }
                else
                {
                    selection = selection + " AND " + Config.COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int count = 0;
        if (m_databaseHelper.openForWrite())
        {
            count = m_databaseHelper.delete(selection, selectionArgs);
            m_databaseHelper.close();
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri)
    {
        switch (m_uriMatcher.match(uri))
        {
            case URI:
                return CONTENT_TYPE;
            case URI_ID:
                return CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values)
    {
        Uri resultUri = null;

        if (m_uriMatcher.match(uri) == URI)
        {
            if (m_databaseHelper.openForWrite())
            {
                long rowId = m_databaseHelper.insert(null, values);
                resultUri = ContentUris.withAppendedId(LEVELS_URI, rowId);
                m_databaseHelper.close();
                getContext().getContentResolver().notifyChange(resultUri, null);
            }
        }
        else
        {
            throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        return resultUri;
    }

    @Override
    public boolean onCreate()
    {
        m_databaseHelper = new SignalSampleDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        switch (m_uriMatcher.match(uri))
        {
            case URI:
                if (TextUtils.isEmpty(sortOrder))
                {
                    sortOrder = Config.COLUMN_SIGNALSAMPLE_LEVEL + " ASC";
                }
                break;
            case URI_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                {
                    selection = Config.COLUMN_ID + " = " + id;
                }
                else
                {
                    selection = selection + " AND " + Config.COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Cursor cursor = null;
        if (m_databaseHelper.openForRead())
        {
            cursor = m_databaseHelper.query(projection, selection, selectionArgs, null, null, sortOrder);
            if (cursor != null)
            {
                Context context = getContext();
                if (context != null)
                {
                    cursor.setNotificationUri(context.getContentResolver(), LEVELS_URI);
                }
            }
        }
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        switch (m_uriMatcher.match(uri))
        {
            case URI:
                break;
            case URI_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                {
                    selection = Config.COLUMN_ID + " = " + id;
                }
                else
                {
                    selection = selection + " AND " + Config.COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int count = 0;
        if (m_databaseHelper.openForWrite())
        {
            count = m_databaseHelper.update(values, selection, selectionArgs);
            m_databaseHelper.close();
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
