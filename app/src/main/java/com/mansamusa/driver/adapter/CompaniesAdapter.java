package com.mansamusa.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mansamusa.driver.R;
import com.mansamusa.driver.activity.LoginActivity;
import com.mansamusa.driver.model.CompaniesListResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.MyViewHolder> {
    private Context context;
    ArrayList<CompaniesListResponse> dataCom;
    private ClickCompany clickCompany;


    public CompaniesAdapter(Context context, ArrayList<CompaniesListResponse> dataCom, ClickCompany clickCompany) {
        this.context=context;
        this.dataCom=dataCom;
        this.clickCompany=clickCompany;

    }

    @NonNull
    @Override
    public CompaniesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_country_select,viewGroup,false);
        return new CompaniesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompaniesAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvLanguage.setText(dataCom.get(i).getCompany_name());

        myViewHolder.tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCompany.clickitemCompany(dataCom.get(i).getCompany_name(),dataCom.get(i).getCompany_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataCom!=null ? dataCom.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLanguage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLanguage=itemView.findViewById(R.id.tvLanguage);
        }
    }


    public interface ClickCompany{
        void clickitemCompany(String text,int i);
    }

}

