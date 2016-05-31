package com.Webtrekk.SDKTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.Webtrekk.SDKTest.SimpleHTTPServer.HttpServer;
import com.webtrekk.webtrekksdk.Webtrekk;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.IOException;


public class MainActivity extends Activity {
    private Webtrekk webtrekk;
    private HttpServer mHttpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mHttpServer = new HttpServer();
            mHttpServer.setContext(getApplicationContext());
            mHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);


        mediaCodeReceiverRegister();

        webtrekk = Webtrekk.getInstance();
        webtrekk.initWebtrekk(getApplication(), R.raw.webtrekk_config_normal_track);

        webtrekk.getCustomParameter().put("own_para", "my-value");

        ((TextView)findViewById(R.id.main_version)).setText(getString(R.string.hello_world) + "\nLibrary Version:" + Webtrekk.TRACKING_LIBRARY_VERSION_UA);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, "9e956a2e5169ddb44eb87b6acb0eee95");
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHttpServer.stop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        mediaCodeReceiverUnRegister();
        super.onDestroy();
    }


    private BroadcastReceiver mSDKReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String mediaCode = intent.getStringExtra("INSTALL_SETTINGS_MEDIA_CODE");
            String advID = intent.getStringExtra("INSTALL_SETTINGS_ADV_ID");

            Log.d(getClass().getName(),"Broad cast message from SDK is received");

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Media Code")
                    .setMessage("Media code is received: " + mediaCode + "\nAdv id is: " + advID)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();

        }
    };

    /**
     * This is just for testing. To receive Campain installation data
     */
    private void mediaCodeReceiverRegister()
    {
        LocalBroadcastManager.getInstance(this).registerReceiver(mSDKReceiver,
                new IntentFilter("com.Webtrekk.CampainMediaMessage"));
    }

    /**
     * This is just for testing. To receive Campain installation data
     */
    private void mediaCodeReceiverUnRegister()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSDKReceiver);
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

    /** Called when the user clicks the Page Example Activity Button button */
    public void showPageExampleActivity(View view) {
        Intent intent = new Intent(this, PageExampleActivity.class);
        startActivity(intent);
    }

    public void showShopExampleActivity(View view) {
        Intent intent = new Intent(this, ShopExampleActivity.class);
        startActivity(intent);
    }

    public void showMediaExampleActivity(View view) {
        Intent intent = new Intent(this, MediaExampleActivity.class);
        startActivity(intent);
    }

    public void sendCDBRequest(View view)
    {
        Intent intent = new Intent(this, CDBActivityTest.class);
        startActivity(intent);
    }

    public void pushTest(View view)
    {
        Intent intent = new Intent(this, PushNotificationActivity.class);
        startActivity(intent);
    }

    public void recommendationTest(View view)
    {
        Intent intent = new Intent(this, RecommendationActivity.class);
        intent.putExtra(RecommendationActivity.RECOMMENDATION_NAME, "complexReco");
        intent.putExtra(RecommendationActivity.RECOMMENDATION_PRODUCT_ID, "085cc2g007");
        startActivity(intent);
    }

    public Webtrekk getWebtrekk() {
        return webtrekk;
    }
}
