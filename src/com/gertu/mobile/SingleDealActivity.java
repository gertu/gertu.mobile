package com.gertu.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class SingleDealActivity extends Activity {
    public ProgressDialog pDialogC;
    public ProgressDialog sDialog;
    public static String id;
    public static String idShop;
    public static String nameShop;
    public static String imageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_deal);

        // getting intent data
        Intent in = getIntent();
        // Get JSON values from previous intent
        Models.Deal actualDeal = (Models.Deal)in.getSerializableExtra("Deal");
        id = actualDeal._id;
        idShop = actualDeal.shop;
        String name = "Oferta: "+actualDeal.name;
        String price = "GertuPrice: "+actualDeal.price+"€";
        imageUrl = actualDeal.image;

        nameShop = new String();
        new ShopName().execute();

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);

        lblName.setText(name);
        lblPrice.setText(price);

        final Button button = (Button) findViewById(R.id.buttonComment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new comment().execute();
            }
        });

        final Button buttonRes = (Button) findViewById(R.id.buttonReserv);
        buttonRes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new reserv().execute();
            }
        });
    }

    private class comment extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            if (Home.getUser().getToken() == null){
                return null;
            }
            String urlComent = "http://10.0.2.2:3000/mobile/v1/deals/" + id + "/comment";
            ServiceHandlerJson shj = new ServiceHandlerJson();
            RatingBar rat = (RatingBar)findViewById(R.id.ratingBar);
            String rating = String.valueOf(rat.getRating()*2);
            EditText comm = (EditText) findViewById(R.id.editTextComment);
            String comentario = comm.getText().toString();
            JSONObject jO = new JSONObject();
            try {
                jO.put("token", Home.getUser().getToken());
                jO.put("comment", comentario);
                jO.put("rating", rating);
            } catch (JSONException e){
                e.printStackTrace();
            }
            StringEntity se = null;
            try {
                se = new StringEntity(jO.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String jsonStr = shj.makeServiceCall(urlComent,ServiceHandlerJson.POST, se);
            Log.d("ComentarioURL", urlComent);
            Log.d("Comentario", jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (Home.getUser().getToken() != null){
                Toast.makeText(getApplicationContext(), "Comentario enviado",
                    Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Tienes que estar loggeado para comentar",
                        Toast.LENGTH_LONG).show();
            }
            finish();
        }

    }

    private class reserv extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (Home.getUser().getToken() == null){
                return null;
            }
            ServiceHandlerJson shj = new ServiceHandlerJson();
            JSONObject jO = new JSONObject();
            String newUrl = "http://10.0.2.2:3000/mobile/v1/deals/" + id + "/reservation";
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
            String jsonStr = shj.makeServiceCall(newUrl,ServiceHandlerJson.POST, se);
            Log.d("Reserva", jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (Home.getUser().getToken() != null){
                Toast.makeText(getApplicationContext(), "Reserva realizada",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Tienes que estar loggeado para reservar",
                    Toast.LENGTH_LONG).show();
            }
            finish();
        }

    }

    private class ShopName extends AsyncTask<Void, Void, Void> implements Runnable{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            sDialog = new ProgressDialog(SingleDealActivity.this);
            sDialog.setMessage("Cargando la oferta...");
            sDialog.setCancelable(false);
            sDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            String jsonStr;
            String newUrl = "http://10.0.2.2:3000/mobile/v1/shopInfo/" + idShop;
            ServiceHandler sh = new ServiceHandler();
            jsonStr = sh.makeServiceCall(newUrl, ServiceHandler.GET);
            nameShop = "Tienda: " + jsonStr;
            run();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (sDialog.isShowing())
                sDialog.dismiss();
        }

        public void run() {

            TextView lblShop = (TextView) findViewById(R.id.shop_label);
            lblShop.setText(nameShop);

            URL url = null;
            try {
                url = new URL("http://10.0.2.2:3000" + imageUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap image = null;
            try {
                assert url != null;
                image = BitmapFactory.decodeStream(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Bitmap finalImage = image;
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ImageView lblImage = (ImageView) findViewById(R.id.imageView);
                    lblImage.setImageBitmap(finalImage);
                }
            });
        }
    }
}
