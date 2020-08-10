package com.mansamusa.driver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mansamusa.driver.R;

public class SelectActivity extends AppCompatActivity {

    @BindView(R.id.cl_seller)
    ConstraintLayout cl_seller;
    @BindView(R.id.cl_buyer)
    ConstraintLayout cl_buyer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);


    }
    @OnClick({R.id.cl_seller,R.id.cl_buyer})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cl_seller:
                startActivity(new Intent(this,SellerActivity.class));
                break;
            case R.id.cl_buyer:
                startActivity(new Intent(this,BuyerActivity.class));
                break;
        }
    }

}
