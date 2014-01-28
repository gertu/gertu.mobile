package com.gertu.mobile;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.gertu.mobile.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("WrongViewCast")
public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button boton = (Button) findViewById (R.id.btnAccess);
        boton.setOnClickListener(new Button.OnClickListener()
        
        {
            public void onClick(View v)
            {
            	EditText ed = (EditText) findViewById(R.id.edtUser);
            	String texto = ed.getText().toString();
            	EditText ed2 = (EditText) findViewById(R.id.edtPassword);
            	String texto2 = ed2.getText().toString();
            	ServiceHandler sh = new ServiceHandler();
            	List<NameValuePair> params = new LinkedList<NameValuePair>();                    
                params.add(new BasicNameValuePair("email", texto));
                params.add(new BasicNameValuePair("password", texto2));
                System.out.println("before of call");
            	String result = sh.makeServiceCall("http://10.0.2.2:3000/api/v1/users/session", ServiceHandler.POST, params);
                System.out.println(result);
            	Intent i = new Intent (getApplicationContext(), RECLOGIN.class);
            	i.putExtra("clave", texto);
            	i.putExtra("pass", texto2);
                startActivity(i);
            }
        });
        
        Button botonN = (Button) findViewById (R.id.btnNewUser);
        botonN.setOnClickListener(new Button.OnClickListener()
        
        {
            public void onClick(View v)
            {
            	
            	
            	Intent i = new Intent (getApplicationContext(), RECLOGIN.class);
            	
                startActivity(i);
            }
        });
	}
	
}
