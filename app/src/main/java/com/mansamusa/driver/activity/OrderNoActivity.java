package com.mansamusa.driver.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mansamusa.driver.database.SPreferenceKey;
import com.mansamusa.driver.database.SharedPreferenceWriter;
import com.mansamusa.driver.R;
import com.mansamusa.driver.model.CommonResponse;
import com.mansamusa.driver.retrofit.ServicesConnection;
import com.mansamusa.driver.retrofit.ServicesInterface;
import com.mansamusa.driver.utilities.CommonUtilities;

public class OrderNoActivity extends AppCompatActivity {
    @BindView(R.id.txt_submitBt)
    TextView txt_submitBt;

    @BindView(R.id.edt_oredrno)
    EditText edt_oredrno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_no);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.txt_submitBt})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.txt_submitBt:
//                startActivity(new Intent(this,SelectActivity.class));
                if (!edt_oredrno.getText().toString().isEmpty()){
                    serviceOrder();
                }else {
                    CommonUtilities.snackBar(this,"Please enter order no.");
                }
                break;
        }
    }



    // hit oreder API
    private void serviceOrder() {
        String userToken= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonResponse> commonResponseCall = servicesInterface.orderDetails(edt_oredrno.getText().toString(),
                        "Bearer "+userToken);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        commonResponseCall,
                        this,
                        true,
                        new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                if (response.isSuccessful()) {
                                    CommonResponse body = response.body();
                                    if (body.getCode() == 200) {
                                        acceptOrder();
                                        SharedPreferenceWriter.getInstance(OrderNoActivity.this).writeStringValue(SPreferenceKey.ORDERID,edt_oredrno.getText().toString());

                                    } else {
                                        CommonResponse error=response.body().getError();
                                        CommonUtilities.snackBar(OrderNoActivity.this, error.getMsg());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(OrderNoActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

    // hit acceptOrder API
    private void acceptOrder() {
        String userToken= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonResponse> commonResponseCall = servicesInterface.acceptDetails("Bearer "+userToken,edt_oredrno.getText().toString());

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        commonResponseCall,
                        this,
                        true,
                        new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                if (response.isSuccessful()) {
                                    CommonResponse body = response.body();
                                    if (body.getCode() == 200) {
                                        startActivity(new Intent(OrderNoActivity.this,SelectActivity.class));
                                    } else {
                                        CommonResponse error=response.body().getError();
                                        CommonUtilities.snackBar(OrderNoActivity.this, error.getMsg());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(OrderNoActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
