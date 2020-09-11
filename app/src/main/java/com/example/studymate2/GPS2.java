package com.example.studymate2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

public class GPS2 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    private GoogleMap mMap;
    private ArrayList<MarkerItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.53, 126.95669), 14));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);


        setCustoMarkerView();
        getSampleMarkerItems();


    }

    private void getSampleMarkerItems() {
        ArrayList<MarkerItem> sampleList = new ArrayList();

        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Location");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MarkerItem markerItem = snapshot.getValue(MarkerItem.class);
                    arrayList.add(markerItem);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        for (MarkerItem markerItem : sampleList) {
            addMarker(markerItem, false);
        }


    }

    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {

        LatLng position = new LatLng(markerItem.getLatitute(), markerItem.getLongitute());
        String name = markerItem.getName();
        String formatted = NumberFormat.getCurrencyInstance().format((name));

        tv_marker.setText(formatted);

        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.board);
            tv_marker.setTextColor(Color.WHITE);
        } else {
            tv_marker.setBackgroundResource(R.drawable.board);
            tv_marker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(String.valueOf(name));
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));


        return mMap.addMarker(markerOptions);

    }

    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;
       String name = String.valueOf(marker.getTitle());
        MarkerItem temp = new MarkerItem(latitude, longitude, name);
        return addMarker(temp, isSelectedMarker);

    }

    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;

    }


    private void setCustoMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.activity_location, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);

        changeSelectedMarker(marker);

        return true;

    }

    private void changeSelectedMarker(Marker marker) {
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();
        }

        // 선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            marker.remove();
        }
    }
}
