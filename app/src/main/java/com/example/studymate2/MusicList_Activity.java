package com.example.studymate2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MusicList_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


        recyclerView = (RecyclerView) findViewById(R.id.rc_music); //  아이디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //User객체를 담는 리스트

        database = FirebaseDatabase.getInstance(); //파이어베이스 연동

        databaseReference = database.getReference("Music"); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 DB 데이터를 받아오는 부분
                arrayList.clear(); //기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //데이터를 반복문으로 추출하는 것
                    User user = snapshot.getValue(User.class); //User객체에 가져온 데이터를 담는다
                    arrayList.add(user); //담은 데이터들을 배열리스트에 넣고 리사이클러 뷰에 넣을 준비
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
