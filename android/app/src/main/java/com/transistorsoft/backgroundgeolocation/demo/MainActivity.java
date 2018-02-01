package com.transistorsoft.backgroundgeolocation.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.transistorsoft.locationmanager.adapter.BackgroundGeolocation;
import com.transistorsoft.locationmanager.adapter.Config;
import com.transistorsoft.locationmanager.adapter.callback.TSCallback;
import com.transistorsoft.locationmanager.adapter.callback.TSLocationCallback;
import com.transistorsoft.locationmanager.location.TSLocation;
import com.transistorsoft.locationmanager.logger.TSLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final BackgroundGeolocation adapter = BackgroundGeolocation.getInstance(getApplicationContext(), getIntent());

        ArrayList<String> schedule = new ArrayList<>();

        JSONObject params = new JSONObject();
        try {
            params.put("foo", "bar");
        } catch (JSONException e) {
            TSLog.logger.error(TSLog.error(e.getMessage()));
        }

        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra-value-1");
        } catch (JSONException e) {
            TSLog.logger.error(TSLog.error(e.getMessage()));
        }

        Config.Builder builder = new Config.Builder()
                .setDebug(true)
                .setLogLevel(5)
                .setSchedule(schedule)
                .setForegroundService(true)
                .setParams(params)
                .setHeader("X-FOO", "FOO")
                .setHeader("X-BAR", "BAR")
                .setExtras(extras)
                .setDistanceFilter(50)
                .setDesiredAccuracy(0);

        Config config = builder.build();

        adapter.onMotionChange(new TSLocationCallback() {
            @Override
            public void onLocation(TSLocation location) {
                TSLog.logger.debug("*** [event] motionchange: " + location.getJson());
            }
            @Override
            public void onError(Integer code) {

            }
        });

        adapter.onLocation(new TSLocationCallback() {
            @Override
            public void onLocation(TSLocation location) {
                TSLog.logger.debug("*** [event] location: " + location.toJson());
            }

            @Override
            public void onError(Integer code) {

            }
        });

        adapter.configure(config, new TSCallback() {
            @Override
            public void onSuccess() {
                TSLog.logger.debug("************* configure success");
                adapter.start(new TSCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            }

            @Override
            public void onFailure(String error) {
                TSLog.logger.debug("************** configure FAILURE: " + error);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
