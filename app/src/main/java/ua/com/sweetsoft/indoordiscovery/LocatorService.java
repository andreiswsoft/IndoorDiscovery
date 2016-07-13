package ua.com.sweetsoft.indoordiscovery;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.LocationResult;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelperFactory;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.Location;
import ua.com.sweetsoft.indoordiscovery.db.ormlite.LocationDao;

public class LocatorService extends IntentService
{
    public LocatorService()
    {
        super("LocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (LocationResult.hasResult(intent))
        {
            final LocationResult locationResult = LocationResult.extractResult(intent);
            if (locationResult != null)
            {
                android.location.Location loc = locationResult.getLastLocation();
                addLocation(loc);
                Logger.logInformation(loc.toString());
            }
            else
            {
                Logger.logInformation("null location");
            }
        }
        else
        {
            Logger.logInformation("no location");
        }

        Intent serviceIntent = new Intent(this, DatabaseUpdateService.class);
        serviceIntent.fillIn(intent, Intent.FILL_IN_DATA);
        WakefulReceiver.startWakefulService(this, serviceIntent);

        WakefulReceiver.completeWakefulIntent(intent);
    }

    protected Location addLocation(android.location.Location loc)
    {
        Location location = null;

        LocationDao locationDao = DatabaseHelperFactory.getHelper().getLocationDao();
        if (locationDao != null)
        {
            location = locationDao.create(new Location(loc.getLatitude(), loc.getLongitude(), loc.hasAltitude() ? loc.getAltitude() : Double.NaN, loc.getTime()/1000L));
        }
        return location;
    }
}
