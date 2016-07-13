package ua.com.sweetsoft.indoordiscovery.db;

import android.provider.BaseColumns;

public interface Config extends BaseColumns
{
    String DATABASE_NAME = "database.db";
    int DATABASE_VERSION = 2;

    // table common
    String COLUMN_ID = BaseColumns._ID;
    int COLUMN_ID_INDEX = 0;

    // Network table
    String TABLE_NETWORK = "network";
    String COLUMN_NETWORK_SSID = "ssid";
    String COLUMN_NETWORK_BSSID = "bssid";
    int COLUMN_NETWORK_SSID_INDEX = 1;
    int COLUMN_NETWORK_BSSID_INDEX = 2;

    // Signal sample table
    String TABLE_SIGNALSAMPLE = "signalsample";
    String COLUMN_SIGNALSAMPLE_NETWORK_ID = "network";
    String COLUMN_SIGNALSAMPLE_LEVEL = "level";
    String COLUMN_SIGNALSAMPLE_TIME = "time";
    int COLUMN_SIGNALSAMPLE_NETWORK_ID_INDEX = 1;
    int COLUMN_SIGNALSAMPLE_LEVEL_INDEX = 2;
    int COLUMN_SIGNALSAMPLE_TIME_INDEX = 3;

    // Location table
    String TABLE_LOCATION = "location";
    String COLUMN_LOCATION_LATITUDE = "latitude";
    String COLUMN_LOCATION_LONGITUDE = "longitude";
    String COLUMN_LOCATION_ALTITUDE = "altitude";
    String COLUMN_LOCATION_TIME = "time";
    int COLUMN_LOCATION_LATITUDE_INDEX = 1;
    int COLUMN_LOCATION_LONGITUDE_INDEX = 2;
    int COLUMN_LOCATION_ALTITUDE_INDEX = 3;
    int COLUMN_LOCATION_TIME_INDEX = 4;
}
