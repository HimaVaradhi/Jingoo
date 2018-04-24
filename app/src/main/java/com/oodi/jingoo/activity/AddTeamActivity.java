package com.oodi.jingoo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.oodi.jingoo.adapter.StoreListAdapter;
import com.oodi.jingoo.pojo.StoreList;
import com.oodi.jingoo.utility.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTeamActivity extends AppCompatActivity {

    TextView mTxtHeaderName , mTxtHome , mTxtDelete , Home , mTxtLocation , mTxtDoItLater;
    EditText mEdtSetPassword , mEdtName , mEdtEmail , mEdtPhoneNumber;
    ImageView mImgDelete , mImgBack , mImgWt , imgShare;
    public static Activity addTeam ;
    List<StoreList> storeLists = new ArrayList<>();
    String token , store_id = "", item_name , phone = "";
    android.app.AlertDialog alertDialog ;
    String csm_id = "" , type = "";
    AppUtils appUtils ;
    Button mBtnDone ;
    String id= "" , role_transfer_stock = "1", role_selling_price = "1" , role_reports = "1";
    RadioButton r1 , r2 , r3 , r4 , r5 , r6;
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(this);
        SharedPreferences prefs1 = getSharedPreferences("language", MODE_PRIVATE);
        String lang = prefs1.getString("lang", "");
        if (lang.equals("hi")){
            appUtils.setLocale("hi");
        }else {
            appUtils.setLocale("en");
        }
        setContentView(R.layout.activity_add_team);

        init();

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermission()) {

                Log.e("permission", "Permission already granted.");
            } else {

                requestPermission();
            }
        }

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                try {
                    ManageTeamActivity.manageTeam.finish();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView imgHelp = (ImageView) findViewById(R.id.imgHelp);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnDone.setVisibility(View.INVISIBLE);
                mImgWt.setVisibility(View.VISIBLE);

            }
        });

        //=====================================================================================================================

        if (lang.equals("hi")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                 mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_create_team));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("add_team", "");

        if (first.equals("1")){

            mImgWt.setVisibility(View.GONE);
            mBtnDone.setVisibility(View.VISIBLE);

        }

        mImgWt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mImgWt.setVisibility(View.GONE);
                mBtnDone.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = getSharedPreferences("wt", MODE_PRIVATE).edit();
                editor.putString("add_team", "1");

                editor.commit();

            }
        });

        //=====================================================================================================================


        try{
            Intent intent = getIntent();
            type = intent.getStringExtra("type");
            if (type.equals("new")){

                //ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);

                mTxtHome.setVisibility(View.GONE);
                mImgBack.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);

                mTxtDoItLater.setVisibility(View.VISIBLE);

            }
        }catch (Exception e){
            type = "" ;
        }

        if (!appUtils.isOnLine()){
            Intent intent = new Intent(getApplicationContext() , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }else {
            getStoreList();
        }

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        if (type.equals("new")){
            mTxtHeaderName.setText(getResources().getString(R.string.Create_Your_Team));
            mTxtHome.setText(getResources().getString(R.string.Create_Your_Team));
        }else {
            mTxtHome.setText(getResources().getString(R.string.Create_Your_Team_));
        }
        addTeam = this;

        mTxtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeList();
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageTeamActivity.manageTeam.finish();
                addTeam.finish();
            }
        });

        mEdtSetPassword.setTypeface(Typeface.DEFAULT);

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try{
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String store_name = intent.getStringExtra("store_name");
            csm_id = intent.getStringExtra("csm_id");
            phone = intent.getStringExtra("phone");
            store_id = intent.getStringExtra("store_id");
            role_selling_price = intent.getStringExtra("selling");
            role_transfer_stock = intent.getStringExtra("transfer");
            role_reports = intent.getStringExtra("role_reports");

            if (id.equals("add")){
                mTxtDelete.setVisibility(View.GONE);
                mImgDelete.setVisibility(View.GONE);
                if (!type.equals("new")){
                    mTxtHeaderName.setText(getResources().getString(R.string.create_new));
                }
                store_id = "";
                role_selling_price = "0";
                role_transfer_stock = "0";
                role_reports = "0";
            }else {
                mEdtEmail.setText(email);
                mEdtName.setText(name);
                mTxtLocation.setText(store_name);
                mEdtPhoneNumber.setText(phone);

                if (role_transfer_stock.equals("1")){
                    r3.setChecked(true);
                    r4.setChecked(false);
                }else{
                    r3.setChecked(false);
                    r4.setChecked(true);
                }

                if (role_selling_price.equals("1")){
                    r1.setChecked(true);
                    r2.setChecked(false);
                }else{
                    r1.setChecked(false);
                    r2.setChecked(true);
                }

                if (role_reports.equals("1")){
                    r5.setChecked(true);
                    r6.setChecked(false);
                }else{
                    r5.setChecked(false);
                    r6.setChecked(true);
                }

                mTxtHeaderName.setText(getResources().getString(R.string.Edit_Team));
                mEdtSetPassword.setHint(getResources().getString(R.string.change_password));
            }

        }catch (Exception e){

        }

        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        role_selling_price = "1";
                    }
                }
            }
        });

        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        role_selling_price = "0";
                    }
                }
            }
        });

        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        role_transfer_stock = "1";
                    }
                }
            }
        });

        r4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        role_transfer_stock = "0";

                    }
                }
            }
        });

        r5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        role_reports = "1";
                    }
                }
            }
        });

        r6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    if (isChecked){
                        role_reports = "0";
                    }
                }
            }
        });

        mTxtDoItLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTeamActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddTeamActivity.this);
                builder.setMessage(AddTeamActivity.this.getResources().getString(R.string.delete))
                        .setCancelable(false)
                        .setNegativeButton("Cancel" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                                deletecsm();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        mTxtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddTeamActivity.this);
                builder.setMessage("Are you sure want to delete?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                                deletecsm();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals("add")){
                    if (isValide()){
                        addCSM();
                    }
                }else {
                    if (isValide2()) {
                        editCSM();
                    }
                }
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }

    private boolean checkPermission() {

//Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(AddTeamActivity.this, Manifest.permission.READ_CONTACTS);

//If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

//If the app doesn’t have this permission, then return false//

            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);

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
                    mEdtPhoneNumber.setText(cNumber);
                    phone = cNumber;

                }else {

                    Toast.makeText(AddTeamActivity.this , "Oops! There is no phone number for this contact. Please enter the number manually." , Toast.LENGTH_LONG).show();

                }
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mEdtName.setText(name);

            }
        }

    }

    public void init(){

        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mEdtSetPassword = (EditText) findViewById(R.id.edtSetPassword);
        mImgDelete = (ImageView) findViewById(R.id.imgDelete);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        Home = (TextView) findViewById(R.id.Home);
        mTxtDelete = (TextView) findViewById(R.id.txtDelete);
        mEdtName = (EditText) findViewById(R.id.edtName);
        mTxtLocation = (TextView) findViewById(R.id.txtLocation);
        mEdtEmail = (EditText) findViewById(R.id.edtEmail);
        mBtnDone = (Button) findViewById(R.id.btnDone);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        r1 = (RadioButton) findViewById(R.id.r1);
        r2 = (RadioButton) findViewById(R.id.r2);
        r3 = (RadioButton) findViewById(R.id.r3);
        r4 = (RadioButton) findViewById(R.id.r4);
        r5 = (RadioButton) findViewById(R.id.r5);
        r6 = (RadioButton) findViewById(R.id.r6);
        mTxtDoItLater = (TextView) findViewById(R.id.txtDoItLater);
        mImgWt = (ImageView) findViewById(R.id.imgWt);
        imgShare = (ImageView) findViewById(R.id.imgShare);

    }

    private void getStoreList(){

        final ProgressDialog pd = new ProgressDialog(AddTeamActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = AddTeamActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_store_list";

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
                        String msg = jsonObject.optString("msg");

                        if (!msg.equals("")){
                            Toast.makeText(AddTeamActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
                        if(status.equals("1"))
                        {

                            JSONArray array = null;
                            try {
                                array = jsonObject.getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject c = null;
                                try {
                                    c = array.getJSONObject(i);

                                    String id = c.getString("id");
                                    String name = c.getString("name");

                                    StoreList storeList = new StoreList();

                                    storeList.setId(id);
                                    storeList.setName(name);

                                    storeLists.add(storeList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(AddTeamActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

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

    public void storeList(){

        LayoutInflater layoutInflater = LayoutInflater.from(AddTeamActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.custom_version_dialogbox, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddTeamActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        final TextView Update = (TextView) promptsView.findViewById(R.id.btn_confirm);

        final RecyclerView recyclerView = (RecyclerView) promptsView.findViewById(R.id.list);

        StoreListAdapter adapter = new StoreListAdapter(this,  storeLists);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void editCSM(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = AddTeamActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/editcsm";

        final String KEY_USERNAME = "username";

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
                        String msg = jsonObject.optString("msg");

                        if (status.equals("0")){
                            Toast.makeText(AddTeamActivity.this,msg,Toast.LENGTH_LONG).show();
                        }

                        if(status.equals("1"))
                        {
                            Toast.makeText(AddTeamActivity.this,msg,Toast.LENGTH_LONG).show();
                            finish();
                        }

                        appUtils.dismissProgressBar();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,mEdtName.getText().toString());
                params.put("phone",mEdtPhoneNumber.getText().toString());
                params.put("password",mEdtSetPassword.getText().toString());
                params.put("email",mEdtEmail.getText().toString());
                params.put("store_id" , store_id);
                params.put("token" , token);
                params.put("csm_id" , csm_id);
                params.put("role_selling_price" , role_selling_price);
                params.put("role_transfer_stock" , role_transfer_stock);
                params.put("role_reports" , role_reports);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddTeamActivity.this);
        requestQueue.add(stringRequest);
    }

    public void deletecsm(){

        final ProgressDialog pd = new ProgressDialog(AddTeamActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = AddTeamActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/deletecsm";

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
                        String msg = jsonObject.optString("msg");

                        if (!msg.equals("")){
                            Toast.makeText(AddTeamActivity.this,msg,Toast.LENGTH_LONG).show();
                        }
                        if(status.equals("1"))
                        {
                            pd.dismiss();
                            finish();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(AddTeamActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("csm_id" , csm_id );

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddTeamActivity.this);
        requestQueue.add(stringRequest);
    }

    private void addCSM(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = AddTeamActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/addcsm";

        final String KEY_USERNAME = "username";

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
                        String msg = jsonObject.optString("msg");

                        if (status.equals("0")){
                            Toast.makeText(AddTeamActivity.this,msg,Toast.LENGTH_LONG).show();
                        }

                        if(status.equals("1"))
                        {
                            Toast.makeText(AddTeamActivity.this,msg,Toast.LENGTH_LONG).show();
                            if (type.equals("new")){

                                final AlertDialog.Builder builder = new AlertDialog.Builder(AddTeamActivity.this);
                                builder.setMessage(getResources().getString(R.string.add_team))
                                        .setCancelable(false)
                                        .setNegativeButton(getResources().getString(R.string.no) , new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();

                                                Intent intent = new Intent(AddTeamActivity.this , MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                mEdtName.setText("");
                                                mEdtPhoneNumber.setText("");
                                                mEdtEmail.setText("");
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }else {
                                finish();
                            }

                        }

                        appUtils.dismissProgressBar();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appUtils.dismissProgressBar();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,mEdtName.getText().toString());
                params.put("phone",mEdtPhoneNumber.getText().toString());
                params.put("password",mEdtSetPassword.getText().toString());
                params.put("email",mEdtEmail.getText().toString());
                params.put("store_id" , store_id);
                params.put("token" , token);
                params.put("role_selling_price" , role_selling_price);
                params.put("role_transfer_stock" , role_transfer_stock);
                params.put("role_reports" , role_reports);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isValide() {

        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (mEdtName.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter name");
            return false;
        } else if (mEdtPhoneNumber.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter phone number");
            return false;
        }/*else if (mEdtSetPassword.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please set password");
            return false;
        }*/else if (store_id.equals("")) {
            makeToast("Please select store");
            return false;
        }/*else if (mEdtEmail.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter email address");
            return false;
        }else if (!mEdtEmail.getText().toString().matches(emailPattern)) {
            makeToast("Please enter valid email address");

        }*/

        return true;
    }

    public boolean isValide2() {

        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (mEdtName.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter name");
            return false;
        } else if (mEdtPhoneNumber.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter phone number");
            return false;
        }else if (store_id.equals("")) {
            makeToast("Please select store");
            return false;
        }/*else if (mEdtEmail.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter email address");
            return false;
        }else if (!mEdtEmail.getText().toString().matches(emailPattern)) {
            makeToast("Please enter valid email address");

        }*/

        return true;
    }

    public void makeToast(String name) {

        Toast.makeText(AddTeamActivity.this, name, Toast.LENGTH_SHORT).show();

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("from")){
            }
            else {
                store_id = intent.getStringExtra("from");
                item_name = intent.getStringExtra("item_name");

                mTxtLocation.setText(item_name);

                try {
                    alertDialog.dismiss();
                }catch (Exception e){
                }
            }
        }
    };
}
