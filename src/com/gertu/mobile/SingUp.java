package com.gertu.mobile;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SingUp extends Activity implements OnClickListener{
   
	private ProgressDialog ventanaEspera;

	private EditText editEmail;
	private EditText edtFName;
	private EditText edtLName;
	private EditText edtPassword;
	private EditText edtRptPassword;
	private Button btnRegister;

	//private AsyncTask<Void, Void, Void> execute;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sing_up);
		
		this.editEmail=(EditText)this.findViewById(R.id.editEmail);
		this.edtFName=(EditText)this.findViewById(R.id.edtFName);
		this.edtLName=(EditText)this.findViewById(R.id.edtLName);
		this.edtPassword=(EditText)this.findViewById(R.id.edtPassword);
		this.edtRptPassword=(EditText)this.findViewById(R.id.edtRptPassword);
		this.btnRegister=(Button)this.findViewById(R.id.btnRegister);
		
		this.btnRegister.setOnClickListener(this);
		
	}
 

	 
	public void onClick(View view){
		
		
		if(view.getId()==btnRegister.getId())
		{
			if (isEmailValid(editEmail.getText().toString())){
				if(!edtPassword.getText().toString().isEmpty() && !edtRptPassword.getText().toString().isEmpty()){				
					if(edtPassword.getText().toString().equals(edtRptPassword.getText().toString()) && edtPassword.getText().length()>=6){
						Toast toast = Toast.makeText(getApplicationContext(), "Te puedes registrar" , Toast.LENGTH_LONG); 
						toast.show();
						new SetSingUp().execute();
					}
					else{
						edtPassword.setText("");
						edtRptPassword.setText("");
						Toast toast = Toast.makeText(getApplicationContext(), "La contraseña no es igual o es mayor a 6 caracteres" , Toast.LENGTH_LONG); 
						toast.show();
					}
				}
			}else{
				editEmail.setText("");
				Toast toast = Toast.makeText(getApplicationContext(), "No es un email" , Toast.LENGTH_LONG); 
				toast.show();
			}
			
		}
		
		
		
	}
	
	private class SetSingUp extends AsyncTask<Void, Void, Void> {
		
		protected void onPreExecute(){
			if( ventanaEspera == null || !ventanaEspera.isShowing() )
			{
				ventanaEspera = new ProgressDialog(SingUp.this);
				ventanaEspera.setMessage(getString(R.string.REGISTRO));
				ventanaEspera.show();
			}	
		}
		
		protected Void doInBackground(Void... params){
			
		
			
			String email_singup = editEmail.getText().toString();
			String fName_singup = edtFName.getText().toString();
			String lName_singup = edtLName.getText().toString();
			String pass_singup = edtPassword.getText().toString();
			
			JSONObject singupJson = new JSONObject();
			
			
			ServiceHandlerJson service = new ServiceHandlerJson();	
			
			
			
			try{
				
				singupJson.put("firstName", fName_singup);
				singupJson.put("lastName", lName_singup);
				singupJson.put("email", email_singup);
				singupJson.put("password", pass_singup);
				
				StringEntity jsonParams = new StringEntity(singupJson.toString());
				String result = service.makeServiceCall("http://10.0.2.2:3000/mobile/v1/users", 2, jsonParams);
				System.out.println(result);
                Intent i = new Intent (getApplicationContext(), RECLOGIN.class);            	
                startActivity(i);
				ventanaEspera.dismiss();
				if(result.isEmpty()){
					Toast toast = Toast.makeText(getApplicationContext(), "No funciona" , Toast.LENGTH_LONG); 
					toast.show();
				}
			}catch(NullPointerException e){
				System.out.println(e.toString());
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.toString());
			} catch(JSONException e){
				System.out.println(e.toString());
			}
				
			
		return null;	
		}
		
		protected void onPostExecute(Void result){
			
			if(ventanaEspera != null && ventanaEspera.isShowing())
			{
				ventanaEspera.dismiss();
			}
		}
	
		
		
	}
	
	

	private boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
}
