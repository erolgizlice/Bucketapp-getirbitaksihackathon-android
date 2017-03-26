package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
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

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    CallbackManager callbackManager;
    JSONObject eventJSONObject;
    String userGender;
    String userEventIDs = "[";
    User u;
    PlaceLocation loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile", "user_friends", "user_birthday", "user_events", "user_location", "user_hometown");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/events?limit=5000",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                eventJSONObject = response.getJSONObject();

                                try {
                                    JSONArray eventIDs = eventJSONObject.getJSONArray("data");
                                    for (int i = 0; i < eventIDs.length(); i++) {
                                        JSONObject mainObject = new JSONObject(eventIDs.get(i).toString());
                                        String eventID = mainObject.getString("id");

                                        userEventIDs += eventID;
                                        if (i < eventIDs.length() - 1)
                                            userEventIDs += ",";
                                    }
                                    userEventIDs += "]";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                AsyncTaskAddEvents asyncT = new AsyncTaskAddEvents();
                                asyncT.execute();
                            }
                        }
                ).executeAsync();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Log.i("Response", response.toString());

                                    String email = response.getJSONObject().getString("email");
                                    String firstName = response.getJSONObject().getString("first_name");
                                    String lastName = response.getJSONObject().getString("last_name");
                                    String gender = response.getJSONObject().getString("gender");
                                    String bday = response.getJSONObject().getString("birthday");
                                    String age = getAge(Integer.parseInt(bday.split("/")[0]), Integer.parseInt(bday.split("/")[1]), Integer.parseInt(bday.split("/")[2]));
                                    String locationID = response.getJSONObject().getJSONObject("location").getString("id");
                                    PlaceLocation placeLocation = new PlaceLocation("","","","");

                                    new GraphRequest(
                                            AccessToken.getCurrentAccessToken(),
                                            "/" + locationID + "?fields=location",
                                            null,
                                            HttpMethod.GET,
                                            new GraphRequest.Callback() {
                                                public void onCompleted(GraphResponse response) {
                                                    try {
                                                        Log.d("LOCATION", response.getJSONObject().toString());
                                                        JSONObject locationObject = response.getJSONObject().getJSONObject("location");
                                                        String city = locationObject.getString("city");
                                                        String country = locationObject.getString("country");
                                                        String lat = locationObject.getString("latitude");
                                                        String longt = locationObject.getString("longitude");

                                                        loc = new PlaceLocation(city, country, lat, longt);
                                                        u.setPlaceLocation(loc);

                                                        AsyncTaskAddUser asyncT = new AsyncTaskAddUser();
                                                        asyncT.execute();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                    ).executeAsync();

                                    Profile profile = Profile.getCurrentProfile();
                                    String id = profile.getId();
                                    String name = profile.getName();
                                    String link = profile.getLinkUri().toString();
                                    String profilePicLink = profile.getProfilePictureUri(200, 200).toString();
                                    Log.i("Link", link);
                                    if (Profile.getCurrentProfile() != null) {
                                        Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                    }

                                    Log.i("Login" + "Email", email);
                                    Log.i("Login" + "FirstName", firstName);
                                    Log.i("Login" + "LastName", lastName);
                                    Log.i("Login" + "Gender", gender);
                                    Log.i("Login" + "Bday", bday);
                                    Log.i("Login" + "Age", age);
                                    Log.i("Login" + "LocationID", locationID);

                                    u = new User(id, name, link, gender, age, profilePicLink, email, locationID, placeLocation);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name,gender, birthday, location, hometown");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("facebookLogin", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebookLogin", "error");
            }
        });
    }

    private String getAge(int month, int day, int year) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    class AsyncTaskAddEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient hc = new DefaultHttpClient();
            String message;

            HttpPost p = new HttpPost("https://bucketapp-getirbitaksi.herokuapp.com/addEvent");
            JSONObject object = new JSONObject();

            try {
                String id = Profile.getCurrentProfile().getId();

                message = "{ \"facebook_user_id\":\"" + id + "\",\"events\":" + eventJSONObject.get("data").toString() + " }";

                Log.d("EVEEEEENT",eventJSONObject.get("data").toString());
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
            Log.d("serverPostAddEvents", "onPostExecute");
        }
    }

    class AsyncTaskAddUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient hc = new DefaultHttpClient();
            String message;

            HttpPost p = new HttpPost("https://bucketapp-getirbitaksi.herokuapp.com/addUser");

            try {
                message = u.toString() + " \"events\": " + userEventIDs + "}";

                Log.d("USER", message);
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
            Log.d("serverPostAddUser", "onPostExecute");
        }
    }
}
