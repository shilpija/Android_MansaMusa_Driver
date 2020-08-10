package com.mansamusa.driver.adapter;

import android.content.Context;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mansamusa.driver.R;


import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageHorizontalAdapter extends RecyclerView.Adapter<ImageHorizontalAdapter.MyViewHolder> {
    ArrayList<File> listPhoto;
    private Context context;
    private MyClickListner listner;

    public ImageHorizontalAdapter(Context context, ArrayList<File> listPhoto) {
        this.context=context;
        this.listPhoto=listPhoto;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemType= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_image,viewGroup,false);
        return new ImageHorizontalAdapter.MyViewHolder(itemType);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.iv_photo.setImageURI(Uri.fromFile(listPhoto.get(position)));

//        Glide.with(context)
//                .load(listPhoto.get(position))
//                .apply(new RequestOptions()
//                        .error(R.drawable.dummyimg)
//                        .override(300, 300))
//                .into(holder.iv_photo);




        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPhoto.remove(position);
                if (listner!=null) {
                    listner.onItemRemove(position, v,listPhoto.size());
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listPhoto.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView iv_photo;
        @BindView(R.id.iv_remove)
        ImageView iv_remove;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public void SetonClickListner(MyClickListner listner) {

        this.listner = listner;

    }


    public interface MyClickListner {

        void ImageClick(int Position, View view);

        void onItemClick(int position, View v);
        void onItemRemove(int position, View v, int size);

    }
}
