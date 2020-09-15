package com.example.studymate2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    private MarkerItem markerItem;
    private GoogleMap mMap;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<MarkerItem> arrayList;
    private Marker mMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        arrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance(); //파이어베이스 연동

        databaseReference = database.getReference("Math"); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 DB 데이터를 받아오는 부분
                arrayList.clear(); //기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //데이터를 반복문으로 추출하는 것
                    MarkerItem markerItem
                            = snapshot.getValue(MarkerItem.class); //User객체에 가져온 데이터를 담는다
                    arrayList.add(markerItem); //담은 데이터들을 배열리스트에 넣고 리사이클러 뷰에 넣을 준비
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //DB를 가져오다가 err 발생하면 뭘 띄워줄지
                Log.e("MapActivity", String.valueOf(databaseError.toException())); //에러문 출력
            }


        });
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng latLng = new LatLng(markerItem.getLat(), markerItem.getLon());


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.537523, 126.96558), 14));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        mMarker = googleMap.addMarker(new MarkerOptions()
        .position(latLng)
        .title(markerItem.getName()));



        setCustomMarkerView();
        getSampleMarkerItems();


        }





    private void setCustomMarkerView() {

        marker_root_view = LayoutInflater.from(this).inflate(R.layout.markerlayout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }


    public void setLocation() {

        DatabaseReference mdatabaseReference = databaseReference.child("Location");
        mdatabaseReference.setValue(markerItem);
    }
    private void getSampleMarkerItems() {

        ArrayList<MarkerItem> sampleList = new ArrayList();

        sampleList.add(new MarkerItem(37.538523, 126.96568, "2500000"));
        sampleList.add(new MarkerItem(37.527523, 126.96568, "100000"));
        sampleList.add(new MarkerItem(37.549523, 126.96568, "15000"));
        sampleList.add(new MarkerItem(37.538523, 126.95768, "5000"));



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                MarkerItem markerItem = new MarkerItem(37.549523, 126.96568, "15000");
                markerItem.setLat((double) snapshot.child("latitude").getValue());
                markerItem.setLon((double) snapshot.child("longitude").getValue());
                markerItem.setName((String) snapshot.child("name").getValue());
                MarkerItem item = snapshot.getValue(MarkerItem.class);
                arrayList.add(item);

addMarker(item,false);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

        for (MarkerItem markerItem : sampleList) {

            addMarker(markerItem, false);
        }

}


    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String name = markerItem.getName();




        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.board);
            tv_marker.setTextColor(Color.WHITE);
        } else {
            tv_marker.setBackgroundResource(R.drawable.board);
            tv_marker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(name);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));


        return mMap.addMarker(markerOptions);

    }


    // View를 Bitmap으로 변환
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


    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String name = String.valueOf(marker.getTitle());
        MarkerItem temp = new MarkerItem(37.549523, 126.96568, "15000");
        return addMarker(temp, isSelectedMarker);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);

        changeSelectedMarker(marker);

        return true;
    }


    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
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


    @Override
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }


}
