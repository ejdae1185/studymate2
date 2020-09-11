package com.example.studymate2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Koreanlist extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kor);

        recyclerView = (RecyclerView) findViewById(R.id.rc_kor);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //User객체를 담는 리스트

        database = FirebaseDatabase.getInstance(); //파이어베이스 연동

        databaseReference = database.getReference("Korea"); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    arrayList.add(user);
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //DB를 가져오다가 err 발생하면 뭘 띄워줄지
                Log.e("SubActivity", String.valueOf(databaseError.toException())); //에러문 출력
            }


        });

        adapter = new CustomerAdapter(arrayList, this); //CustomAdapter 생성자에 넘겨줌
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어뎁터 연결


    }


}
