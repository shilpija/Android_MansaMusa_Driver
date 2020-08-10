package com.mansamusa.driver.utilities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.mansamusa.driver.R;
import com.mansamusa.driver.activity.LoginActivity;
import com.mansamusa.driver.retrofitExtra.CustomProgressBar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by Mahipal Singh on 26,June,2018
 * mahisingh1@outlook.com
 */
public class CommonUtilities {
    public static CustomProgressBar customProgressBar;
    public  static String path;
    public static Uri fileUri;
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+[\\.+[a-z]+]+";
    public static String MobilePattern = "[0-9]{10}";
    public static String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
    ///////email check string///////////
//   public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//    public static String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";


    public static void showLoadingDialog(Activity activity){
        if(customProgressBar==null)
            customProgressBar = CustomProgressBar.show(activity, true, true);

        try {
            customProgressBar.setCancelable(false);
            customProgressBar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissLoadingDialog(){
        try
        {
            if (null != customProgressBar && customProgressBar.isShowing()) {
                customProgressBar.dismiss();
                customProgressBar=null;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void showDialog(Activity activity, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", null);;
        builder.show();
    }


    public static void showDialogLoggedIn(Activity activity, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog OptionDialog = builder.create();
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
                OptionDialog.dismiss();

            }
        });
        builder.show();
    }





    public static void hideSoftKeyboard(Activity activity) {
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)activity.
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }



    public static String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(day, month, year);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }


    public static void snackBar(Activity activity, String message) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setMinimumHeight(20);
        snackBarView.setBackgroundColor(Color.parseColor("#FFC53F"));
//        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView tv = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        tv.setTextSize(14);
        //Fonts.OpenSans_Regular_Txt(tv, activity.getAssets());
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }


    //    METHOD: TO DRAW UNDERLINE BELOW TEXT_VIEW AND MAKE TEXT AND LINE IN BLUE COLOR
    public static void setUpSpannableStringColor(TextView textView, int start, int end, String str, int color) {
        SpannableString content = new SpannableString(str);
        content.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }

    // to set style a particular text
    public static void setUpSpannableStyle(TextView textView, int start, int end, String str){
        SpannableString content = new SpannableString(str);
        content.setSpan(new StyleSpan(Typeface.ITALIC), start, end, 0);
        textView.setText(content);
    }


    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting()
                        || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }





//    public static void setToolbar(final AppCompatActivity activity, androidx.appcompat.widget.Toolbar toolbar, TextView title, String TitleContent){
//        activity.setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.back);
//        title.setText(TitleContent);
//        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.onBackPressed();
//            }
//        });
//    }


    // show date picker dialog box
    public static void showDatePicker(Context context, final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = mdformat.format(calendar.getTime());
        String[] currentDateArray = currentDate.split("/");
        int yyyy = Integer.parseInt(currentDateArray[0]);
        int mm = Integer.parseInt(currentDateArray[1])-1;
        int dd = Integer.parseInt(currentDateArray[2]);

        DatePickerDialog pickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date mdate = new Date(year, month, dayOfMonth-1);
                String dayOfWeek = simpledateformat.format(mdate);
                int mMonth = month+1;
                String date  = "";
                switch (mMonth){
                    case 1:
                        date = "Jan-"+year;
                        break;
                    case 2:
                        date = "Feb-"+year;
                        break;
                    case 3:
                        date = "March-"+year;
                        break;
                    case 4:
                        date = "April-"+year;
                        break;
                    case 5:
                        date = "May-"+year;
                        break;
                    case 6:
                        date = "June-"+year;
                        break;
                    case 7:
                        date = "July-"+year;
                        break;
                    case 8:
                        date = "Aug-"+year;
                        break;
                    case 9:
                        date = "Sep-"+year;
                        break;
                    case 10:
                        date = "Oct-"+year;
                        break;
                    case 11:
                        date = "Nov-"+year;
                        break;
                    case 12:
                        date = "Dec-"+year;
                        break;

                }
                textView.setText(date);
            }
        },yyyy,mm,dd);
        pickerDialog.show();
    }

    // show time picker dialog box
    public static void showTimePicker(Context context, final TextView textView){
        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String time=String.format(Locale.US, "%02d:%02d", hourOfDay, minute);

                SimpleDateFormat input = new SimpleDateFormat("HH:mm");
                SimpleDateFormat output = new SimpleDateFormat("hh:mm");
                Date timeInput = null;
                try {
                    timeInput=input.parse(time);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String timeUpdate =output.format(timeInput);

                String AM_PM;
                if (hourOfDay >=0 && hourOfDay < 12){
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                textView.setText(timeUpdate+" "+AM_PM);
            }
        },00,00,false);
        dialog.show();
    }








    public static void openFile(Context context, String url) throws IOException {
        try {
// Create URI
            Uri uri = Uri.parse(url);

            Intent intent = new Intent ();
// Check what kind of file you are trying to open, by comparing the url with extensions.
// When the if condition is matched, plugin sets the correct intent (mime) type,
// so Android knew what application to use to open the file
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
// Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
// PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
// Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
// Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
// WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
// RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
// WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
// GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
// JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
// Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
// Video files
                intent.setDataAndType(uri, "video/*");
            } else {
//if you want you can also define the intent type for any other file

//additionally use else clause below, to manage other unknown extensions
//in this case, Android will show all applications installed on the device
//so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
