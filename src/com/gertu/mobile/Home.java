package com.gertu.mobile;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gertu.mobile.models.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@TargetApi(19)
public class Home extends ListActivity {

    private static User user;
    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static ArrayList<Models.Deal> arrayDeals = new ArrayList<Models.Deal>();
    public static TextView textViewName;

    // JSON Node names

    private static final String TAG_DEALS = "_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_SHOP  = "nameShop";
    private static final String TAG_DEAL  = "deal";       
    private static final String TAG_IMAGE = "image";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_COMMENT  = "comment";
    private static final String TAG_EDIT = "Editar";
    private static final String TAG_VIEW = "Ver Perfil";
    private static final String TAG_LOGOUT  = "Salir";


    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dealList;
    private ProgressDialog pDialog;
    // contacts JSONArray
    private Location gertuLocation = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LocationManager glocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener glocListener = new GertuLocationListener();
        glocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, glocListener);
        gertuLocation = glocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        dealList = new ArrayList<HashMap<String, String>>();

        textViewName = (TextView) findViewById(R.id.textViewWel);

        ListView lv = getListView();
        // Listview on item click listener
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String price = ((TextView) view.findViewById(R.id.price))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                    SingleDealActivity.class);
                in.putExtra("Deal", arrayDeals.get(position));

                startActivity(in);
            }
        });

        // Calling async task to get json
        new GetDeals().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(Home.getUser().getToken() == null){
            getMenuInflater().inflate(R.menu.home, menu);
        } else {
            getMenuInflater().inflate(R.menu.edit, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if(Home.getUser().getToken() == null){
            getMenuInflater().inflate(R.menu.home, menu);
        } else {
            getMenuInflater().inflate(R.menu.edit, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(getUser().getToken() != null){
            if(item.getTitle().equals("Editar")){
                //Intent i = new Intent (getApplicationContext(), EditUser.class);
                //startActivity(i);
                return true;
            } else if(item.getTitle().equals("Ver Perfil")) {
                Intent i = new Intent (getApplicationContext(), Profile.class);
                startActivity(i);
                return true;
            } else if(item.getTitle().equals("Salir")) {
                getUser().setImage("");
                getUser().setToken(null);
                getUser().setFirstName("");
                getUser().setEmail("");
                getUser().set_id("");
                getUser().setLastName("");
                TextView lblWelc = (TextView) findViewById(R.id.textViewWel);
                lblWelc.setText("Bienvenido, pulsa en el menu para entrar");
                Toast.makeText(getApplicationContext(), "Sesion cerrada",
                        Toast.LENGTH_LONG).show();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
    	}else{
    		switch (item.getItemId()) {
            case R.id.action_login:
                Intent i = new Intent (getApplicationContext(), Login.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
    	}
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDeals extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Home.this);
            pDialog.setMessage("Cargando las mejores ofertas para ti...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            String jsonStr;
            Boolean location = true;
            if (gertuLocation != null) {
                List<NameValuePair> locationList = new ArrayList<NameValuePair>(2);
                locationList.add(new BasicNameValuePair("userLat", String.valueOf(gertuLocation.getLatitude())));
                locationList.add(new BasicNameValuePair("userLong", String.valueOf(gertuLocation.getLongitude())));
                String newUrl = "http://www.gertu.info/api/v1/webData";
                ServiceHandler sh = new ServiceHandler();
                jsonStr = sh.makeServiceCall(newUrl, ServiceHandler.GET, locationList);
            } else {
                location = false;
                ServiceHandler sh = new ServiceHandler();
                String url = "http://www.gertu.info/mobile/v1/deals";
                jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            }
            if (!jsonStr.equals("")) {
                if (location){
                    try {
                        JSONArray result = new JSONArray(jsonStr);
                        JSONObject jsonObj = result.getJSONObject(0);
                        JSONArray allDeals = jsonObj.getJSONArray("neardeals");
                        if (allDeals.length() == 0) {
                            location = false;
                        }
                        // looping through All Deals
                        for (int x = 0; x < allDeals.length(); x++) {
                            JSONObject dealObject = allDeals.getJSONObject(x);
                            JSONObject deal = dealObject.getJSONObject("deal");
                            String name = deal.getString(TAG_NAME);
                            String price = deal.getString("gertuprice");
                            String shop = deal.getString("shop");

                            Models.Deal actualDeal = new Models().new Deal();

                            actualDeal.name = name;
                            actualDeal.gertuprice = price;
                            actualDeal.shop = shop;
                            actualDeal.price = price;
                            actualDeal.description = deal.getString("description");
                            actualDeal._id = deal.getString("_id");
                            actualDeal.image = deal.getString("image");

                            // tmp hashmap for single contact
                            HashMap<String, String> dealH = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            dealH.put(TAG_NAME, name);
                            dealH.put(TAG_PRICE, price + " €");

                            // adding deal to deals list
                            dealList.add(dealH);
                            arrayDeals.add(actualDeal);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (!location){
                    try {
                        JSONArray allDeals = new JSONArray(jsonStr);

                        // looping through All Deals
                        for (int x = 0; x < allDeals.length(); x++) {
                            JSONObject dealObject = allDeals.getJSONObject(x);
                            JSONObject deal = dealObject.getJSONObject("shop");
                            String shop = deal.getString("_id");

                            String name = dealObject.getString("name");
                            String price = dealObject.getString("gertuprice");

                            Models.Deal actualDeal = new Models().new Deal();

                            actualDeal.name = name;
                            actualDeal.gertuprice = price;
                            actualDeal.shop = shop;
                            actualDeal.price = price;
                            actualDeal.description = dealObject.getString("description");
                            actualDeal._id = dealObject.getString("_id");
                            actualDeal.image = dealObject.getString("image");

                            // tmp hashmap for single contact
                            HashMap<String, String> dealH = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            dealH.put(TAG_NAME, name);
                            dealH.put(TAG_PRICE, price + " €");

                            // adding deal to deals list
                            dealList.add(dealH);
                            arrayDeals.add(actualDeal);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Home.this, dealList,
                    R.layout.list_item, new String[]{TAG_NAME, TAG_PRICE
                    }, new int[]{R.id.name,
                    R.id.price});

            setListAdapter(adapter);
        }

    }

    public class GertuLocationListener implements LocationListener {

        @Override

        public void onLocationChanged(Location loc)

        {
        }


        @Override

        public void onProviderDisabled(String provider)

        {
        }


        @Override

        public void onProviderEnabled(String provider)

        {
        }


        @Override

        public void onStatusChanged(String provider, int status, Bundle extras)

        {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(getUser().getFirstName() != null){
            textViewName.setText("Bienvenido " + getUser().getFirstName());
        }
    }
}