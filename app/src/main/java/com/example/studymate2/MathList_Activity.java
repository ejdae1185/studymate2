package com.example.studymate2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MathList_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.appbar_action, menu);
//
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search :
//                Toast.makeText(getApplicationContext(), "검색", Toast.LENGTH_SHORT).show();
//                return true ;
//            case R.id.action_location :
//                Toast.makeText(getApplicationContext(), "위치설정", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), Location.class);
//                startActivity(intent);
//                return true ;
//
//            default :
//                return super.onOptionsItemSelected(item) ;
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);



        recyclerView = (RecyclerView) findViewById(R.id.rc_math); //  아이디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //User객체를 담는 리스트

        database = FirebaseDatabase.getInstance(); //파이어베이스 연동

        databaseReference = database.getReference("Math"); //DB 테이블 연결
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
            public void onCancelled(@NonNull DatabaseError databaseError){

                //DB를 가져오다가 err 발생하면 뭘 띄워줄지
                Log.e("SubActivity", String.valueOf(databaseError.toException())); //에러문 출력
            }


        });

        adapter = new CustomerAdapter(arrayList, this); //CustomAdapter 생성자에 넘겨줌
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어뎁터 연결

Button button = findViewById(R.id.btn_location_math);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), GPS2.class);
        startActivity(intent);

    }
});

    }


}

