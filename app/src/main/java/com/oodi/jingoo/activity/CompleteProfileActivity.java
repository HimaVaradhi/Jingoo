package com.oodi.jingoo.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.oodi.jingoo.utility.AppUtils;
import com.oodi.jingoo.utility.MultipartUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {

    Button mBtnDone ;
    TextView mTxtAvatar , mTxtTaC ;
    TextView mTxtHeaderName ;
    ImageView mImgDp , mImgWt;
    EditText mEdtName , mEdtPhoneNumber , mEdtPassword ;
    AppUtils appUtils ;
    String token = "", name = "", phone = ""  , avatar , _avatar = "";
    String avatar_id = "" , type = "" ;
    RadioButton mCbEnglish , mCbHindi ;
    CheckBox mCbTaC ;
    String status = "";

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
        setContentView(R.layout.activity_complete_profile);

        init();

        //=====================================================================================================================

        if (lang.equals("hi")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                 mImgWt.setBackground(getResources().getDrawable(R.drawable.hi_complete_profile));
            }
        }

        SharedPreferences wt = getSharedPreferences("wt", MODE_PRIVATE);
        String first = wt.getString("complete_profile", "");

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
                editor.putString("complete_profile", "1");

                editor.commit();

            }
        });

        //=====================================================================================================================


        String s = "I AGREE TO THE <u><b>TERMS &amp; CONDITION</b></u>";
        mTxtTaC.setText(Html.fromHtml(s));

        mTxtTaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://oodiproject.com/jingoo/terms-and-conditions/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        SharedPreferences prefs = getSharedPreferences("language", MODE_PRIVATE);
        String lang1 = prefs.getString("lang", "");
        if (lang1.equals("hi")){
            mCbHindi.setChecked(true);
        }else {
            mCbEnglish.setChecked(true);
        }

        final SharedPreferences.Editor editor = getSharedPreferences("language", MODE_PRIVATE).edit();

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this).setCancelable(false);

                        builder.setMessage("क्या आप वाकई हिंअंग्रेज़ी में स्विच करना चाहते हैं? ऐप की सभी सामग्री अंग्रेज़ी में बदल जाएगी").setPositiveButton("हाँ", dialogClickListener)
                                .setNegativeButton("नहीं", dialogClickListener).show();

                    }
                    /*if (isChecked){
                        editor.putString("lang", "en");
                        editor.commit();
                        finish();
                        startActivity(getIntent());
                    }*/
                }
            }
        });

        mCbHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    /*if (isChecked){
                        editor.putString("lang", "hi");
                        editor.commit();
                        finish();
                        startActivity(getIntent());
                    }*/
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteProfileActivity.this).setCancelable(false);
                        builder.setMessage("Are you sure you want to switch to Hindi? All content on the app will change to Hindi").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                }
            }
        });

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        avatar = intent.getStringExtra("avatar");
        avatar_id = intent.getStringExtra("avatar_id");

        Picasso.with(CompleteProfileActivity.this)
                .load(avatar)
                .fit()
                .placeholder(R.drawable.j1)
                .into(mImgDp);

        mEdtName.setText(name);
        mEdtPhoneNumber.setText(phone);

        appUtils = new AppUtils(this);

        mTxtHeaderName.setText("Profile");

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appUtils.isOnLine()){
                    if (isValide()){
                        new EditProfile().execute(mEdtName.getText().toString() , mEdtPhoneNumber.getText().toString() ,mEdtPassword.getText().toString()
                                ,avatar_id , token );
                    }
                }else {
                    Intent intent = new Intent(CompleteProfileActivity.this , NoInternetActivity.class);
                    startActivity(intent);
                    //appUtils.showToast(R.string.offline);
                }

            }
        });

        mTxtAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompleteProfileActivity.this , AvatarActivity.class);
                startActivityForResult(intent , 2);
            }
        });

        mImgDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompleteProfileActivity.this , AvatarActivity.class);
                startActivityForResult(intent , 2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            try {
                String message = data.getStringExtra("MESSAGE");
                avatar_id = data.getStringExtra("id");
                String type = data.getStringExtra("type");
                if (type.equals("avatar")) {
                    Picasso.with(CompleteProfileActivity.this)
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

        mBtnDone = (Button) findViewById(R.id.btnDone);
        mTxtAvatar = (TextView) findViewById(R.id.txtAvatar);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mImgDp = (ImageView) findViewById(R.id.imgDp);
        mEdtName = (EditText) findViewById(R.id.edtName);
        mEdtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        mEdtPassword = (EditText) findViewById(R.id.edtPassword);
        mCbEnglish = (RadioButton) findViewById(R.id.cbEnglish);
        mCbHindi = (RadioButton) findViewById(R.id.cbHindi);
        mTxtTaC = (TextView) findViewById(R.id.txtTaC);
        mCbTaC = (CheckBox) findViewById(R.id.cbTaC);
        mImgWt = (ImageView) findViewById(R.id.imgWt);


    }

    private void update_profile(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = CompleteProfileActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/update_profile";

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
                            Toast.makeText(CompleteProfileActivity.this,msg,Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(CompleteProfileActivity.this , IntroAppActivity.class);
                        startActivity(intent);
                        finish();

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
                params.put("avatar" , avatar_id);
                params.put("token" , token);
                params.put("email" , "");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isValide() {

        if (mEdtName.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter username");
            return false;
        }else if (mEdtPhoneNumber.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter phone number");
            return false;
        }else if (!mCbTaC.isChecked()) {
            makeToast("Please check terms and conditions");
            return false;
        }

        return true;
    }

    public void makeToast(String name) {

        Toast.makeText(CompleteProfileActivity.this, name, Toast.LENGTH_SHORT).show();

    }

    public class EditProfile extends AsyncTask<String , Void , String> {

        ProgressDialog progressDialog ;
        String msg = "" ;


        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(CompleteProfileActivity.this, "Loading. . .","Please Wait. . .", true);
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

                String charset = "UTF-8";
                String requestURL = CompleteProfileActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/update_profile/";

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);

                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");

                multipart.addFormField("name", name.trim());
                multipart.addFormField("phone", phone.trim());
                multipart.addFormField("password", password.trim());
                multipart.addFormField("avatar", avatar.trim());
                multipart.addFormField("token", token.trim());
                if (_avatar.equals("1")){
                    multipart.addFilePart("avatar_img", AvatarActivity.f);
                }

                List<String> response = multipart.finish();
                JSONObject jsonObject = new JSONObject(response.get(0));

                Log.d("Json Array", "doInBackground: " + jsonObject);

                status = jsonObject.optString("success");
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

                    SharedPreferences.Editor editor1 = getSharedPreferences("wt", MODE_PRIVATE).edit();

                    editor1.putString("first" , "1");

                    editor1.commit();

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

            SharedPreferences login = getSharedPreferences("Login", MODE_PRIVATE);
            String _type = login.getString("type", "");

            if (status.equals("1") && !_type.equals("csm")){
                Intent intent = new Intent(CompleteProfileActivity.this , IntroAppActivity.class);

//                Intent intent = new Intent(CompleteProfileActivity.this , ChooseSupliersActivity.class);
                intent.putExtra("type" , "new");
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(CompleteProfileActivity.this , IntroAppActivity.class);
                startActivity(intent);
                finish();
            }

            Toast.makeText(CompleteProfileActivity.this, aVoid, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

}
