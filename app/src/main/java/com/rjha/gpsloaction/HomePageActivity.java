package com.rjha.gpsloaction;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomePageActivity extends AppCompatActivity implements LocationListener {
TextView tvlocation;
Button b1,b2;
Geocoder geocoder;
List<Address> addresses;
Double latitude=26.9124;
Double lonfitude=75.7873;

LocationManager locationManager;
    FirebaseUser user;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    String email,name,mobile;
    TextView tv1,tv2,tv3;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);
        tvlocation=findViewById(R.id.locationtext);
        b1=findViewById(R.id.bttnsend);
        b2=findViewById(R.id.bttnalluser);
        tv1=findViewById(R.id.editText_emailAddress);
        tv2=findViewById(R.id.editText_name);
        tv3=findViewById(R.id.editText_mobile);

        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    onLocationChanged(location);
        geocoder=new Geocoder(this, Locale.getDefault());
        try {
            addresses=geocoder.getFromLocation(latitude,lonfitude,1);
            address=addresses.get(0).getAddressLine(0);
            String area=addresses.get(0).getLocality();
            String city=addresses.get(0).getAdminArea();
            String countrt=addresses.get(0).getCountryName();
            String postalcode=addresses.get(0).getPostalCode();

            String fulladdress=address+","+area+","+city+","+countrt+","+postalcode;

            tvlocation.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }



        reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                email=dataSnapshot.child("email").getValue().toString();
                name =dataSnapshot.child("name").getValue().toString();
                mobile=dataSnapshot.child("mobile").getValue().toString();
                //String pointsa=dataSnapshot.child("Location").getValue().toString();

                    tv1.setText(email);
                    tv2.setText(name);
                    tv3.setText(mobile);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();


            }
        });






        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);*/
                Intent i=new Intent(getApplicationContext(),AlluserActivity.class);
                startActivity(i);
               /* String label = "I'm Here!";
                String uriBegin = "geo:" + latitude + "," + lonfitude;
                String query = latitude + "," + lonfitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(mapIntent);*/
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference("Hospitals");
                DatabaseReference myRef1 = database.getReference("Users");
                String textvalue=tv3.getText().toString();
                String username=tv1.getText().toString();
                String name=tv2.getText().toString();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
               // myRef.child(userId).child("address").setValue(address);

                //address is longitude
                myRef.child(userId).child("address").setValue(address);

                //email is ltitude
                myRef.child(userId).child("email").setValue(username);

                myRef.child(userId).child("name").setValue(name);
                myRef.child(userId).child("mobile").setValue(mobile);
                Toast.makeText(getApplicationContext(),"Your Loction is Sent ",Toast.LENGTH_LONG).show();

            }
        });




    }

    @Override
    public void onLocationChanged(Location location) {
        lonfitude=location.getLongitude();
        latitude=location.getLatitude();

    }
}
