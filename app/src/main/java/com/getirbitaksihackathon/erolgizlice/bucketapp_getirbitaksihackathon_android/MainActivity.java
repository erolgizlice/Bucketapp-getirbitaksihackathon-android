package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private ArrayList<Event> eventList = new ArrayList<Event>();
    private ListViewAdapter eventListAdapter;
    private ListView listView;

    String json = "";

    LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,   // 3 sec
                    10, this);
            //myLocation = new LatLng(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
            myLocation = new LatLng(41.078607, 29.022397);

            /*new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + Profile.getCurrentProfile().getId() + "/events?limit=5000",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {

                            try {
                                JSONArray mainObjectArray = response.getJSONObject().getJSONArray("data");

                                for (int i = 0; i < 1; i++) {
                                    Log.d("EventsCount", mainObjectArray.length()+"");

                                    JSONObject mainObject = new JSONObject(mainObjectArray.get(i).toString());
                                    String eventName = mainObject.getString("name");

                                    JSONObject placeObject = mainObject.getJSONObject("place");
                                    String placeName = placeObject.getString("name");

                                    Log.d("asdad", eventName + placeName);

                                    json += mainObjectArray.get(i).toString();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                                int maxLogSize = 1000;
                                for(int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                    int start = i * maxLogSize;
                                    int end = (i+1) * maxLogSize;
                                    end = end > response.toString().length() ? response.toString().length() : end;
                                    Log.v("asas", response.toString().substring(start, end));
                                }
                        }
                    }
            ).executeAsync();*/

            AsyncTaskGetEvents asyncT = new AsyncTaskGetEvents();
            asyncT.execute();

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/1536745433010103/attending?limit=5000",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {
                                JSONArray mainObjectArray = response.getJSONObject().getJSONArray("data");

                                Log.d("AttendingCount", mainObjectArray.length() + "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.listViewToDoList);

        eventListAdapter = new ListViewAdapter(MainActivity.this, eventList);
        listView.setAdapter(eventListAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
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

    public ArrayList<Event> getEventList() {
        return this.eventList;
    }

    class AsyncTaskGetEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient hc = new DefaultHttpClient();
            String message;

            HttpPost p = new HttpPost("https://bucketapp-getirbitaksi.herokuapp.com/getEvent");

            try {
                //"{ \"facebook_user_id\":\""+id+"\",\"events\":" +
                //message = "{ \"facebook_user_id\":\""+id+"\",\"events\":" +jsonObject.get("data").toString()+" }";
                message = "{ \"latitude\": \"" + myLocation.latitude + "\", " +
                        "\"longitude\": \"" + myLocation.longitude + "\" }";


                p.setEntity(new StringEntity(message, "UTF8"));
                p.setHeader("Content-type", "application/json");
                HttpResponse resp = hc.execute(p);
                if (resp != null) {
                    if (resp.getStatusLine().getStatusCode() == 204)
                        Log.d("serverPost", "204");
                }

                Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
                //Log.d("serverResponse", EntityUtils.toString(resp.getEntity()));

                String json = EntityUtils.toString(resp.getEntity());

                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("JSON", json).apply();

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

                    eventList.add(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("serverPost", "onPostExecute");
            Toast.makeText(MainActivity.this, "events are pulled.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}