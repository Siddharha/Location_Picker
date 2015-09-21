package com.rnd;

import android.content.Context;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private GoogleMap map;
    private PlacePicker.IntentBuilder builder;
    private Context context;
    private ImageView mark;
    private Projection projection;
    private int Point_x,Point_y;
    private LatLng latLng;
    private Point x_y_points;
    private TextView txt_place;
    private Geocoder geocoder;
    private List<Address> addresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadMap();
        setPicker();
    }
    private void initialize() {
        mapView = (MapView)findViewById(R.id.view);
        mark = (ImageView)findViewById(R.id.mark);
        txt_place = (TextView)findViewById(R.id.txt_place);
    }
    private void loadMap() {
        MapsInitializer.initialize(MainActivity.this);
        map = mapView.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
       // map.setMyLocationEnabled(true);
        map.setBuildingsEnabled(true);

      //  map.animateCamera(CameraUpdateFactory.zoomBy(10));

    }

    private void setPicker() {

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                pointerHotpoint();

            }
        });


    }

    private void pointerHotpoint() {
        Point_x = (int) (mark.getX()+(mark.getWidth()/2));
        Point_y = (int) (mark.getY()+(mark.getHeight()/2));
        Log.e("Position : ",Point_x+","+Point_y);
        projection = map.getProjection();
        x_y_points = new Point(Point_x,Point_y);
        latLng = map.getProjection().fromScreenLocation(x_y_points);

        double Lat = latLng.latitude;
        double Lng = latLng.longitude;
        Log.e("x = ", String.valueOf(latLng));

        //----------------------------------
        try {
            new GetLocationAsync()
                    .execute(Lat,Lng);

        } catch (Exception e) {
        }
//------------------------------------
      //  txt_place.setText(String.valueOf(Lat));


    }

    //region manu...
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

    //endregion

    private class GetLocationAsync extends AsyncTask<Double, Void, String> {

        // boolean duplicateResponse;
        double x, y;

        @Override
        protected void onPreExecute() {
            txt_place.setText(" Getting location.... ");

        }

        @Override
        protected String doInBackground(Double... params) {

            x = params[0];
            y = params[1];

            try {
                geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                Log.v("addresses", addresses.toString());

            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                txt_place.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1) + " ");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}
