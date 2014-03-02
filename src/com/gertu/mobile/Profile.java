package com.gertu.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Profile  extends Activity {

    public ProgressDialog uDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // getting intent data
        Intent in = getIntent();

        TextView lblNameProfile = (TextView) findViewById(R.id.textViewNameProfile);
        TextView lblEmailProfile = (TextView) findViewById(R.id.textViewEmailProfile);

        lblNameProfile.setText(Home.getUser().getFirstName());
        lblEmailProfile.setText(Home.getUser().getEmail());
        new GetData().execute();

        final Button button = (Button) findViewById(R.id.buttonRes);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext(), Reservation.class);
                startActivity(i);
            }
        });
    }

    private class GetData extends AsyncTask<Void, Void, Void> implements Runnable{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            uDialog = new ProgressDialog(Profile.this);
            uDialog.setMessage("Cargando tus datos...");
            uDialog.setCancelable(false);
            uDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instances
            run();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (uDialog.isShowing())
                uDialog.dismiss();
        }

        public void run() {

            URL url = null;
            try {
                url = new URL("http://www.gertu.info" + Home.getUser().getImage());
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
                    ImageView lblImage = (ImageView) findViewById(R.id.imageViewPicProfile);
                    lblImage.setImageBitmap(finalImage);
                }
            });
        }
    }
}
