package com.mansamusa.driver.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mansamusa.driver.R;
import com.mansamusa.driver.adapter.ItemSellerAdapter;
import com.mansamusa.driver.database.SPreferenceKey;
import com.mansamusa.driver.database.SharedPreferenceWriter;
import com.mansamusa.driver.model.CommonResponse;
import com.mansamusa.driver.model.DriverOrderDetails;
import com.mansamusa.driver.model.OrderUpdateResponse;
import com.mansamusa.driver.retrofit.ServicesConnection;
import com.mansamusa.driver.retrofit.ServicesInterface;
import com.mansamusa.driver.utilities.CommonUtilities;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerActivity extends AppCompatActivity {
    ArrayList<CommonResponse> orderItems;
    @BindView(R.id.txt_customerName)
    TextView txt_customerName;
    @BindView(R.id.txt_phoneNum)
    TextView txt_phoneNum;
    @BindView(R.id.txt_orderNo)
    TextView txt_orderNo;
    @BindView(R.id.txt_totalAmount)
    TextView txt_totalAmount;
    @BindView(R.id.rc_itemSeller)
    RecyclerView rc_itemSeller;
    @BindView(R.id.img_sigImage)
    ImageView img_sigImage;
    @BindView(R.id.cl_sig)
    ConstraintLayout cl_sig;
    @BindView(R.id.txt_orderDeliverBt)
    TextView txt_orderDeliverBt;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.cl_sig_delBt)
    ConstraintLayout cl_sig_delBt;
    @BindView(R.id.txt_paymentType)
    TextView txt_paymentType;

    @BindView(R.id.img_navigate)
    ImageView img_navigate;

    @BindView(R.id.ivEdit)
    ImageView ivEdit;

    @BindView(R.id.txt_flatNo)
    TextView txt_flatNo;

    @BindView(R.id.txt_floorNo)
    TextView txt_floorNo;

    @BindView(R.id.txt_buildingNo)
    TextView txt_buildingNo;

    @BindView(R.id.txt_landmark)
    TextView txt_landmark;

    String latitude = "";
    String longitude = "";
    File imgFile;
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        ButterKnife.bind(this);
        orderItems = new ArrayList<>();
        orderId = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.ORDERID);
        serviceOrderDetails();
        ivEdit.setVisibility(View.GONE);
        txt_orderDeliverBt.setBackgroundColor(getResources().getColor(R.color.grey));
        txt_orderDeliverBt.setEnabled(false);
    }

    @OnClick({R.id.cl_sig, R.id.txt_orderDeliverBt, R.id.img_navigate, R.id.ivEdit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cl_sig:
            case R.id.ivEdit:
            case R.id.img_sigImage:
                Intent intent = new Intent(this, SignatureActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.txt_orderDeliverBt:
                if (imgFile != null) {
                    serviceOrderUpdate();
                } else {
                    CommonUtilities.snackBar(this, "Customer signature required");
                }
                break;
            case R.id.img_navigate:
                startActivity(new Intent(this, MapActivity.class).putExtra("lati", latitude)
                        .putExtra("longi", longitude));
                break;
        }
    }

    //driver order detilas
    private void serviceOrderDetails() {
        String userToken = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonResponse> commonResponseCall = servicesInterface.driverOrderDetails("Bearer " + userToken, orderId);

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
                                        txt_orderNo.setText(orderId);
                                        ArrayList<CommonResponse> data = body.getData();
                                        String order_status = data.get(0).getOrder_status();
                                        //String paecelImag=data.get(0).getParcel_image();

                                        if (order_status.equals("Out for delivery")) {
                                            cl_sig_delBt.setVisibility(View.VISIBLE);
                                        } else {
                                            cl_sig_delBt.setVisibility(View.GONE);
                                        }

                                        txt_totalAmount.setText("Total AED " + data.get(0).getTotal());
                                        txt_paymentType.setText(data.get(0).getPayment_method());
                                        CommonResponse details = data.get(0).getUser_info();
                                        CommonResponse delAdd = details.getDelivery_address();
                                        latitude = delAdd.getLatitude();
                                        longitude = delAdd.getLongitude();
                                        setData(details, delAdd);


                                        orderItems = data.get(0).getOrderitems();
                                        setRecyclerView(orderItems);

//                                        for (int j= 0; j<orderItems.size(); j++){
//                                            if(orderItems.get(j).getParcel_image() != null && !orderItems.get(j).getParcel_image().equalsIgnoreCase("")){
//                                                cl_sig_delBt.setVisibility(View.GONE);
//                                            } else{
//                                                cl_sig_delBt.setVisibility(View.VISIBLE);
//                                            }
//                                        }

                                    } else {
                                        CommonResponse error = response.body().getError();
                                        CommonUtilities.snackBar(BuyerActivity.this, error.getMsg());
                                    }
                                } else {
                                    CommonUtilities.snackBar(BuyerActivity.this, "Your request was made with invalid credentials.");
                                }
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(BuyerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

    private void setData(CommonResponse details, CommonResponse delDe) {
        txt_customerName.setText(details.getName());
        txt_phoneNum.setText(details.getPhone_number());
        txt_address.setText(delDe.getLocation());
        txt_flatNo.setText(delDe.getFlat_no());
        txt_floorNo.setText(delDe.getFloor_no());
        txt_buildingNo.setText(delDe.getBuilding_no());
        txt_landmark.setText(delDe.getLandmark());
    }

    private void setRecyclerView(ArrayList<CommonResponse> orderItems) {
        rc_itemSeller.setLayoutManager(new LinearLayoutManager(this));
        ItemSellerAdapter itemSellerAdapter = new ItemSellerAdapter(this, orderItems, "Buyer");
        rc_itemSeller.setAdapter(itemSellerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            if (data != null) {
                String imgPath = data.getStringExtra("imagePath");
                imgFile = new File(imgPath);
                if (!imgPath.isEmpty()) {
//                    Glide.with(this)
//                            .load(Uri.fromFile(imgFile))
////                            .placeholder(R.drawable.loader)
////                            .error(R.drawable.d_img)
//                            .into(img_sigImage);
                    ivEdit.setVisibility(View.VISIBLE);
                    txt_orderDeliverBt.setBackgroundColor(getResources().getColor(R.color.colorBrown));
                    txt_orderDeliverBt.setEnabled(true);

                    Picasso.with(this).load(Uri.fromFile(imgFile)).into(img_sigImage);
                }
            }

        }
    }

    //data set by multipart
    public Map<String, RequestBody> getParam() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("order_id", RequestBody.create(MediaType.parse("text/plain"), orderId));
        return map;
    }

    //buyer order update detilas
    private void serviceOrderUpdate() {
        RequestBody profile_body = RequestBody.create(MediaType.parse("image/*"), imgFile);
        MultipartBody.Part menuPart = MultipartBody.Part.createFormData("order_image", imgFile.getName(), profile_body);

        String userToken = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<OrderUpdateResponse> commonResponseCall = servicesInterface.buyerUpdateDetails("Bearer " + userToken,
                        getParam(), menuPart);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        commonResponseCall,
                        this,
                        true,
                        new Callback<OrderUpdateResponse>() {
                            @Override
                            public void onResponse(Call<OrderUpdateResponse> call, Response<OrderUpdateResponse> response) {
                                if (response.isSuccessful()) {
                                    OrderUpdateResponse body = response.body();
                                    if (body.getCode() == 200) {
                                        serviceOrder();
                                        Toast.makeText(BuyerActivity.this, body.getSuccess(), Toast.LENGTH_SHORT).show();
//                                        CommonUtilities.snackBar(BuyerActivity.this,body.getSuccess());
//                                        startActivity(new Intent(BuyerActivity.this,OrderNoActivity.class));
//                                        finish();

                                        Intent intent = new Intent(BuyerActivity.this, OrderNoActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        CommonUtilities.snackBar(BuyerActivity.this, "Please pick the order");
                                    }
                                } else {
                                    CommonUtilities.snackBar(BuyerActivity.this, "Your request was made with invalid credentials.");
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderUpdateResponse> call, Throwable t) {
                                Toast.makeText(BuyerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

    private void serviceOrder() {
        String userToken = SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<DriverOrderDetails> commonResponseCall = servicesInterface.driverorderdeliverDetails("Bearer " + userToken,
                        orderId);

                ServicesConnection.getInstance().enqueueWithoutRetry(
                        commonResponseCall,
                        this,
                        true,
                        new Callback<DriverOrderDetails>() {
                            @Override
                            public void onResponse(Call<DriverOrderDetails> call, Response<DriverOrderDetails> response) {
                                if (response.isSuccessful()) {
                                    DriverOrderDetails body = response.body();
                                    if (body.getCode() == 200) {

                                    } else {
                                        DriverOrderDetails error = response.body().getError();
                                        CommonUtilities.snackBar(BuyerActivity.this, error.getMsg());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DriverOrderDetails> call, Throwable t) {
                                Toast.makeText(BuyerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

}

