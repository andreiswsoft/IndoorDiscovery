package ua.com.sweetsoft.indoordiscovery;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.DatabaseChangeNotifier;
import ua.com.sweetsoft.indoordiscovery.db.IDatabaseChangeListener;
import ua.com.sweetsoft.indoordiscovery.fragment.IFragment;
import ua.com.sweetsoft.indoordiscovery.fragment.graph.IGraphListener;
import ua.com.sweetsoft.indoordiscovery.fragment.grid.IGridListener;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements IDatabaseChangeListener, IGridListener, IGraphListener
{
    DatabaseChangeNotifier m_databaseChangeNotifier;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Logger.enable(true);

        m_databaseChangeNotifier = new DatabaseChangeNotifier(this);
        m_databaseChangeNotifier.add(this);
        m_databaseChangeNotifier.start();

        startService(new Intent(this, ScanService.class));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        m_databaseChangeNotifier.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean boResult = true;

        switch (item.getItemId())
        {
            case R.id.main_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                boResult = super.onOptionsItemSelected(item);
                break;
        }

        return boResult;
    }

    public void onNetworkClick(int networkId)
    {

    }

    public void onGraph(Uri uri)
    {

    }

    @Override
    public void onDatabaseChanging()
    {
        IFragment fragment = findFragment(R.id.fragmentGraph);
        if (fragment != null)
        {
            fragment.reset();
        }
        fragment = findFragment(R.id.fragmentGrid);
        if (fragment != null)
        {
            fragment.reset();
        }
    }

    @Override
    public void onDatabaseChanged()
    {
        IFragment fragment = findFragment(R.id.fragmentGraph);
        if (fragment != null)
        {
            fragment.refresh();
        }
        fragment = findFragment(R.id.fragmentGrid);
        if (fragment != null)
        {
            fragment.refresh();
        }
    }

    private IFragment findFragment(int id)
    {
        IFragment fragment = (IFragment) getFragmentManager().findFragmentById(id);
        if (fragment == null)
        {
            fragment = (IFragment) getSupportFragmentManager().findFragmentById(id);
        }
        return fragment;
    }
}
