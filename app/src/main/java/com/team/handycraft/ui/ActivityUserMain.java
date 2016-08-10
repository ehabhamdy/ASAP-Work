package com.team.handycraft.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.handycraft.R;
import com.team.handycraft.model.Order;
import com.team.handycraft.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActivityUserMain extends ActivityBase {

    private static final String TAG = "Message";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mOrdersReference;
    private RecyclerView mOrdersRecycler;
    private OrderAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);



        mDatabase = Utils.getDatabase();


        mOrdersReference = mDatabase.getReference()
                .child("user-orders").child(getUid());


        mOrdersRecycler = (RecyclerView) findViewById(R.id.recycler_orders);
        mOrdersRecycler.setLayoutManager(new LinearLayoutManager(this));



        FloatingActionButton newOrderBtn = (FloatingActionButton) findViewById(R.id.fab_new_order);

        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityUserMain.this, ActivityPlaceOrder.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new OrderAdapter(this, mOrdersReference);
        mOrdersRecycler.setAdapter(mAdapter);

    }

    private static class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryView;
        public  TextView locationView;
        public TextView detailsView;

        public OrderViewHolder(View itemView) {
            super(itemView);

            categoryView = (TextView) itemView.findViewById(R.id.order_category);
            locationView = (TextView) itemView.findViewById(R.id.order_location);
            detailsView = (TextView) itemView.findViewById(R.id.order_details);
        }
    }


    private static class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mOrderIds = new ArrayList<>();
        private List<Order> mOrders = new ArrayList<>();

        public OrderAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new order has been added, add it to the displayed list
                    Order order = dataSnapshot.getValue(Order.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mOrderIds.add(dataSnapshot.getKey());
                    mOrders.add(order);
                    notifyItemInserted(mOrders.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Order newOrder = dataSnapshot.getValue(Order.class);
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int orderIndex = mOrderIds.indexOf(commentKey);
                    if (orderIndex > -1) {
                        // Replace with the new data
                        mOrders.set(orderIndex, newOrder);

                        // Update the RecyclerView
                        notifyItemChanged(orderIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String orderKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int orderIndex = mOrderIds.indexOf(orderKey);
                    if (orderIndex > -1) {
                        // Remove data from the list
                        mOrderIds.remove(orderIndex);
                        mOrders.remove(orderIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(orderIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + orderKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Order movedOrder = dataSnapshot.getValue(Order.class);
                    String orderKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load orders.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(mChildEventListener);
            // [END child_event_listener_recycler]
        }

        @Override
        public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_order2, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderViewHolder holder, int position) {
            Order order = mOrders.get(position);
            holder.categoryView.setText(order.category);
            holder.locationView.setText("In: " + order.location);
            holder.detailsView.setText(order.details);
        }

        @Override
        public int getItemCount() {
            return mOrders.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, ActivityLogin.class));
                return true;
            case R.id.action_user_profile:
                // TODO: 6/21/16 implement user profile activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
