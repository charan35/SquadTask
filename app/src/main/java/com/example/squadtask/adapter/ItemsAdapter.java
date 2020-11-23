package com.example.squadtask.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.squadtask.R;
import com.example.squadtask.model.Result;

import java.util.Collections;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Result> data= Collections.emptyList();
    Result current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public ItemsAdapter(Context context, List<Result> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.items_list, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder= (MyHolder) holder;
        Result current=data.get(position);
        myHolder.mRestName.setText(current.name);
        myHolder.mRating.setText(current.rating);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.food);
        requestOptions.error(R.drawable.food);

        Glide.with(context)
                .asBitmap()
                .load(current.icon)
                .apply(requestOptions)
                .into(myHolder.mPicture);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{

        private ImageView mPicture;
        private TextView mRestName, mStatus, mRating;
        private LinearLayout mRowContainer;

        public MyHolder(View itemView) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.image);
            mRestName = itemView.findViewById(R.id.rest_name);
            mRating = itemView.findViewById(R.id.rating);
            mRowContainer = itemView.findViewById(R.id.row_container);
        }
    }

}
