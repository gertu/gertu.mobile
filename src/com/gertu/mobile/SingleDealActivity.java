package com.gertu.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class SingleDealActivity extends Activity {
	

	final Context context = this;
	
	// JSON node keys
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_SHOP = "shop";
    private static final String TAG_DESCRIPTION  = "description";
    private static final String TAG_COMMENT  = "comment";
    private static final String TAG_DEAL  = "deal";    

	private static final Object TAG_IMAGE = "image";
	private static final Object TAG_ID = "_id";
	
	HashMap<String,String>  deal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_deal);
		
		 // getting intent data
        Intent in = getIntent();
        
        deal = (HashMap<String, String>)in.getSerializableExtra(TAG_DEAL);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblShop = (TextView) findViewById(R.id.shop_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblDescription = (TextView) findViewById(R.id.description_label);
        TextView lblComment = (TextView) findViewById(R.id.comment_label);
        ImageView imgDeal = (ImageView) findViewById(R.id.imgDeal);

        
        lblName.setText(deal.get(TAG_NAME));
        lblShop.setText(deal.get(TAG_SHOP));
        lblPrice.setText(deal.get(TAG_PRICE));
        lblDescription.setText(deal.get(TAG_DESCRIPTION));
        lblComment.setText(deal.get(TAG_COMMENT));
        System.out.println(deal.get(TAG_IMAGE));
        try {
        	  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(deal.get(TAG_IMAGE)).getContent());
        	  imgDeal.setImageBitmap(bitmap); 
        	} catch (MalformedURLException e) {
        	  e.printStackTrace();
        	} catch (IOException e) {
        	  e.printStackTrace();
        	}

		
		Button boton = (Button) findViewById (R.id.btnReservation);
        boton.setOnClickListener(new Button.OnClickListener()
        
        {
            public void onClick(View v)
            {
            	
            	AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("¿Confirma la Reserva?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        new loginAsyncTask().execute();
                    	Intent i = new Intent (getApplicationContext(), RECLOGIN.class);
                    	ServiceHandlerJson shj = new ServiceHandlerJson();
                    	String jsonStr;
                    	jsonStr = shj.makeServiceCall("http://10.0.2.2:3000/mobile/v1/deals/"+deal.get(TAG_ID)+"/reserve",ServiceHandlerJson.POST, null);
                    	startActivity(i);
                    }
                });
                builder1.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            	
            }
        });    
		
		
	}
	
	
	private class loginAsyncTask extends AsyncTask<Void, Void, Void>    {		 
        
		
		private ProgressDialog pDialog;
        
		@Override
        protected void onPreExecute() {
			// Showing progress dialog
            pDialog = new ProgressDialog(SingleDealActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();		 	       	
                      
        	
        }
 
        @Override
        protected Void doInBackground(Void... params1) {
        	ServiceHandler sh = new ServiceHandler();
        	List<NameValuePair> params = new LinkedList<NameValuePair>();                    
			System.out.println("before of Reservation");
        	String result = sh.makeServiceCall("http://10.0.2.2:3000/deals/:id/reservation", ServiceHandler.GET, params);
            System.out.println(result);
			return null;          
			            
        } 
        
        @Override
        protected void onPostExecute(Void result) {
        	if (pDialog.isShowing())
                pDialog.dismiss();       	       	
        	        	
        }
    }
	
}