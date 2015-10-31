package in.dinnernight.broadcasttest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


public class BroadcastService extends Service implements SensorEventListener, LocationListener {
    private DBHelper mydb ;

    //Empty constructor
    public BroadcastService()
    {

    }

    private float lastX=0, lastY=0, lastZ=0;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;




    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 ; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;



    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "in.dinnernight.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    Intent intent;
    static int counter = 0;




    @Override
    public void onCreate() {
        super.onCreate();
        mydb = new DBHelper(getApplicationContext());
        intent = new Intent(BROADCAST_ACTION);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {


            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the change of the x,y,z values of the accelerometer
        float x=event.values[0];
        float y=event.values[1];
        float z=event.values[2];

        deltaX = Math.abs(lastX - x);
        deltaY = Math.abs(lastY - y);
        deltaZ = Math.abs(lastZ - z);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if (deltaZ < 10)
            deltaZ = 0;

        if(deltaX >50 || deltaY>50 || deltaZ >50) {
            DisplayLoggingInfo();
        }

        lastX=x;
        lastY=y;
        lastZ=z;

    }

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");
        //  intent.putExtra("time", String.valueOf(deltaZ));
        intent.putExtra("currentX", Float.toString(deltaX));
        intent.putExtra("currentY", Float.toString(deltaY));
        intent.putExtra("currentZ", Float.toString(deltaZ));
        if (deltaX > 10) {
            deltaXMax = deltaX;
            intent.putExtra("maxX", Float.toString(deltaXMax));

        }
        if (deltaY > 10) {
            deltaYMax = deltaY;
            intent.putExtra("maxY", Float.toString(deltaYMax));
        }
        if (deltaZ > 10) {
            deltaZMax = deltaZ;
            intent.putExtra("maxZ", Float.toString(deltaZMax));
        }


            Location l=getLocation();
            if(l !=null)
            {

                    sendMessage(l);


            }

        sendBroadcast(intent);
    }



    private void sendMessage( Location loc) {
        if(counter==0) {
        StringBuilder sb = new StringBuilder();
        sb.append("It's an emergency, please help !!!\n");

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                sb.append(listAddresses.get(0).getAddressLine(0));
                sb.append(listAddresses.get(0).getAddressLine(1));
                sb.append(listAddresses.get(0).getAddressLine(2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int num=mydb.numberOfRows();
       int id=1;
       while(num>0)
      {

           String phoneNo = mydb.getPhoneNumbers(id);

            Log.i("Send SMS", "");



            try {

                SmsManager smsManager = SmsManager.getDefault();

                    smsManager.sendTextMessage(phoneNo, null, sb.toString(), null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            id++;
            num--;
        }
            counter++;
        }
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                if(!isNetworkEnabled)
                {
                    //To add the code to enable network programatically
                }
                else if(!isGPSEnabled) {
                    //To add the code to enable GPS programatically

            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
