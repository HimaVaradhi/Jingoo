package com.oodi.jingoo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReferalCodeActivity extends AppCompatActivity {

    TextView mTxtHeaderName, mTxtHome, Home, txtPhoneNumber;
    ImageView mImgBack , imgShare , imgHelp;
    Button btnShare;
    AppUtils appUtils;
    LinearLayout lnrDetails;
    EditText edtShopName , edtState , edtCity , edtOwnerName , edtPhoneNumber;
    FrameLayout f1 , f2 ;

    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final String DEBUG_TAG = "Contact List";
    private static final int RESULT_OK = -1;
    private static final int PERMISSION_REQUEST_CODE = 1;

    boolean isPopup = true;

    String phone = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")) {
            appUtils.setLocale("hi");
        } else {
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_referal_code);

        init();

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareRefferalCodeActivity.shareRefferalCode.finish();
                finish();
            }
        });

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                try {
                    ShareRefferalCodeActivity.shareRefferalCode.finish();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermission()) {

                Log.e("permission", "Permission already granted.");
            } else {

                requestPermission();
            }
        }

        mTxtHeaderName.setText(getResources().getString(R.string.referral_friend));
        mTxtHome.setText(getResources().getString(R.string.referral_friend));

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (isPopup) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkPermission()) {
                                    Intent it = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                    startActivityForResult(it, CONTACT_PICKER_RESULT);
                                } else {
                                    requestPermission();
                                }
                            }else {
                                Intent it = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(it, CONTACT_PICKER_RESULT);
                            }
                        }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPopup){
                        if (!edtPhoneNumber.getText().toString().equals("")){
                            phone = edtPhoneNumber.getText().toString();
                            imgAdd();
                        }else {
                            Toast.makeText(ReferalCodeActivity.this , "Please enter phone number" , Toast.LENGTH_LONG).show();
                        }

                }else {
                    if (edtShopName.getText().toString().equals("")
                            || edtState.getText().toString().equals("")
                            || edtCity.getText().toString().equals("")
                            || edtOwnerName.getText().toString().equals("")){
                        Toast.makeText(ReferalCodeActivity.this , "Please enter all details before sharing." , Toast.LENGTH_LONG).show();
                    }else {
                        share_new_contact();
                    }

                }
            }
        });

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);

    }


    private boolean checkPermission() {

//Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(ReferalCodeActivity.this, Manifest.permission.READ_CONTACTS);

//If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

//If the app doesn’t have this permission, then return false//

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // Toast.makeText(LoginActivity.this,
                    //       "Permission accepted", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(LoginActivity.this,
                    //      "Permission denied", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Uri contactData = data.getData();
            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {

                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
                    phones.moveToFirst();
                    String cNumber = phones.getString(phones.getColumnIndex("data1"));
                    System.out.println("number is:" + cNumber);
                    cNumber = cNumber.replaceAll("[-+.^:,() ]","");
                    txtPhoneNumber.setText(cNumber);
                    edtPhoneNumber.setText(cNumber);
                    phone = cNumber;

                }else {

                    f1.setVisibility(View.GONE);
                    f2.setVisibility(View.VISIBLE);
                    Toast.makeText(ReferalCodeActivity.this , "Oops! There is no phone number for this contact. Please enter the number manually." , Toast.LENGTH_LONG).show();

                }
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                edtOwnerName.setText(name);

            }
        }

    }

    public void init() {

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        btnShare = (Button) findViewById(R.id.btnShare);
        lnrDetails = (LinearLayout) findViewById(R.id.lnrDetails);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        edtShopName = (EditText) findViewById(R.id.edtShopName);
        edtState = (EditText) findViewById(R.id.edtState);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtOwnerName = (EditText) findViewById(R.id.edtOwnerName);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        f1 = (FrameLayout) findViewById(R.id.f1);
        f2 = (FrameLayout) findViewById(R.id.f2);

        imgHelp = (ImageView) findViewById(R.id.imgHelp);

        imgHelp.setVisibility(View.GONE);
        Home.setVisibility(View.VISIBLE);
        mTxtHome.setVisibility(View.VISIBLE);

    }

    public void imgAdd() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(ReferalCodeActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle(getResources().getString(R.string.Almost_Done));
        dialog.setMessage(getResources().getString(R.string.popup));
        dialog.setPositiveButton(getResources().getString(R.string.VERIFY), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                check_contact();

                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();

        /*Rect displayRectangle = new Rect();
        Window window = ReferalCodeActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater)ReferalCodeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View promptsView = inflater.inflate(R.layout.add_popup, null);
        promptsView.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        promptsView.setMinimumHeight((int)(displayRectangle.height() * 0.1f));

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ReferalCodeActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        final AlertDialog alertDialog2 = alertDialogBuilder.create();
        alertDialog2.setCanceledOnTouchOutside(true);
        alertDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog2.setCancelable(false);

        final ImageView imgCancle = (ImageView) promptsView.findViewById(R.id.imgCancle);
        Button btnVerify = (Button) promptsView.findViewById(R.id.btnVerify);

        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lnrDetails.setVisibility(View.VISIBLE);

                alertDialog2.dismiss();
            }
        });

        alertDialog2.show();
        alertDialog2.setView(promptsView,0,0,0,0);*/

    }

    private void check_contact(){

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        appUtils.showProgressBarLoading();

        String REGISTER_URL = ReferalCodeActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/check_contact";

        final String KEY_USERNAME = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if(status.equals("1"))
                        {
                            String CODE = jsonObject.optString("CODE");

                            if (CODE.equals("NOT_SHARED")){
                                isPopup = false;
                                lnrDetails.setVisibility(View.VISIBLE);
                            }else {

                                String msg ="Thanks for sharing! The more you share the more coins you can earn!";

                                AlertDialog.Builder dialog = new AlertDialog.Builder(ReferalCodeActivity.this);
                                dialog.setCancelable(false);
                                dialog.setMessage(msg);
                                dialog.setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        ShareRefferalCodeActivity.shareRefferalCode.finish();
                                        ReferalCodeActivity.this.finish();

                                        dialog.dismiss();
                                    }
                                });

                                final AlertDialog alert = dialog.create();
                                alert.show();
                            }

                        }

                        appUtils.dismissProgressBar();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();

                        //Toast.makeText(SettingsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("phone",phone);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void share_new_contact(){

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String token = prefs.getString("token" , "");

        appUtils.showProgressBarLoading();

        String REGISTER_URL = ReferalCodeActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/share_new_contact";

        final String KEY_USERNAME = "token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if(status.equals("1"))
                        {

                            String msg ="Thanks for sharing! The more you share the more coins you can earn!";

                            AlertDialog.Builder dialog = new AlertDialog.Builder(ReferalCodeActivity.this);
                            dialog.setCancelable(false);
                            dialog.setMessage(msg);
                            dialog.setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    ShareRefferalCodeActivity.shareRefferalCode.finish();
                                    ReferalCodeActivity.this.finish();

                                    dialog.dismiss();
                                }
                            });

                            final AlertDialog alert = dialog.create();
                            alert.show();

                        }

                        appUtils.dismissProgressBar();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();

                        //Toast.makeText(SettingsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("phone",phone);
                params.put("store_name",edtShopName.getText().toString());
                params.put("store_state",edtState.getText().toString());
                params.put("store_city",edtCity.getText().toString());
                params.put("store_owner_name",edtOwnerName.getText().toString());

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        if (!isPopup){
            isPopup = true ;
           lnrDetails.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }
}
