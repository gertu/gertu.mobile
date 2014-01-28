package com.gertu.mobile;

import com.gertu.mobile.R;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class RECLOGIN extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reclogin);
		
		String data = "";
		String data2 = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            data = bundle.getString("clave");
            data2 = bundle.getString("pass");
        }

        TextView tV = (TextView) findViewById(R.id.TextRecLogin);
        tV.setText(data);
        TextView tV2 = (TextView) findViewById(R.id.TextRecPassword);
        tV2.setText(data2);
	}

	

}



