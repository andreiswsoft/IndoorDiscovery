package ua.com.sweetsoft.indoordiscovery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity
{
    private static final int duration = 2000; // msec
    private Timer m_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        final Context context = this;
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(context, MainActivity.class));
            }
        };
        m_timer = new Timer(true);
        m_timer.schedule(task, duration);
    }
}
