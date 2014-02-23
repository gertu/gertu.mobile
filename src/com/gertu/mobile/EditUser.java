package com.gertu.mobile;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUser extends Activity implements OnClickListener{
	
	private EditText editFName;
	private EditText editLName;
	private EditText editEmail;
	private Button btnEditUser;

	
	protected void onCreate(Bundle savedInstanceState) {
		StringEntity jsonString = null;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edituser);
		
		this.editEmail=(EditText)this.findViewById(R.id.editEmail);
		this.editFName=(EditText)this.findViewById(R.id.edtFName);
		this.editLName=(EditText)this.findViewById(R.id.edtLName);
		this.btnEditUser=(Button)this.findViewById(R.id.btnEditUser);
		
		this.btnEditUser.setOnClickListener(this);
		
		JSONObject jsonExample = new JSONObject();
		
		try{
		jsonExample.put("firstName", "Aritz");
		jsonExample.put("lastName", "aaaa");
		jsonExample.put("email", "a@g.com");
		
	   jsonString = new StringEntity(jsonExample.toString());
		
	   
	   editFName.setText(jsonExample.getString("firstName"));
	   editLName.setText(jsonExample.getString("lastName"));
	   editEmail.setText(jsonExample.getString("email"));
	   
	   
	   
		}catch(JSONException e){
			System.out.println(e.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}		
			
		
	
		
	}

	public void onClick(View v){
	
		
		if(v.getId()==btnEditUser.getId()){
			new asynEditUser().execute();
			finish();
		}
	}
	
	private class asynEditUser extends AsyncTask <Void, Void , Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			String email_edituser = editEmail.getText().toString();
			String fName_edituser = editFName.getText().toString();
			String lName_edituser = editLName.getText().toString();
			
			JSONObject editUserJSon = new JSONObject();
			
			
			ServiceHandlerEditUser service = new ServiceHandlerEditUser();	
						
			
			try{
				
				String id="52ffd7cc0d8843a017edb2dc";
				String password="123456";
				editUserJSon.accumulate("firstName", fName_edituser);
				editUserJSon.accumulate("lastName", lName_edituser);
				editUserJSon.accumulate("email", email_edituser);
				editUserJSon.accumulate("password", password);
				editUserJSon.accumulate("_id", id);
				 
				
				System.out.println(editUserJSon.toString());
				StringEntity jsonParams = new StringEntity(editUserJSon.toString());
				
				String result = service.makeServiceCall("http://10.0.2.2:3000/mobile/v1/users", 3, jsonParams);
				
				System.out.println(result);
				
			}catch(NullPointerException e){
				System.out.println(e.toString());
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.toString());
			} catch(JSONException e){
				System.out.println(e.toString());
			}

			return null;
		}
		
		
	}

	
		
}
	

	




