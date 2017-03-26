package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView eventNameTV, eventDateTV;
    Event event;
    Button buttonGoWithSomeone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventNameTV = (TextView) findViewById(R.id.eventNamePage);
        eventDateTV = (TextView) findViewById(R.id.eventTimePage);
        buttonGoWithSomeone = (Button) findViewById(R.id.buttonGoWithSomeone);

        String tag = getIntent().getStringExtra("eventClassTag");

        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("JSON", "");
        if (!json.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String facebook_event_id = object.getString("facebook_event_id");
                    String name = object.getString("name");
                    String start_time = object.getString("start_time");
                    String description = object.has("description") ? object.getString("description") : "";

                    JSONArray usersArray = object.getJSONArray("users");
                    for (int j = 0; j < usersArray.length(); j++) {
                        String user = usersArray.getString(j);
                    }

                    JSONObject placeObject = object.getJSONObject("place");;
                    String facebook_place_id = "";
                    String placeName = "";

                    JSONObject locationObject = placeObject.getJSONObject("location");
                    String city = "";
                    if (object.has("city"))
                        city = object.getString("city");

                    String country = "";
                    if (object.has("country"))
                        country = object.getString("country");

                    if (placeObject.has("name")) {
                        facebook_place_id = placeObject.getString("facebook_place_id");
                        placeName = placeObject.getString("name");
                    }


                    JSONArray locArray = locationObject.getJSONArray("loc");
                    String latitude = locArray.get(0).toString();
                    String longitude = locArray.get(1).toString();
                    PlaceLocation pl = new PlaceLocation(city, country, latitude, longitude);
                    Place place = new Place(placeName, facebook_place_id, pl);
                    Event e = new Event(description, name, start_time, facebook_event_id, place);

                    if (facebook_event_id.equals(tag))
                        this.event = e;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        buttonGoWithSomeone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, MatchActivity.class);
                startActivity(intent);
            }
        });
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

        double latitude = Double.parseDouble(event.getPlace().getPlaceLocation().getLatitude());
        double longitude = Double.parseDouble(event.getPlace().getPlaceLocation().getLongitude());
        LatLng pos = new LatLng(longitude, latitude);

        mMap.addMarker(new MarkerOptions().position(pos).title(event.name));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12.0f));

        eventNameTV.setText(event.getName());
        eventDateTV.setText(event.getStart_time());
    }
}
