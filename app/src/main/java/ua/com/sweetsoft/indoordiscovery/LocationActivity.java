package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.com.sweetsoft.indoordiscovery.db.DatabaseChangeNotifier;
import ua.com.sweetsoft.indoordiscovery.db.IDatabaseChangeListener;
import ua.com.sweetsoft.indoordiscovery.fragment.Fragment;

public class LocationActivity extends AppCompatActivity implements IDatabaseChangeListener
{
    private DatabaseChangeNotifier m_databaseChangeNotifier;
    private Locator m_locator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        setFragments();

        m_databaseChangeNotifier = new DatabaseChangeNotifier(this);
        m_databaseChangeNotifier.start();

        m_locator = new Locator(this);
        m_locator.start();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        m_locator.stop();

        m_databaseChangeNotifier.stop();
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
        Fragment fragment = findFragment(Fragment.FragmentType.Location);
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
            Fragment.FragmentType type = Fragment.FragmentType.Location;
            fragment = getFragment(type);
            fragmentTransaction.replace(R.id.frameFragment, fragment, type.toTag());
        }
        fragmentTransaction.commit();
    }
}
