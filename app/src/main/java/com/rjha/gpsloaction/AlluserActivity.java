package com.rjha.gpsloaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rjha.gpsloaction.Adapter.RecyclerviewAdapter;
import com.rjha.gpsloaction.Model.AllUsers;
import com.rjha.gpsloaction.Model.Listdata;

import java.util.ArrayList;
import java.util.List;

public class AlluserActivity extends AppCompatActivity {
    TextView ename,eemail,eaddress;
    Button save,view;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<Listdata> list;
    RecyclerView recyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alluser);
        recyclerview = (RecyclerView) findViewById(R.id.rview);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Hospitals");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    AllUsers userdetails = dataSnapshot1.getValue(AllUsers.class);
                    Listdata listdata = new Listdata();
                    String name=userdetails.getName();
                    String email=userdetails.getEmail();
                    String address=userdetails.getAddress();
                    listdata.setName(name);
                    listdata.setEmail(email);
                    listdata.setAddress(address);
                    list.add(listdata);
                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                RecyclerviewAdapter recycler = new RecyclerviewAdapter(list);
                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(AlluserActivity.this);
                recyclerview.setLayoutManager(layoutmanager);
                recyclerview.setItemAnimator( new DefaultItemAnimator());
                recyclerview.setAdapter(recycler);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        }

}
