package com.example.gertu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        Button boton = (Button) findViewById (R.id.btnLogin);
        boton.setOnClickListener(new Button.OnClickListener()
        
        {
            public void onClick(View v)
            {
            	
            	
            	Intent i = new Intent (getApplicationContext(), Login.class);
            	
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
}




