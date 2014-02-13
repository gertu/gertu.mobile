package com.gertu.mobile;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.gertu.mobile.models.User;

import java.util.ArrayList;
import java.util.HashMap;


@TargetApi(19)
public class Home extends ListActivity {

    private ProgressDialog pDialog;

    // public static User user = new User();
    
    // URL to get contacts JSON
    private static String url = "http://www.google.es";

    // JSON Node names
    private static final String TAG_DEALS = "neardeals";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_SHOP  = "shop";
    private static final String TAG_DEAL  = "deal";       
    private static final String TAG_IMAGE = "image";
  
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dealList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dealList = new ArrayList<HashMap<String, String>>();

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
                String shop = ((TextView) view.findViewById(R.id.shop))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SingleDealActivity.class);
                in.putExtra(TAG_DEAL, dealList.get(position));
                startActivity(in);

            }
        });

        // Calling async task to get json
        new GetDeals().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                Intent i = new Intent (getApplicationContext(), Login.class);            	
                startActivity(i);
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetDeals extends AsyncTask<Void, Void, Void> {



		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Home.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            
            Log.d("Response: ", "> " + jsonStr);

            /* DEMO DATA -> With server running delete the next line of code */
            jsonStr = "[{'dealcount':5,'usercount':3,'shopcount':3,'neardeals':[{'dist':0.21955096697481835,'deal':{'shop':'52cfc5233385d8c419000001','_id':'52c8219542be50d01a000002','__v':0,'content':'','image':'http://www.vigolowcost.com/wp-content/uploads/2012/06/tintahp.jpg','name':'2x1encartuchosparaimpresoraHP','price':25.5,'created':'2014-01-04T14:58:29.559Z'}},{'dist':0.21955096697481835,'deal':{'shop':'52cfc5233385d8c419000001','_id':'52c822e442be50d01a000003','__v':0,'content':'','image':'http://www.empetel.es/WebRoot/StoreLES/Shops/17205045/4FDC/6C1F/A9EC/2938/7C7D/C0A8/2981/8728/funda_galaxy_s3.jpg','name':'Fundaprotectoraparam�vilSamsungGalaxySIII','price':12.75,'created':'2014-01-04T15:04:04.078Z'}},{'dist':0.722701900894575,'deal':{'shop':'52d024f7ec5c05681f000001','_id':'52c8231642be50d01a000004','__v':0,'content':'','image':'http://d243u7pon29hni.cloudfront.net/images/Pilas_PlusPower_AAA_8_4_l.jpg','name':'8+4pilasAADuracell','price':8.95,'created':'2014-01-04T15:04:54.980Z'}},{'dist':0.722701900894575,'deal':{'shop':'52d024f7ec5c05681f000001','_id':'52d40b48558be5e81d000001','__v':0,'content':'','image':'http://static4.tudespensa.com/rep/8c79/imagenes/38719/4/lasana-bolonesa-con-bechamel-maheso--300-gr.jpg','name':'Lasa�aMahesoa1�laud.','price':1,'created':'2014-01-13T15:50:32.182Z'}},{'dist':0.3998569491326972,'deal':{'shop':'52d4032e6a32063c18000001','_id':'52c8215542be50d01a000001','__v':0,'content':'','image':'http://mosaicocine.files.wordpress.com/2011/11/cinesa_logo_grande.jpg','name':'50%dedescuentoenCinesaZubiarte','price':8,'created':'2014-01-04T14:57:25.468Z'}}]}]";
            if (jsonStr != null) {
                try {

                    JSONArray result = new JSONArray(jsonStr);
                    JSONObject jsonObj = result.getJSONObject(0);
                    JSONArray allDeals = jsonObj.getJSONArray(TAG_DEALS);

                    // looping through All Deals
                    for (int x = 0; x < allDeals.length(); x++)
                    {
                        JSONObject dealObject = allDeals.getJSONObject(x);
                        JSONObject deal = dealObject.getJSONObject("deal");
                        String name = deal.getString(TAG_NAME);
                        String price = deal.getString(TAG_PRICE);
                        String shop = deal.getString(TAG_SHOP);
                        String image = deal.getString(TAG_IMAGE);
                      
                        // tmp hashmap for single contact
                        HashMap<String, String> dealH = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        dealH.put(TAG_NAME, name);
                        dealH.put(TAG_PRICE, price);
                        dealH.put(TAG_SHOP, shop);
                        dealH.put(TAG_IMAGE, image);
                      
                        // adding deal to deals list
                        dealList.add(dealH);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
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
                    R.layout.list_item, new String[] { TAG_NAME, TAG_PRICE,
                    TAG_SHOP }, new int[] { R.id.name,
                    R.id.price, R.id.shop });

            setListAdapter(adapter);
        }

    }

}



