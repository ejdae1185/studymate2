package com.example.studymate2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.studymate2.GetLocation.RequestPermissionCode;


public class Location extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public double currentLatitude;
    public double currentLongitude;
    public static final int ROUND = 10;
    private GoogleMap mMap;
    public GoogleApiClient googleApiClient;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;
    public FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ChildEventListener mChildEventListener;
        mUsers = FirebaseDatabase.getInstance().getReference("coordinates");
        mUsers.push().setValue(marker);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        currentLatitude = location.getLatitude();               //getting current latitude
        currentLongitude = location.getLongitude();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()                             //Through this we are fetching the last location of user recorded
                    .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(final android.location.Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object

                                //making button clickable and to open the maps with nearby bus and metro stations from your current location


                                //making button clickable and to open the maps with nearby bus and metro stations from your current location


                                //making button clickable and to open the maps with nearby bus and metro stations from your current location



                            }
                        }
                    });
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Location.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.google_style));
            if (!success) {
                // Handle map style load failure
                Log.e("map_style", "map style updated please do check it");
            }
        } catch (Resources.NotFoundException e) {
            // Oops, looks like the map style resource couldn't be found!
            Log.e("map_style", "map is not updated yet ... do some other stuff");


        }
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMapType(R.raw.google_style);

        int height = 80;
        int width = 80;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_baseline_location_on_24);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    UserInformation user = s.getValue(UserInformation.class);
                    LatLng location = new LatLng(Double.parseDouble(user.latitude), Double.parseDouble(user.longitude));
                    mMap.addMarker(new MarkerOptions().position(location).title(user.name)).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();       //discarding the google api client service if the activity is in onStop condition
        }
        super.onStop();
    }

}