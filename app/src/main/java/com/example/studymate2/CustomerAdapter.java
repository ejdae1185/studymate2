package com.example.studymate2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomViewHolder> {
    private ArrayList<User> arrayList;
    private Context context;


    public CustomerAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_itme, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;


    }



    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.iv_profile);
        holder.tv_type.setText(arrayList.get(position).getType());
        holder.tv_address.setText(arrayList.get(position).getAddress());
        holder.tv_classinfo.setText(arrayList.get(position).getClassinfo());
        holder.tv_phone.setText(arrayList.get(position).getPhone());
        holder.tv_major.setText(arrayList.get(position).getMajor());
        holder.tv_name.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        //삼항연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile;
        TextView tv_type;
        TextView tv_address;
        TextView tv_name;
        TextView tv_major;
        TextView tv_phone;
        TextView tv_classinfo;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_type = itemView.findViewById(R.id.tv_tpye);
            this.tv_address = itemView.findViewById(R.id.tv_address);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_classinfo = itemView.findViewById(R.id.tv_classinfo);
            this. tv_phone = itemView.findViewById(R.id.tv_phone);
            this.tv_major = itemView.findViewById(R.id.tv_major);
        }
    }
}
