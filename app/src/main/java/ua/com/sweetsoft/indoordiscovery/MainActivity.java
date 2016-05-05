package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ua.com.sweetsoft.indoordiscovery.common.Logger;
import ua.com.sweetsoft.indoordiscovery.db.DatabaseChangeNotifier;
import ua.com.sweetsoft.indoordiscovery.db.IDatabaseChangeListener;
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;
import ua.com.sweetsoft.indoordiscovery.fragment.graph.IGraphListener;
import ua.com.sweetsoft.indoordiscovery.fragment.grid.IGridListener;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsActivity;
import ua.com.sweetsoft.indoordiscovery.settings.SettingsManager;

public class MainActivity extends AppCompatActivity implements IDatabaseChangeListener, IGridListener, IGraphListener
{
    private DatabaseChangeNotifier m_databaseChangeNotifier;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setFragments();

        Logger.enable(true);

        m_databaseChangeNotifier = new DatabaseChangeNotifier(this);
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
    protected void onPostCreate (Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        refreshFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (findViewById(R.id.frameFragment) != null)
        {
            switch (SettingsManager.getInstance(this).getCurrentFragmentType())
            {
                case Grid:
                    menu.removeItem(R.id.main_view_grid);
                    break;
                case Graph:
                    menu.removeItem(R.id.main_view_graph);
                    break;
            }
        }
        else
        {
            menu.removeItem(R.id.main_view_grid);
            menu.removeItem(R.id.main_view_graph);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean boResult = true;

        switch (item.getItemId())
        {
            case R.id.main_view_grid:
                SettingsManager.getInstance(this).setCurrentFragmentType(Fragment.FragmentType.Grid);
                recreate();
                break;
            case R.id.main_view_graph:
                SettingsManager.getInstance(this).setCurrentFragmentType(Fragment.FragmentType.Graph);
                recreate();
                break;
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
    public Context getContext()
    {
        return this;
    }

    @Override
    public void onDatabaseChanged()
    {
        refreshFragments();
    }

    private void refreshFragments()
    {
        Fragment fragment = findFragment(Fragment.FragmentType.Grid);
        if (fragment != null)
        {
            fragment.refresh();
        }
        fragment = findFragment(Fragment.FragmentType.Graph);
        if (fragment != null)
        {
            fragment.refresh();
        }
    }

    private Fragment findFragment(Fragment.FragmentType type)
    {
        return (Fragment) getSupportFragmentManager().findFragmentByTag(type.toTag());
    }

    private Fragment getFragment(Fragment.FragmentType type)
    {
        Fragment fragment = findFragment(type);
        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        else
        {
            fragment = Fragment.create(type);
        }
        return fragment;
    }

    private void setFragments()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        if (findViewById(R.id.frameFragment) != null)
        {
            Fragment.FragmentType type = SettingsManager.getInstance(this).getCurrentFragmentType();
            fragment = getFragment(type);
            fragmentTransaction.replace(R.id.frameFragment, fragment, type.toTag());
        }
        else
        {
            if (findViewById(R.id.frameFragmentGrid) != null)
            {
                Fragment.FragmentType type = Fragment.FragmentType.Grid;
                fragment = getFragment(type);
                fragmentTransaction.replace(R.id.frameFragmentGrid, fragment, type.toTag());
            }
            if (findViewById(R.id.frameFragmentGraph) != null)
            {
                Fragment.FragmentType type = Fragment.FragmentType.Graph;
                fragment = getFragment(type);
                fragmentTransaction.replace(R.id.frameFragmentGraph, fragment, type.toTag());
            }
        }
        fragmentTransaction.commit();
    }
}
