package in.dinnernight.broadcasttest;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


import android.app.FragmentManager;
import android.app.ListFragment;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    DBHelper mydb;
    private Intent intent;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.Add);
        b1.setOnClickListener(this);
        Button b2 = (Button) findViewById(R.id.start);
        b2.setOnClickListener(this);
        Button b3 = (Button) findViewById(R.id.stop);
        b3.setOnClickListener(this);
        b3.setClickable(false);
        mydb = new DBHelper(this);






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.item1:Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
                intent.putExtras(dataBundle);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if(v.getId()==R.id.Add) {
            Intent intent = new Intent(this, DisplayContact.class);
            startActivity(intent);
        }

        if(v.getId()==R.id.start){
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isNetworkEnabled)
            {
                CharSequence text = "Please enable network";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else{
                if(!isGPSEnabled)
                {
                    CharSequence text = "Please enable gps";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    intent = new Intent(this, BroadcastService.class);
                    startService(intent);
                    Button b3 = (Button) findViewById(R.id.stop);

                    b3.setClickable(true);

                    CharSequence text = "Service started succesfully!!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }

        }

        if(v.getId()==R.id.stop){
            intent = new Intent(this, BroadcastService.class);
            stopService(intent);
            CharSequence text = "Service stopped";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
    }


}