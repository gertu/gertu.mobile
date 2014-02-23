package com.gertu.mobile;

import java.io.UnsupportedEncodingException;
import com.gertu.mobile.models.User;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("WrongViewCast")
public class Login extends Activity {
	
	EditText ed;
	String texto;
	EditText ed2;
	String texto2;
	//int idUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button boton = (Button) findViewById (R.id.btnAccess);
        boton.setOnClickListener(new Button.OnClickListener()
        
        {
            public void onClick(View v)
            {
            	
            	
            	
            	ed = (EditText) findViewById(R.id.edtUser);
            	texto = ed.getText().toString();
            	ed2 = (EditText) findViewById(R.id.edtPassword);
            	texto2 = ed2.getText().toString();
            	new loginAsyncTask().execute();
            	Intent i = new Intent (getApplicationContext(), Home.class);
//            	i.putExtra("clave", texto);
//            	i.putExtra("pass", texto2);
            	//i.putExtra("_id", idUser);
                startActivity(i);
            }
        });
        
        Button botonN = (Button) findViewById (R.id.btnNewUser);
        botonN.setOnClickListener(new Button.OnClickListener()
        
        {
            public void onClick(View v)
            {
            	
            	
            	Intent i = new Intent (getApplicationContext(), SingUp.class);
            	
                startActivity(i);
            }
        });
     /*   
        Button btnAdjustUser= (Button) findViewById(R.id.btnEditAdjust);
        btnAdjustUser.setOnClickListener(new Button.OnClickListener()
        {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), EditUser.class);
            
                startActivity(i);
				
			}
        	
        });
       */
	}
	private class loginAsyncTask extends AsyncTask<Void, Void, Void>    {		 
        
		
		private ProgressDialog pDialog;
        
		@Override
        protected void onPreExecute() {
			// Showing progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();		 	       	
                      
        	
        }
 
        @Override
        protected Void doInBackground(Void... params1) {
        	String jsonStr;
        	ServiceHandlerJson shj = new ServiceHandlerJson();
        	JSONObject jO = new JSONObject();
        	try {
        		jO.put("email", texto);
        		jO.put("password", texto2);
        	} catch (JSONException e){
        		e.printStackTrace();
        	}
        	StringEntity se = null;
        	try {
        		se = new StringEntity(jO.toString());
        	} catch (UnsupportedEncodingException e) {
        		e.printStackTrace();
        	}
        	System.out.println("before of call Json");
        	jsonStr = shj.makeServiceCall("http://10.0.2.2:3000/mobile/v1/users/session",ServiceHandlerJson.POST, se);
            try {
            	
                //Gson gson = new Gson();

                //convert java object to JSON format
               
            	//String json = gson.toJson(jsonStr);

                //System.out.println(json);
                //JSONObject user = new JSONObject(json);
                System.out.println(jsonStr);
            	JSONObject user = new JSONObject(jsonStr);
            	
				Home.user.setFirstName(user.getString("firstName"));
				//System.out.println(Home.user.getFirstName());
        		Home.user.setLastName(user.getString("lastName"));
        		Home.user.set_id(user.getString("_id"));
        		System.out.println(Home.user.get_id());
        		Home.user.setEmail(user.getString("email"));
				String firstName = user.getString("firstName");
				//idUser = user.getInt("_id");
				System.out.println(firstName);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//System.out.println(jsonStr);
			return null;      	       
			            
        } 
        
        @Override
        protected void onPostExecute(Void result) {
        	if (pDialog.isShowing())
                pDialog.dismiss();       	       	
        	        	
        }
    }   
}
