package com.mansamusa.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mansamusa.driver.R;
import com.mansamusa.driver.model.CommonResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemSellerAdapter extends RecyclerView.Adapter<ItemSellerAdapter.MyViewHolder> {
    ArrayList<CommonResponse> orderItems;
    private Context context;
    private OnItemListener onItemListener;
    private String from;

    public ItemSellerAdapter(Context context, ArrayList<CommonResponse> orderItems, OnItemListener onItemListener, String from) {
        this.context = context;
        this.orderItems = orderItems;
        this.onItemListener = onItemListener;
        this.from = from;
    }

    public ItemSellerAdapter(Context context, ArrayList<CommonResponse> orderItems, String from) {
        this.context = context;
        this.orderItems = orderItems;
        this.from = from;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_seller, parent, false);
        return new ItemSellerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_itemName.setText(orderItems.get(position).getName());
        holder.txt_quantity.setText("Quantity: " + orderItems.get(position).getQuantity());
        Double aa = Double.parseDouble(orderItems.get(position).getQuantity());
        Double pricetxt = Double.parseDouble(orderItems.get(position).getPrice().replaceAll(",", ""));
        Double res = aa * pricetxt;
        holder.txt_price.setText("AED " + String.valueOf(res));

        String url1 = orderItems.get(position).getDish_image();
        if (url1 != null && !url1.isEmpty() && !url1.equalsIgnoreCase(" ")) {
            Glide.with(context)
                    .load(url1)
                    .apply(RequestOptions.placeholderOf(R.drawable.loader))
                    .error(R.drawable.d_img)
                    .into(holder.img_item);
        }


        if (orderItems.get(position).getParcel_image() != null && !orderItems.get(position).getParcel_image().equalsIgnoreCase("")) {

            if (orderItems.get(position).getfCamera() != null && !orderItems.get(position).getfCamera().equalsIgnoreCase("")) {
                holder.img_photoUpload.setVisibility(View.VISIBLE);
                holder.iv_remove.setVisibility(View.VISIBLE);
                holder.tvclickPhoto.setVisibility(View.GONE);
                Glide.with(context)
                        .load(orderItems.get(position).getParcel_image())
                        .placeholder(R.drawable.d_img)
                        .error(R.drawable.d_img)
                        .into(holder.img_photoUpload);

            } else {
                holder.img_photoUpload.setVisibility(View.VISIBLE);
                holder.iv_remove.setVisibility(View.GONE);
                holder.tvclickPhoto.setVisibility(View.GONE);
                Glide.with(context)
                        .load(orderItems.get(position).getParcel_image())
                        .placeholder(R.drawable.d_img)
                        .error(R.drawable.d_img)
                        .into(holder.img_photoUpload);
//                Glide.with(context)
//                        .load(orderItems.get(position).getParcel_image())
//                        .apply(RequestOptions.placeholderOf(R.drawable.loader))
//                        .error(R.drawable.d_img)
//                        .into(holder.img_photoUpload);

            }


        } else {
            holder.img_photoUpload.setVisibility(View.GONE);
            holder.iv_remove.setVisibility(View.GONE);
            if (from.equalsIgnoreCase("Seller")) {
                holder.tvclickPhoto.setVisibility(View.VISIBLE);
            } else {
                holder.tvclickPhoto.setVisibility(View.GONE);
            }

        }


    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    public interface OnItemListener {
        default void onItemRowClick(int position, String productId) {
        }

        ;

        default void onItemRemoveClick(int position, String productId) {
        }

        ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_itemName)
        TextView txt_itemName;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_price)
        TextView txt_price;
        @BindView(R.id.tvclickPhoto)
        TextView tvclickPhoto;
        @BindView(R.id.img_item)
        ImageView img_item;
        @BindView(R.id.iv_remove)
        ImageView iv_remove;
        @BindView(R.id.img_photoUpload)
        ImageView img_photoUpload;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvclickPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemListener.onItemRowClick(getAdapterPosition(), orderItems.get(getAdapterPosition()).getDish_id());
                }
            });

            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemListener.onItemRemoveClick(getAdapterPosition(), orderItems.get(getAdapterPosition()).getDish_id());
                }
            });


        }
    }
}
