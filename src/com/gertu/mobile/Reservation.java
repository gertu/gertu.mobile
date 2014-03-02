package com.gertu.mobile;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class Reservation extends ListActivity {

    ArrayList<HashMap<String, String>> reservationList;
    public ProgressDialog rDialog;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservations);

        // getting intent data
        Intent in = getIntent();

        reservationList = new ArrayList<HashMap<String, String>>();
        new GetData().execute();

    }

    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            rDialog = new ProgressDialog(Reservation.this);
            rDialog.setMessage("Cargando tus reservas...");
            rDialog.setCancelable(false);
            rDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            String jsonStr;
            String newUrl = "http://www.gertu.info/mobile/v1/users/reservations";
            JSONObject jO = new JSONObject();
            try {
                jO.put("token", Home.getUser().getToken());
            } catch (JSONException e){
                e.printStackTrace();
            }
            StringEntity se = null;
            try {
                se = new StringEntity(jO.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ServiceHandlerJson sh = new ServiceHandlerJson();
            jsonStr = sh.makeServiceCall(newUrl, ServiceHandler.POST, se);
            if (!jsonStr.equals("")) {
                try {

                    JSONArray allDeals = new JSONArray(jsonStr);

                    // looping through All Deals
                    for (int x = 0; x < allDeals.length(); x++) {
                        JSONObject dealObject = allDeals.getJSONObject(x);
                        String name = dealObject.getString("deal");

                        HashMap<String, String> reservH = new HashMap<String, String>();

                        String jsonStrRes;
                        String shopURL = "http://www.gertu.info/mobile/v1/deals/" + name;
                        ServiceHandler shRes = new ServiceHandler();
                        jsonStrRes = shRes.makeServiceCall(shopURL, ServiceHandler.GET);


                        JSONObject deal = new JSONObject(jsonStrRes);
                        String nameOfTheDeal = deal.getString("name");
                        String codeOfTheDeal = dealObject.getString("_id");

                        // adding each child node to HashMap key => value
                        reservH.put("textViewReservation", "Oferta: "+nameOfTheDeal);
                        reservH.put("textViewReservationId", "Codigo de reserva: "+codeOfTheDeal);


                        // adding deal to deals list
                        reservationList.add(reservH);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
            if (rDialog.isShowing())
                rDialog.dismiss();

            ListAdapter adapterR = new SimpleAdapter(
                    Reservation.this, reservationList,
                    R.layout.list_reservation, new String[]{"textViewReservation", "textViewReservationId"
            }, new int[]{R.id.textViewReservation, R.id.textViewReservationId});

            setListAdapter(adapterR);
        }
    }

}
