package ua.com.sweetsoft.indoordiscovery;

import ua.com.sweetsoft.indoordiscovery.db.ormlite.DatabaseHelperFactory;

public class Application extends android.app.Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        DatabaseHelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate()
    {
        DatabaseHelperFactory.releaseHelper();

        super.onTerminate();
    }
}
