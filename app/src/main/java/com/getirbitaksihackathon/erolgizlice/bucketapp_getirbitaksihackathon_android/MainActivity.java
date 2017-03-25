package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

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
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    String json = "";

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
            //asyncT.execute();

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

        /*lvItems = (ListView) findViewById(R.id.listViewToDoList);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        for (int i = 0; i < 20; i++)
            items.add(i + " Item");*/
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

    class AsyncTaskGetEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient hc = new DefaultHttpClient();
            String message;

            HttpPost p = new HttpPost("http://192.168.88.162:3000/getEvent");
            JSONObject object = new JSONObject();

            try {
                String id = Profile.getCurrentProfile().getId(),
                        firstName = Profile.getCurrentProfile().getFirstName(),
                        lastName = Profile.getCurrentProfile().getLastName();
                Uri linkUri = Profile.getCurrentProfile().getLinkUri(),
                        profilePicUri = Profile.getCurrentProfile().getProfilePictureUri(25, 25);

                //"{ \"facebook_user_id\":\""+id+"\",\"events\":" +
                //message = "{ \"facebook_user_id\":\""+id+"\",\"events\":" +jsonObject.get("data").toString()+" }";
                message = "{ \"key\":\"Z7gOVEuESS\"}";


                p.setEntity(new StringEntity(message, "UTF8"));
                p.setHeader("Content-type", "application/json");
                HttpResponse resp = hc.execute(p);
                if (resp != null) {
                    if (resp.getStatusLine().getStatusCode() == 204)
                        Log.d("serverPost", "204");
                }

                Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
                Log.d("serverResponse", EntityUtils.toString(resp.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("serverPost", "onPostExecute");
        }
    }
}