package com.mansamusa.driver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mansamusa.driver.adapter.ImageHorizontalAdapter;
import com.mansamusa.driver.adapter.ItemSellerAdapter;
import com.mansamusa.driver.database.SPreferenceKey;
import com.mansamusa.driver.database.SharedPreferenceWriter;
import com.mansamusa.driver.R;
import com.mansamusa.driver.model.CommonResponse;
import com.mansamusa.driver.model.OrderUpdateResponse;
import com.mansamusa.driver.retrofit.ServicesConnection;
import com.mansamusa.driver.retrofit.ServicesInterface;
import com.mansamusa.driver.utilities.CommonUtilities;
import com.mansamusa.driver.utilities.FilePath;
import com.mansamusa.driver.utilities.TakeImage;
import com.mansamusa.driver.utilities.URI_to_Path;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mansamusa.driver.BuildConfig.DEBUG;

public class SellerActivity extends AppCompatActivity implements ItemSellerAdapter.OnItemListener {
    private int itemPosition ;
    private String productId="" ;
    ArrayList<CommonResponse>orderItems;
    CommonResponse imagedata;
    ItemSellerAdapter itemSellerAdapter;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    int PICK_IMAGE_MULTIPLE = 1;
    public static final String DOCUMENTS_DIR = "documents";
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    private String path = "";
    File imgFile;
    String imageEncoded;
    ArrayList<File> imagesEncodedList;
    ArrayList<File> Imgpath = new ArrayList<>();
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    private ImageHorizontalAdapter imageHorizontalAdapter;
    @BindView(R.id.txt_supplierName)
    TextView txt_supplierName;

    @BindView(R.id.txt_city)
    TextView txt_city;

    @BindView(R.id.txt_phoneNumber)
    TextView txt_phoneNumber;

    @BindView(R.id.txt_orderNo)
    TextView txt_orderNo;

    @BindView(R.id.txt_addPhoto)
    TextView txt_addPhoto;

    @BindView(R.id.txt_orderPicked)
    TextView txt_orderPicked;

    @BindView(R.id.img_photoUpload)
    ImageView img_photoUpload;

    @BindView(R.id.rc_itemSeller)
    RecyclerView rc_itemSeller;

    @BindView(R.id.rc_recyHoriz)
    RecyclerView rc_recyHoriz;

    @BindView(R.id.cl_photo_pickedBt)
    ConstraintLayout cl_photo_pickedBt;
    @BindView(R.id.img_navigate)
    ImageView img_navigate;

    @BindView(R.id.txt_flatNo)
    TextView txt_flatNo;

    @BindView(R.id.txt_floorNo)
    TextView txt_floorNo;

    @BindView(R.id.txt_buildingNo)
    TextView txt_buildingNo;

    @BindView(R.id.txt_landmark)
    TextView txt_landmark;

    String latitude="";
    String longitude="";

    private String orderId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        ButterKnife.bind(this);

        imagesEncodedList = new ArrayList<>();
         orderItems = new ArrayList<>();


        orderId= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.ORDERID);
        serviceOrderDetails();
        txt_orderPicked.setBackgroundColor(getResources().getColor(R.color.grey));
        txt_orderPicked.setEnabled(false);
    }


    @OnClick({R.id.txt_addPhoto,R.id.txt_orderPicked,R.id.img_navigate})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.txt_addPhoto:
            case R.id.img_photoUpload:
                showImagePickDialog();
                break;
            case R.id.txt_orderPicked:
               // if (imgFile!=null) {
                    serviceOrderUpdate();
//                }else {
//                    CommonUtilities.snackBar(this,"Please upload image");
//                }
                break;
            case R.id.img_navigate:
                startActivity(new Intent(this,MapActivity.class).putExtra("lati",latitude)
                .putExtra("longi",longitude));

                /*Intent intent = new Intent(this,MapActivity.class);
                intent.putExtra("lati",latitude);
                intent.putExtra("longi",longitude);
                startActivity(intent);*/
                break;
        }
    }

    //driver order detilas
    private void serviceOrderDetails() {
        String userToken= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonResponse> commonResponseCall = servicesInterface.driverOrderDetails("Bearer "+userToken,orderId);

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
                                        ArrayList<CommonResponse>data=body.getData();

                                        String order_status=data.get(0).getOrder_status();
                                        String paecelImag=data.get(0).getParcel_image();
                                        if (paecelImag!=null) {
                                            if (order_status.equals("Processing") && paecelImag.equals("")) {
                                                cl_photo_pickedBt.setVisibility(View.VISIBLE);
                                            } else {
                                                cl_photo_pickedBt.setVisibility(View.GONE);
                                            }
                                        }

                                        CommonResponse details=data.get(0).getKitchen_location();
                                        setData(details);

                                        latitude=details.getLatitude();
                                        longitude=details.getLongitude();

                                        orderItems=data.get(0).getOrderitems();
                                        setRecyclerView(orderItems);

                                        for (int j= 0; j<orderItems.size(); j++){
                                            if(orderItems.get(j).getParcel_image() != null && !orderItems.get(j).getParcel_image().equalsIgnoreCase("")){
                                                txt_orderPicked.setVisibility(View.GONE);
                                                txt_orderPicked.setBackgroundColor(getResources().getColor(R.color.colorBrown));
                                                txt_orderPicked.setEnabled(true);
                                            } else{
                                                txt_orderPicked.setVisibility(View.VISIBLE);
                                                txt_orderPicked.setEnabled(false);
                                                txt_orderPicked.setBackgroundColor(getResources().getColor(R.color.grey));
                                            }
                                        }

                                    } else {
                                        CommonResponse error=response.body().getError();
                                        CommonUtilities.snackBar(SellerActivity.this, error.getMsg());
                                    }
                                }else{
                                    CommonUtilities.snackBar(SellerActivity.this, "Your request was made with invalid credentials.");
                                }
                            }
                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(SellerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }





    private void setData(CommonResponse details) {
        txt_supplierName.setText(details.getUsername());
        txt_phoneNumber.setText(details.getPhone_number());
        txt_city.setText(details.getLocation());
        txt_flatNo.setText(details.getFlat_no());
        txt_floorNo.setText(details.getFloor_no());
        txt_buildingNo.setText(details.getBuilding_no());
        txt_landmark.setText(details.getLandmark());
    }


    private void setRecyclerView(ArrayList<CommonResponse> orderItems) {
        rc_itemSeller.setLayoutManager(new LinearLayoutManager(this));
         itemSellerAdapter=new ItemSellerAdapter(this,orderItems,this,"Seller");
        rc_itemSeller.setAdapter(itemSellerAdapter);
    }

    //Profile Image get click on Button
    private void showImagePickDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Choose image");
//        dialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(SellerActivity.this, TakeImage.class);
//                intent.putExtra("from","gallery");
//                startActivityForResult(intent,REQ_CODE_PICK_IMAGE);

//                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
//                    marshMallowPermission.requestPermissionForExternalStorage();
//                } else {
//                    Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                    intent2.setType("image/*");
//                    //intent.setType("image/*|application/pdf");
//                    intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    startActivityForResult(Intent.createChooser(intent2, "Select Picture"), REQ_CODE_PICK_IMAGE);
//
//                }





//            }
//        });
        dialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SellerActivity.this, TakeImage.class);
                intent.putExtra("from","camera");
                startActivityForResult(intent,CAMERA_PIC_REQUEST);
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private List<MultipartBody.Part> menuPart() {
        List<MultipartBody.Part> list = new ArrayList<>();

        for (int i = 0; i < imagesEncodedList.size(); i++) {
            RequestBody profile_body = RequestBody.create(MediaType.parse("image/*"), imagesEncodedList.get(i));
            MultipartBody.Part menuPart = MultipartBody.Part.createFormData("parcel_image", imagesEncodedList.get(i).getName(), profile_body);
            //MultipartBody.Part menuPart = MultipartBody.Part.createFormData("files[" + i + "]", imagesEncodedList.get(i).getName(), profile_body);
            list.add(menuPart);
        }
        return list;
    }




    @Override
    public void onItemRowClick(int position, String product_id) {
        showImagePickDialog();
        itemPosition = position;
        productId = product_id;

    }

    @Override
    public void onItemRemoveClick(int position, String product_id) {
        itemPosition = position;
        productId = product_id;
        Intent intent = new Intent(SellerActivity.this, TakeImage.class);
        intent.putExtra("from","camera");
        startActivityForResult(intent,CAMERA_PIC_REQUEST);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            path = data.getStringExtra("filePath");
            if (path != null) {

                imgFile = new File(path);
                callDriverparcelApi(imgFile);

//                if (imgFile.exists()) {
//                    txt_addPhoto.setVisibility(View.GONE);
//                    img_photoUpload.setVisibility(View.VISIBLE);
//                    Glide.with(this)
//                            .load(Uri.fromFile(imgFile))
////                            .placeholder(R.drawable.loader)
//                            .error(R.drawable.d_img)
//                            .into(img_photoUpload);


                   // Picasso.with(this).load(Uri.fromFile(imgFile)).into(img_photoUpload);


             //   }
            }

        }


//        try {
//            // When an Image is picked
//
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            //imagesEncodedList = new ArrayList<>();
//
//            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {
//                // Get the Image from data
//
//                if (data.getClipData() != null) {
//                    ClipData mClipData = data.getClipData();
//                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                    for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                        ClipData.Item item = mClipData.getItemAt(i);
//                        Uri uri = item.getUri();
//                        mArrayUri.add(uri);
//                        // Get the cursor
//                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                        // Move to first row
//                        cursor.moveToFirst();
//
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        imageEncoded = cursor.getString(columnIndex);
//
//                        if (imageEncoded == null) {
//                            //   String selectedFilePath = FilePath.getPath(getActivity(), mImageUri);
//                            String selectedFilePath = getPath(this, uri);
//
//                            if (isGoogleDriveUri(uri)) {
//                                selectedFilePath = getDriveFilePath(uri, this);
//                            }
//
//                            imagesEncodedList.add(new File(selectedFilePath));
//                            rc_recyHoriz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//                            imageHorizontalAdapter = new ImageHorizontalAdapter(this, imagesEncodedList);
//                            rc_recyHoriz.setAdapter(imageHorizontalAdapter);
//
//                        } else {
//                            imagesEncodedList.add(new File(imageEncoded));
//                        }
//
//                        // imagesEncodedList.add(new File(imageEncoded));
//                        Imgpath = imagesEncodedList;
//                        rc_recyHoriz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//                        imageHorizontalAdapter = new ImageHorizontalAdapter(this, Imgpath);
//                        rc_recyHoriz.setAdapter(imageHorizontalAdapter);
//                        // tvChooseFile.setText(Imgpath.size() + " " + "files attached");
//                        cursor.close();
//
//                    }
//                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
//                } else {
//
//                    if (data.getData() != null) {
//
//                        Uri mImageUri = data.getData();
//
//                        // Get the cursor
//                        Cursor cursor = getContentResolver().query(mImageUri,
//                                new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
//                        // Move to first row
//                        cursor.moveToFirst();
//
//                        // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        imageEncoded = cursor.getString(0);
//
//                        if (imageEncoded == null) {
//                            //   String selectedFilePath = FilePath.getPath(getActivity(), mImageUri);
//                            String selectedFilePath = null;
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                                selectedFilePath = getPath(this, mImageUri);
//                            }
//
////                            if (isGoogleDriveUri(mImageUri))
////                            {
////                                selectedFilePath =getDriveFilePath(mImageUri,this);
////                            }
//                            imagesEncodedList.add(new File(selectedFilePath));
//                        } else {
//                            imagesEncodedList.add(new File(imageEncoded));
//                        }
//                        // imagesEncodedList.add(new File(imageEncoded));
//                        Imgpath = imagesEncodedList;
//                        rc_recyHoriz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//                        imageHorizontalAdapter = new ImageHorizontalAdapter(this, Imgpath);
//                        rc_recyHoriz.setAdapter(imageHorizontalAdapter);
//                        //tvChooseFile.setText(Imgpath.get(0).getName());
//                        cursor.close();
//
//                    }
//                }
//
//
//            } else if (resultCode == RESULT_OK) {
//                if (data != null)
//                    if (data.getStringExtra("filePath") != null) {
//
//                        Uri mImageUri = data.getParcelableExtra("uri");
//                        final String pdfFilePath = data.getStringExtra("filePath");
//                        imagesEncodedList.add(new File(pdfFilePath));
//                        Imgpath = imagesEncodedList;
//
//                        rc_recyHoriz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//                        imageHorizontalAdapter = new ImageHorizontalAdapter(this, Imgpath);
//                        rc_recyHoriz.setAdapter(imageHorizontalAdapter);
//                        //tvChooseFile.setText(Imgpath.get(0).getName());
//
//                    }
//            }//
//            /////////////////////
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
//        }
        super.onActivityResult(requestCode, resultCode, data);




    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == START_VERIFICATION) {
//            if (resultCode == RESULT_OK) {
//                setResult(RESULT_OK);
//                finish();
//            }
//        } else if (resultCode == RESULT_OK) {
//            if (data.getStringExtra("filePath") != null) {
//                imagePath = data.getStringExtra("filePath");
//                fileFlyer = new File(data.getStringExtra("filePath"));
//
//                if (fileFlyer.exists() && fileFlyer != null) {
//                    img_photoUpload.setVisibility(View.VISIBLE);
//                    //img_photoUpload.setImageURI(Uri.fromFile(fileFlyer));
//                    //Picasso.with(this).load(Uri.fromFile(fileFlyer)).into(img_photoUpload);
//                    //ivMyProfile.setImageURI(Uri.fromFile(fileFlyer));
//                    Glide.with(SellerActivity.this)
//                            .load(imagePath).error(R.drawable.camera).into(img_photoUpload);
//                }
//            }
//        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
//            finish();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }




    //data set by multipart
    public Map<String, RequestBody> getParam() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("order_id", RequestBody.create(MediaType.parse("text/plain"),orderId));
        map.put("status_id", RequestBody.create(MediaType.parse("text/plain"),"10"));
        return map;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (URI_to_Path.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (URI_to_Path.isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = URI_to_Path.getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                    }
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            // MediaProvider
            else if (URI_to_Path.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return URI_to_Path.getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (FilePath.isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return URI_to_Path.getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }




    //	permissions
    public class MarshMallowPermission {
        Activity activity;

        public MarshMallowPermission(Activity activity) {
            this.activity = activity;
        }

        public boolean checkPermissionForRecord() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public boolean checkPermissionForExternalStorage() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public boolean requestPermissionForExternalStorage() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
            return true;
        }

        public boolean checkPermissionForCamera() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    public File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logDir(context.getCacheDir());
        logDir(dir);

        return dir;
    }
    private static void logDir(File dir) {
        if (!DEBUG) return;
        //Log.d(TAG, "Dir=" + dir);
        File[] files = dir.listFiles();
        for (File file : files) {
            // Log.d(TAG, "File=" + file.getPath());
        }


    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            // Log.w(TAG, e);
            return null;
        }

        logDir(directory);

        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //    requestCode == CAMERA_REQUEST
    public static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    public static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }


    //driver order update detilas
    private void serviceOrderUpdate() {
//        RequestBody profile_body = RequestBody.create(MediaType.parse("image/*"), imgFile);
//        MultipartBody.Part menuPart = MultipartBody.Part.createFormData("parcel_image", imgFile.getName(), profile_body);

        String userToken= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<OrderUpdateResponse> commonResponseCall = servicesInterface.driverOrderUpdateDetails("Bearer "+userToken,
                        getParam());

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
                                        Toast.makeText(SellerActivity.this, body.getSuccess(), Toast.LENGTH_SHORT).show();
//                                        CommonUtilities.snackBar(SellerActivity.this,body.getSuccess());
                                        startActivity(new Intent(SellerActivity.this,BuyerActivity.class));
                                        finish();
                                    } else {
                                        CommonUtilities.snackBar(SellerActivity.this,"not successfully");
                                    }
                                }else{
                                    CommonUtilities.snackBar(SellerActivity.this, "Your request was made with invalid credentials.");
                                }
                            }
                            @Override
                            public void onFailure(Call<OrderUpdateResponse> call, Throwable t) {
                                Toast.makeText(SellerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtilities.snackBar(this, "Please check your internet");
        }
    }

    //data set by multipart
    public Map<String, RequestBody> getImagesApiParam() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("order_id", RequestBody.create(MediaType.parse("text/plain"),orderId));
        map.put("product_id", RequestBody.create(MediaType.parse("text/plain"),productId));
        return map;
    }


    //driver order detilas images uploads
    private void callDriverparcelApi(File imagePath) {
        RequestBody profile_body = RequestBody.create(MediaType.parse("image/*"), imagePath);
        MultipartBody.Part imagesPart = MultipartBody.Part.createFormData("parcel_image", imagePath.getName(), profile_body);

        String userToken= SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.TOKEN);
        if (CommonUtilities.isNetworkAvailable(this)) {
            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService();
                Call<CommonResponse> commonResponseCall = servicesInterface.driverparcel("Bearer "+userToken,getImagesApiParam(),imagesPart);

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

                                        imagedata = response.body().getObjdata();

                                        //orderItems.set(itemPosition,imagedata);
                                        orderItems.get(itemPosition).setParcel_image(imagedata.getParcel_image());
                                        orderItems.get(itemPosition).setfCamera("true");
                                        itemSellerAdapter.notifyDataSetChanged();

                                        for (int j= 0; j<orderItems.size(); j++){
                                            if(orderItems.get(j).getParcel_image() != null && !orderItems.get(j).getParcel_image().equalsIgnoreCase("")){
                                                txt_orderPicked.setBackgroundColor(getResources().getColor(R.color.colorBrown));
                                                txt_orderPicked.setEnabled(true);
                                                txt_orderPicked.setVisibility(View.VISIBLE);

                                            } else{
                                                txt_orderPicked.setEnabled(false);
                                                txt_orderPicked.setBackgroundColor(getResources().getColor(R.color.grey));
                                                txt_orderPicked.setVisibility(View.VISIBLE);
                                                break;
                                            }
                                        }


                                    } else {
                                        CommonResponse error=response.body().getError();
                                        CommonUtilities.snackBar(SellerActivity.this, error.getMsg());
                                    }
                                }else{
                                    CommonUtilities.snackBar(SellerActivity.this, "Your request was made with invalid credentials.");
                                }
                            }
                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                Toast.makeText(SellerActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
