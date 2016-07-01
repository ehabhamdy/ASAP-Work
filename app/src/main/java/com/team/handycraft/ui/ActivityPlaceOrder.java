package com.team.handycraft.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.team.handycraft.R;

/**
 * Created by Ehab on 6/30/16.
 */
public class ActivityPlaceOrder extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_login);

        Toast.makeText(ActivityPlaceOrder.this, "adfkdsklfdklsa", Toast.LENGTH_SHORT).show();

        FloatingActionButton submitOrderbtn = (FloatingActionButton) findViewById(R.id.fab_submit_order);

        submitOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });
    }

    private void submitOrder() {
    }
}
