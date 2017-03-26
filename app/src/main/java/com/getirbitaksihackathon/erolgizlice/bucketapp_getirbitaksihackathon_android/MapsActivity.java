package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RelativeLayout rl;
    TextView eventNameTV, eventDateTV;
    ArrayList<Event> eventArrayList = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rl = (RelativeLayout) findViewById(R.id.eventView);
        eventNameTV = (TextView) findViewById(R.id.eventNameMap);
        eventDateTV = (TextView) findViewById(R.id.eventTimeMap);

        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("JSON", "");
        if (!json.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String facebook_event_id = object.getString("facebook_event_id");
                    String name = object.getString("name");
                    String start_time = object.getString("start_time");
                    String description = object.getString("description");

                    JSONArray usersArray = object.getJSONArray("users");
                    for (int j = 0; j < usersArray.length(); j++) {
                        String user = usersArray.getString(j);
                    }

                    JSONObject placeObject = object.getJSONObject("place");
                    String facebook_place_id = placeObject.getString("facebook_place_id");
                    String placeName = placeObject.getString("name");

                    JSONObject locationObject = placeObject.getJSONObject("location");
                    String city = locationObject.getString("city");
                    String country = locationObject.getString("country");

                    JSONArray locArray = locationObject.getJSONArray("loc");
                    String latitude = locArray.get(0).toString();
                    String longitude = locArray.get(1).toString();

                    PlaceLocation pl = new PlaceLocation(city, country, latitude, longitude);
                    Place place = new Place(placeName, facebook_place_id, pl);
                    Event e = new Event(description, name, start_time, facebook_event_id, place);

                    eventArrayList.add(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        Log.d("ARRAYLIST", eventArrayList.size() + "");
        for (int i = 0; i < eventArrayList.size(); i++) {
            double latitude = Double.parseDouble(eventArrayList.get(i).place.placeLocation.getLatitude());
            double longitude = Double.parseDouble(eventArrayList.get(i).place.placeLocation.getLongitude());
            LatLng pos = new LatLng(longitude, latitude);

            mMap.addMarker(new MarkerOptions().position(pos).title(eventArrayList.get(i).name));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                eventDateTV.setText("");
                eventNameTV.setText("");
                rl.setVisibility(View.VISIBLE);

                for (int i = 0; i < eventArrayList.size(); i++) {
                    String eventName = eventArrayList.get(i).getName();
                    if (marker.getTitle().equals(eventName)) {
                        String date = eventArrayList.get(i).start_time;
                        eventNameTV.setText(eventName);
                        eventDateTV.setText(date);
                    }
                }
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                rl.setVisibility(View.GONE);
                eventDateTV.setText("");
                eventNameTV.setText("");
            }
        });
    }
}
