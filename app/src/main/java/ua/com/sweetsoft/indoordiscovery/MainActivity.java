package ua.com.sweetsoft.indoordiscovery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.com.sweetsoft.indoordiscovery.fragment.grid.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, ScanService.class));
    }

    public void onNetworkClick(int networkId)
    {

    }
}
