package com.team.handycraft.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.handycraft.R;
import com.team.handycraft.model.Order;

import java.util.HashMap;
import java.util.Map;

public class ActivityPlaceOrder extends ActivityBase {

    private DatabaseReference mDatabase;

    Spinner mCategorySpinner;
    Spinner mLocationSpinner;
    EditText mDetailsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Request Order");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDetailsField = (EditText) findViewById(R.id.prob_desc_et);

        // Spinner element
        mCategorySpinner = (Spinner) findViewById(R.id.work_spinner);
        mLocationSpinner = (Spinner) findViewById(R.id.location_spinner);


        // Spinner click listener
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Craft Spinner Drop down elements
        String[] locations = getResources().getStringArray(R.array.locations);

        // Craft Spinner Drop down elements
        String[] categories = getResources().getStringArray(R.array.categories);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mCategorySpinner.setAdapter(dataAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);

        // Drop down layout style - list view with radio button
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mLocationSpinner.setAdapter(locationAdapter);




        FloatingActionButton submitOrderbtn = (FloatingActionButton) findViewById(R.id.fab_submit_order);

        submitOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
                finish();
            }
        });
    }

    private void submitOrder() {
        String key = mDatabase.child("orders").push().getKey();
        String category = mCategorySpinner.getSelectedItem().toString();
        String details = mDetailsField.getText().toString();
        String location = mLocationSpinner.getSelectedItem().toString();

        final String userId = getUid();

        Order order = new Order(userId, category, location, details);

        Map<String, Object> postValues = order.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/orders/" + key, postValues);
        childUpdates.put("/user-orders/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

    }
}

