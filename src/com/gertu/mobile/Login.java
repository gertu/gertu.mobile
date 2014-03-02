package com.gertu.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

@SuppressLint("WrongViewCast")
public class Login extends Activity {
	
	EditText editEmail;
	EditText editPass;
	String email;
	String pass;
	public String nombreUsuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button boton = (Button) findViewById (R.id.btnAccess);
        boton.setOnClickListener(new Button.OnClickListener()
        
        {
            public void onClick(View v)
            {
            	
            	
            	
            	editEmail = (EditText) findViewById(R.id.editEmailLogin);
            	editPass = (EditText) findViewById(R.id.edittPasswordLogin);

            	
            	if (isEmailValid(editEmail.getText().toString())){
    				if(!editPass.getText().toString().isEmpty()){				
    					if(editPass.getText().length()>=6){ 
    						new loginAsyncTask().execute();
                        }
    					else{
    						editPass.setText("");
    						Toast toast = Toast.makeText(getApplicationContext(), "La contraseña no es igual o es mayor a 6 caracteres" , Toast.LENGTH_LONG);
    						toast.show();
    					}
    				}
    			}else{
    				editEmail.setText("");
    			}
            	
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

	}
	private class loginAsyncTask extends AsyncTask<Void, Void, Boolean>    {		 
        
		
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
        protected Boolean doInBackground(Void... params1){
        	Boolean userIdent = null;
        	String jsonStr;
        	ServiceHandlerJson shj = new ServiceHandlerJson();
        	JSONObject jO = new JSONObject();
        	try {
        		jO.put("email", editEmail.getText().toString());
        		jO.put("password", editPass.getText().toString());
        	} catch (JSONException e){
        		e.printStackTrace();
        	}
        	StringEntity se = null;
        	try {
        		se = new StringEntity(jO.toString());
        	} catch (UnsupportedEncodingException e) {
        		e.printStackTrace();
        	}
        	jsonStr = shj.makeServiceCall("http://www.gertu.info/mobile/v1/users/session",ServiceHandlerJson.POST, se);
        	try {
            	
                if(jsonStr=="403"){
             
    				userIdent=false;
                		
                }else
                {
                	System.out.println(jsonStr.toString());
                 	JSONObject user = new JSONObject(jsonStr);

                    if (user.has("firstName")){
                        Home.getUser().setFirstName(user.getString("firstName"));
                    } else {
                        Home.getUser().setFirstName(user.getString("email"));
                    }
                    if (user.has("lastName")){
                        Home.getUser().setLastName(user.getString("lastName"));
                    }
                    if (user.has("_id")){
                        Home.getUser().set_id(user.getString("_id"));
                    }

                    Home.getUser().setEmail(user.getString("email"));
                    Home.getUser().setToken(user.getString("token"));
                    Home.getUser().setImage(user.getString("picture"));

                    if (user.has("firstName")){
                        nombreUsuario = " " + user.getString("firstName");
                    } else {
                        nombreUsuario = " " + user.getString("email");
                    }
                    userIdent=true;
     				
                }
			} catch (JSONException e) {
				
				e.printStackTrace();
			}   	
        	
			return userIdent;      	       
			            
        } 
        
        protected void onPostExecute(Boolean userIdent) {
        	
        	if (pDialog.isShowing())
                pDialog.dismiss();       	       	

        	if(userIdent){
        		/*Intent i = new Intent (getApplicationContext(), Home.class);
                startActivity(i);*/
        	}else{
                Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos",
                        Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }  
	private boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
}
