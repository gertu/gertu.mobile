package com.gertu.mobile;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdjustUser extends Activity implements OnClickListener{
		
private Button btnEditUser;
private Button btnEditPass;
		
protected void onCreate(Bundle savedInstanceState) {
			
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_edituser);	
			
	this.btnEditUser=(Button)this.findViewById(R.id.btnEditUser);
	this.btnEditPass=(Button)this.findViewById(R.id.btnEditPass);
	
	this.btnEditUser.setOnClickListener(this);
	this.btnEditPass.setOnClickListener(this);
	}

	
	
	

	
public void onClick(View v) {
		if(btnEditUser.getId()==v.getId()){
			Intent iUser = new Intent (getApplicationContext(), EditUser.class);
			startActivity(iUser);
		}
		/*if(btnEditPass.getId()==v.getId()){
			Intent iPass = new Intent (getApplicationContext(), .class);
			startActivity(iPass);
		}*/
		
	}	
	
	
}
