package com.gertu.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleDealActivity  extends Activity {

    // JSON node keys
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_SHOP = "shop";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_deal);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String price = in.getStringExtra(TAG_PRICE);
        String shop = in.getStringExtra(TAG_SHOP);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblShop = (TextView) findViewById(R.id.shop_label);

        lblName.setText(name);
        lblPrice.setText(price);
        lblShop.setText(shop);
    }
}
