package ua.com.sweetsoft.indoordiscovery;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import ua.com.sweetsoft.indoordiscovery.common.Logger;

public class Locator implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private static GoogleApiClient m_googleApiClient;

    private Context m_context;
    private PendingIntent m_pendingIntent;

    public Locator(Context context)
    {
        this.m_context = context;
    }

    public void start()
    {
        if (m_googleApiClient == null)
        {
            GoogleApiClient.Builder builder = new GoogleApiClient.Builder(m_context);
            builder.addConnectionCallbacks(this);
            builder.addOnConnectionFailedListener(this);
            builder.addApi(LocationServices.API);
            m_googleApiClient = builder.build();
        }
        if (!m_googleApiClient.isConnected() && !m_googleApiClient.isConnecting())
        {
            m_googleApiClient.connect();
        }
    }

    public void stop()
    {
        if (m_googleApiClient != null && m_googleApiClient.isConnected() && m_pendingIntent != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(m_googleApiClient, m_pendingIntent);
            m_googleApiClient.disconnect();
        }
    }

    protected void restart()
    {
        stop();
        start();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        try
        {
            Logger.logInformation("Locator.onConnected");
            //if (Build.VERSION.SDK_INT >= 23 &&
            if (ContextCompat.checkSelfPermission(m_context, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
            {
                Logger.logInformation("requestLocationUpdates");
                LocationServices.FusedLocationApi.requestLocationUpdates(m_googleApiClient, getLocationRequest(), getPendingIntent(false));
            }
        }
        catch (Exception e)
        {
            restart();
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Logger.logInformation("Locator.onConnectionSuspended");
        m_googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Logger.logInformation("Locator.onConnectionFailed");
    }

    protected LocationRequest getLocationRequest()
    {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(0);
        return locationRequest;
    }

    public PendingIntent getPendingIntent(boolean force)
    {
        if (m_pendingIntent == null || force)
        {
            Intent intent = new Intent(m_context, LocatorReceiver.class);
            m_pendingIntent = PendingIntent.getBroadcast(m_context, 0, intent, 0);
        }
        return m_pendingIntent;
    }
}
