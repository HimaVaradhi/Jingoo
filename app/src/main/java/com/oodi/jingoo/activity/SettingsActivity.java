package com.oodi.jingoo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.oodi.jingoo.utility.MultipartUtility;
import com.oodi.jingoo.utility.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    ImageView mImgBack , mImgDp , mImgEditSellingPrise , mImgEditSupplier , mImgEditTransferStock , mImgShareRefferalCode;
    Button mBtnSignOut , mBtnUpdate ;
    Session session ;
    AppUtils appUtils ;
    RadioButton mCbEnglish , mCbHindi ;
    SwitchCompat mSwitchPushNotification ;
    String token , img , id = "" , _avatar = "", name , email , username , phone , is_active;
    TextView mTxtAvatar , mTxtHeaderName , mTxtHome , home , mTxtPhone , mTxtTaC , mTxtVersion , mtxtSupplier , mtxtSelling , mtxtTransfer;
    EditText mEdtName , mEdtEmail , mEdtPassword , mEdtPhoneNumber ;
    static final Integer CALL = 0x2;
    public static boolean isTrue = false ;
    LinearLayout mlnrSelling , mLnrSupplier , mLnrTransferStock;
    public static Activity setting ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else{
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_settings);

        setting = this;

        init();

        getVersionInfo();

        //get_my_profile();

        mTxtHeaderName.setText(getResources().getString(R.string.profile_settings));
        mTxtHome.setText(getResources().getString(R.string.profile_settings));

        SharedPreferences pr = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = pr.getString("token" , "");
        img = pr.getString("avatar" , "");
        name = pr.getString("name" , "");
        email = pr.getString("email" , "");
        username = pr.getString("username" , "");
        phone = pr.getString("phone" , "");

        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            allow_push_notification();
        }

        mTxtAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this , AvatarActivity.class);
                startActivityForResult(intent , 2);
            }
        });

        mImgDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this , AvatarActivity.class);
                startActivityForResult(intent , 2);
            }
        });

        mEdtEmail.setText(email);
        mEdtName.setText(name);
        mEdtPhoneNumber.setText(phone);

        Picasso.with(this)
                .load(img)
                .fit()
                .placeholder(R.drawable.j1)
                .into(mImgDp);

        SharedPreferences prefs = getSharedPreferences("language", MODE_PRIVATE);
        String lang1 = prefs.getString("lang", "");
        if (lang1.equals("hi")){
            mCbHindi.setChecked(true);
        }else {
            mCbEnglish.setChecked(true);
        }

        mTxtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission(android.Manifest.permission.CALL_PHONE,CALL);
            }
        });

        mSwitchPushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()){
                    if (b){
                        is_active = "1";
                        change_customer_status();
                    }else {
                        is_active = "0";
                        change_customer_status();
                    }
                }
            }
        });

        final SharedPreferences.Editor editor = getSharedPreferences("language", MODE_PRIVATE).edit();

        session = new Session(this);

        mCbEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        editor.putString("lang", "en");
                                        editor.commit();

                                        finish();
                                        startActivity(getIntent());
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        mCbEnglish.setChecked(false);
                                        mCbHindi.setChecked(true);

                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this).setCancelable(false);

                        builder.setMessage("क्या आप वाकई अंग्रेज़ी में स्विच करना चाहते हैं? ऐप की सभी सामग्री अंग्रेज़ी में बदल जाएगी").setPositiveButton("हाँ", dialogClickListener)
                                .setNegativeButton("नहीं", dialogClickListener).show();

                    }
                }
            }
        });

        mCbHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isPressed()){
                    if (isChecked){
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        editor.putString("lang", "hi");
                                        editor.commit();

                                        finish();
                                        startActivity(getIntent());
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        mCbHindi.setChecked(false);
                                        mCbEnglish.setChecked(true);

                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this).setCancelable(false);
                        builder.setMessage("Are you sure you want to switch to Hindi? All content on the app will change to Hindi").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                }
            }
        });

        mBtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);

                SharedPreferences s = SettingsActivity.this.getSharedPreferences("Login" , Context.MODE_PRIVATE);
                s.edit().clear().commit();

                SharedPreferences s1 = SettingsActivity.this.getSharedPreferences("language", Context.MODE_PRIVATE);
                s1.edit().clear().commit();

                Intent intent = new Intent(SettingsActivity.this , LoginActivity.class);
                startActivity(intent);

                MainActivity.mainActivity.finish();
                SettingsActivity.this.finish();
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsNewActivity.settingNew.finish();
                onBackPressed();
            }
        });

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update_profile();

                new EditProfile().execute(mEdtName.getText().toString() , mEdtPhoneNumber.getText().toString() ,mEdtPassword.getText().toString()
                ,id , token , mEdtEmail.getText().toString());
            }
        });

        mTxtTaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://oodiproject.com/jingoo/terms-and-conditions/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mImgEditSellingPrise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this , SellingPriseActivity.class);
                startActivity(intent);
            }
        });

        mImgEditSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this , StoreActivity.class);
                intent.putExtra("type" , "setting");
                startActivity(intent);
            }
        });

        mLnrTransferStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this , TransferStockActivity.class);
                startActivity(intent);
            }
        });

        mImgShareRefferalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this , ShareRefferalCodeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + mTxtPhone.getText().toString()));
            if (ActivityCompat.checkSelfPermission(SettingsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            }


            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SettingsActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +mTxtPhone.getText().toString()));
            startActivity(in);
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2){
            try {
                String message = data.getStringExtra("MESSAGE");
                id = data.getStringExtra("id");
                String type = data.getStringExtra("type");
                if (type.equals("avatar")) {
                    Picasso.with(SettingsActivity.this)
                            .load(message)
                            .fit()
                            .into(mImgDp);
                } else {
                    _avatar = data.getStringExtra("id");

                    mImgDp.setImageURI(Uri.parse(message));
                }


            } catch (Exception e) {

            }

        }
    }

    public void init(){

        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mBtnSignOut = (Button) findViewById(R.id.btnSignOut);
        mCbEnglish = (RadioButton) findViewById(R.id.cbEnglish);
        mCbHindi = (RadioButton) findViewById(R.id.cbHindi);
        mSwitchPushNotification = (SwitchCompat) findViewById(R.id.switchPushNotification);
        mImgDp = (ImageView) findViewById(R.id.imgDp);
        mTxtAvatar = (TextView) findViewById(R.id.txtEditAvatar);
        mEdtName = (EditText) findViewById(R.id.edtName);
        mEdtEmail = (EditText) findViewById(R.id.edtEmail);
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        home = (TextView) findViewById(R.id.home);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mTxtPhone = (TextView) findViewById(R.id.txtPhone);
        mTxtTaC = (TextView) findViewById(R.id.txtTaC);
        mTxtVersion = (TextView) findViewById(R.id.txtVersion);
        mImgEditSellingPrise = (ImageView) findViewById(R.id.imgEditSellingPrise);
        mImgEditSupplier = (ImageView) findViewById(R.id.imgEditSupplier);
        mLnrSupplier = (LinearLayout) findViewById(R.id.lnrSupplier);
        mlnrSelling = (LinearLayout) findViewById(R.id.lnrSelling);
        mtxtSupplier = (TextView) findViewById(R.id.txtSupplier);
        mtxtSelling = (TextView) findViewById(R.id.txtSelling);
        mtxtTransfer = (TextView) findViewById(R.id.txtTransfer);
        mLnrTransferStock = (LinearLayout) findViewById(R.id.lnrTransferStock);
        mImgEditTransferStock = (ImageView) findViewById(R.id.imgEditTransferStock);
        mImgShareRefferalCode = (ImageView) findViewById(R.id.imgShareRefferalCode);

    }

    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mTxtVersion.setText(String.format("Version %s ", versionName));
    }

    private void allow_push_notification(){

        String REGISTER_URL = SettingsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/allow_push_notification";

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

                            try {
                                JSONObject j = jsonObject.getJSONObject("data");

                                if (j.getString("allow_push").equals("1")){
                                    mSwitchPushNotification.setChecked(true);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SettingsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void change_customer_status(){

        String REGISTER_URL = SettingsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/push_notification_update";

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
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(SettingsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("allow_push",is_active);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void update_profile(){

        appUtils.showProgressBarLoading();
        /*final ProgressDialog pd = new ProgressDialog(CompleteProfileActivity.this);
        pd.setMessage("loading");
        pd.show();*/

        String REGISTER_URL = SettingsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/update_profile";

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

                        String msg = jsonObject.optString("msg");
                        String name = jsonObject.optString("name");
                        String phone = jsonObject.optString("phone");
                        String avatar = jsonObject.optString("avatar");
                        String code = jsonObject.optString("code");
                        String email = jsonObject.optString("email");

                        SharedPreferences.Editor editor = getSharedPreferences("Login", MODE_PRIVATE).edit();
                        editor.putString("name", name);
                        editor.putString("phone", phone);
                        editor.putString("avatar", avatar);
                        editor.putString("email", email);

                        editor.commit();

                        SharedPreferences.Editor editor1 = getSharedPreferences("wt", MODE_PRIVATE).edit();

                        editor1.putString("first" , "1");

                        editor1.commit();

                        if (!msg.equals("")){
                            Toast.makeText(SettingsActivity.this,msg,Toast.LENGTH_LONG).show();
                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CompleteProfileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        appUtils.dismissProgressBar();


                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",mEdtName.getText().toString());
                params.put("phone",mEdtPhoneNumber.getText().toString());
                params.put("password" , mEdtPassword.getText().toString());
                params.put("avatar" , id);
                params.put("token" , token);
                params.put("email" , mEdtEmail.getText().toString());


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class EditProfile extends AsyncTask<String , Void , String> {

        ProgressDialog progressDialog ;
        String msg = "" ;

        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(SettingsActivity.this, "Loading. . .","Please Wait. . .", true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String name = params[0];
                String phone = params[1];
                String password = params[2];
                String avatar = params[3];
                String token = params[4];
                String email = params[5];

                String charset = "UTF-8";
                String requestURL = SettingsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/update_profile/";

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");

                multipart.addFormField("name", name.trim());
                multipart.addFormField("phone", phone.trim());
                multipart.addFormField("password", password.trim());
                multipart.addFormField("avatar", avatar.trim());
                multipart.addFormField("token", token.trim());
                multipart.addFormField("email", email.trim());
                if (_avatar.equals("1")){
                    multipart.addFilePart("avatar_img", AvatarActivity.f);
                }

                List<String> response = multipart.finish();
                JSONObject jsonObject = new JSONObject(response.get(0));

                Log.d("Json Array", "doInBackground: " + jsonObject);

                String status = jsonObject.optString("success");
                msg = jsonObject.optString("msg");

                if(status.equals("1"))
                {

                    _avatar = "";

                    String msg_ = jsonObject.optString("msg");
                    String name_ = jsonObject.optString("name");
                    String phone_ = jsonObject.optString("phone");
                    String avatar_ = jsonObject.optString("avatar");
                    String code_ = jsonObject.optString("code");
                    String email_ = jsonObject.optString("email");

                    SharedPreferences.Editor editor = getSharedPreferences("Login", MODE_PRIVATE).edit();
                    editor.putString("name", name_);
                    editor.putString("phone", phone_);
                    editor.putString("avatar", avatar_);
                    editor.putString("email", email_);

                    editor.commit();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                msg = "JSONException : "+e.getMessage();
                Log.e("JSONException : " , ""+e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                msg = "IOException : "+e.getMessage();
                Log.e("IOException : " , ""+e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                msg = "Exception : "+e.getMessage();
                Log.e("Exception : " , ""+e.getMessage());
            }

            return msg;
        }
        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(SettingsActivity.this, aVoid, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    private void get_my_profile(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = SettingsActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_my_profile";

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

                            try {
                                JSONObject object =jsonObject.getJSONObject("data");

                                String role_selling_price = object.getString("role_selling_price");
                                String role_transfer_stock = object.getString("role_transfer_stock");
                                String type = object.getString("type");

                                if (type.equals("csm")){

                                    mLnrSupplier.setVisibility(View.GONE);
                                    mtxtSupplier.setVisibility(View.GONE);

                                    if (role_selling_price.equals("1")){
                                        mlnrSelling.setVisibility(View.VISIBLE);
                                        mtxtSelling.setVisibility(View.VISIBLE);
                                    }else {
                                        mlnrSelling.setVisibility(View.GONE);
                                        mtxtSelling.setVisibility(View.GONE);

                                    }

                                    if (role_transfer_stock.equals("1")){
                                        mLnrTransferStock.setVisibility(View.VISIBLE);
                                        mtxtTransfer.setVisibility(View.VISIBLE);
                                    }else {
                                        mLnrTransferStock.setVisibility(View.GONE);
                                        mtxtTransfer.setVisibility(View.GONE);

                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
