package ua.com.sweetsoft.indoordiscovery.db;

public interface IDatabaseChangeListener
{
    void onDatabaseChanging();
    void onDatabaseChanged();
}
