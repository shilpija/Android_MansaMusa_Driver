package com.mansamusa.driver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.animation.keyframe.GradientColorKeyframeAnimation;
import com.mansamusa.driver.adapter.CompaniesAdapter;
import com.mansamusa.driver.database.SPreferenceKey;
import com.mansamusa.driver.database.SharedPreferenceWriter;
import com.mansamusa.driver.R;
import com.mansamusa.driver.model.CommonResponse;
import com.mansamusa.driver.model.CompaniesListResponse;
import com.mansamusa.driver.model.LoginComResponse;
import com.mansamusa.driver.retrofit.ServicesConnection;
import com.mansamusa.driver.retrofit.ServicesInterface;
import com.mansamusa.driver.utilities.CommonUtilities;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements CompaniesAdapter.ClickCompany {

    @BindView(R.id.edt_username)
    EditText edt_username;

    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.txt_loginBt)
    TextView txt_loginBt;

    @BindView(R.id.rb_mansaDriver)
    RadioButton rb_mansaDriver;

    @BindView(R.id.rb_companyDriver)
    RadioButton rb_companyDriver;

    @BindView(R.id.cl_user_pass)
    ConstraintLayout cl_user_pass;

    @BindView(R.id.cl_Com_phone)
    ConstraintLayout cl_Com_phone;

    @BindView(R.id.spinnerCompanies)
    Spinner spinnerCompanies;

    @BindView(R.id.txt_compnyNAme)
    TextView txt_compnyNAme;

    @BindView(R.id.edt_phone_Number)
    EditText edt_phone_Number;

    ArrayList<CompaniesListResponse> dataCompanies;
    String comId="";
    Dialog dialogCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        rb_mansaDriver.setChecked(true);
        rb_companyDriver.setChecked(false);

    }

    @OnClick({R.id.txt_loginBt,R.id.rb_mansaDriver,R.id.rb_companyDriver,R.id.txt_compnyNAme})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.txt_loginBt:

//                if (rb_mansaDriver.isChecked()) {
//                    if (validation()) {
//                        serviceLogin();
//                    }
//                }else {
//                    if (validationLoginCom()){
//                        serviceLoginCompany();
//                    }
//                }

                if (validation()) {
                    serviceLogin();
                }

                //serviceLoginCompany();
                break;
            case R.id.rb_mansaDriver:
                if (rb_mansaDriver.isChecked()) {
                    cl_user_pass.setVisibility(View.VISIBLE);
                    cl_Com_phone.setVisibility(View.GONE);
                }
                break;
            case R.id.rb_companyDriver:
                if (rb_companyDriver.isChecked()) {
                    cl_Com_phone.setVisibility(View.VISIBLE);
                    cl_user_pass.setVisibility(View.GONE);
                }
                serviceCompaniesList();
                break;
            case R.id.txt_compnyNAme:
                serviceCompaniesList();
//                showdialogCompany(dataCompanies);
                break;
        }
    }

    public boolean validation() {
        if (edt_username.getText().toString().isEmpty()) {
            CommonUtilities.snackBar(this, "Please enter email id");
            return false;
        } else if (edt_password.getText().toString().isEmpty()) {
            CommonUtilities.snackBar(this, "Please enter password");
            return false;
        }
        return true;
    }
    public boolean validationLoginCom() {
        if (txt_compnyNAme.getText().toString().isEmpty()) {
            CommonUtilities.snackBar(this, "Please select company name");
            return false;
        } else if (edt_phone_Number.getText().toString().isEmpty()) {
            CommonUtilities.snackBar(this, "Please enter phone number");
            return false;
        }
        return true;
    }

    // hit login API
    private void serviceLogin() {
        String deviceToken= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.DEVICETOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonResponse> commonResponseCall = servicesInterface.loginDetails(edt_username.getText().toString(),
                        edt_password.getText().toString(), "4", deviceToken);

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
                                        CommonResponse dataCommonResponse = response.body().getSuccess();
                                        String userToken = dataCommonResponse.getToken();
                                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SPreferenceKey.TOKEN, userToken);
                                        startActivity(new Intent(LoginActivity.this, OrderNoActivity.class));
                                        finish();
                                    } else {
                                        CommonResponse error=response.body().getError();
                                        CommonUtilities.snackBar(LoginActivity.this, error.getMsg());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

    // hit login Company API
    private void serviceLoginCompany() {

        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<LoginComResponse> commonResponseCall = servicesInterface.loginComDetails(comId,edt_phone_Number.getText().toString());

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        commonResponseCall,
                        this,
                        true,
                        new Callback<LoginComResponse>() {
                            @Override
                            public void onResponse(Call<LoginComResponse> call, Response<LoginComResponse> response) {
                                if (response.isSuccessful()) {
                                    LoginComResponse body = response.body();
                                    if (body.getCode() == 200) {
                                        LoginComResponse dataCommonResponse = response.body().getData();
                                        String userToken = dataCommonResponse.getAccess_token();
                                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SPreferenceKey.TOKEN, userToken);
                                        startActivity(new Intent(LoginActivity.this, OrderNoActivity.class));
                                        finish();
                                    } else {
                                        LoginComResponse error=response.body().getError();
                                        CommonUtilities.snackBar(LoginActivity.this, error.getMsg());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginComResponse> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

    // hit companies list API
    private void serviceCompaniesList() {
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CompaniesListResponse> commonResponseCall = servicesInterface.companiesListDetails();

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        commonResponseCall,
                        this,
                        true,
                        new Callback<CompaniesListResponse>() {
                            @Override
                            public void onResponse(Call<CompaniesListResponse> call, Response<CompaniesListResponse> response) {
                                if (response.isSuccessful()) {
                                    CompaniesListResponse body = response.body();
                                    if (body.getCode() == 200) {
                                        dataCompanies=body.getData();
                                        showdialogCompany(dataCompanies);
                                    } else {
                                        CommonUtilities.snackBar(LoginActivity.this, "Companies is not available");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<CompaniesListResponse> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }



    //show dialog click on select
    public void showdialogCompany(ArrayList<CompaniesListResponse> dataCompanies) {
        dialogCompany = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        dialogCompany.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCompany.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogCompany.setContentView(R.layout.dialog_select_superpower);
        dialogCompany.setCancelable(false);

        RecyclerView recyclerCountry = (RecyclerView) dialogCompany.findViewById(R.id.recyclerCountry);
        ImageView ivCloseKeyword = (ImageView) dialogCompany.findViewById(R.id.ivCloseKeyword);

        recyclerCountry.setLayoutManager(new LinearLayoutManager(this));
        CompaniesAdapter comAdapter=new CompaniesAdapter(this,dataCompanies,this);
        recyclerCountry.setAdapter(comAdapter);

        ivCloseKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCompany.dismiss();
            }
        });


        dialogCompany.show();

    }

    @Override
    public void clickitemCompany(String text, int i) {
        dialogCompany.dismiss();
        txt_compnyNAme.setText(text);
        comId= String.valueOf(i);
    }
}
