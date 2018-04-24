package com.oodi.jingoo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oodi.jingoo.R;
import com.oodi.jingoo.adapter.AvatarAdapter;
import com.oodi.jingoo.pojo.Avatar;
import com.oodi.jingoo.utility.AppUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvatarActivity extends AppCompatActivity {

    RecyclerView mRecAvatar ;
    List<Avatar> mAvatarList = new ArrayList<>();
    AvatarAdapter mAvatarAdapter ;
    ImageView mImgBack , imageView3;
    TextView mTxtHeaderName;
    String img = "" , id = "";
    Button mBtnDone , mBtnCamera ;
    public static File f ;
    private Uri mCropImageUri;
    String picturePath = "" ,p1 = "";
    private static int RESULT_LOAD_IMAGE = 2;
    AppUtils appUtils ;

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
        setContentView(R.layout.activity_avatar);

        init();

        if (appUtils.isOnLine()){
            get_avatars();
        }else {
            Intent intent = new Intent(AvatarActivity.this , NoInternetActivity.class);
            startActivity(intent);
            //appUtils.showToast(R.string.offline);
        }

        imageView3.setVisibility(View.GONE);

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mTxtHeaderName.setText("Choose Avatar");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        mAvatarAdapter = new AvatarAdapter(this , mAvatarList);

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("MESSAGE",img);
                intent.putExtra("id" , id);
                intent.putExtra("type" , "avatar");

                setResult(2,intent);
                finish();
            }
        });

        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });


    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setRequestedSize(800, 800)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //addImageToGallery(String.valueOf(result.getUri()), PostActivity.this);
                //mImgProfilePic.setImageURI(result.getUri());
                p1 = String.valueOf(result.getUri());
                f = new File(getRealPathFromURI(result.getUri()));

                Intent intent=new Intent();
                intent.putExtra("MESSAGE",p1);
                intent.putExtra("id" , "1");
                intent.putExtra("type" , "image");
                setResult(2,intent);
                finish();
                //Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);

            //mImgProfilePic.setImageURI(selectedImage);

            //Picasso.with(this).load(new File(picturePath)).into(mImgDP);
            //mImgDP.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ripple_effect));

        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("avatar")){
            }
            else {
                img = intent.getStringExtra("avatar");
                id = intent.getStringExtra("id");
            }
        }
    };

    public void init(){
        mRecAvatar = (RecyclerView) findViewById(R.id.recAvatar);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        mTxtHeaderName = (TextView) findViewById(R.id.txtHeaderName);
        mBtnDone = (Button) findViewById(R.id.btnDone);
        mBtnCamera = (Button) findViewById(R.id.btnCamera);
    }

    private void get_avatars(){

        final ProgressDialog pd = new ProgressDialog(AvatarActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = AvatarActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/get_avatars";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Json Array", "doInBackground: " + jsonObject);

                        String status = jsonObject.optString("success");

                        if(status.equals("1"))
                        {

                            JSONArray object = null;
                            try {
                                object = jsonObject.getJSONArray("avatar_data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < object.length(); i++) {

                                try {
                                    JSONObject jsonObject1 = object.getJSONObject(i);

                                    String avatar = jsonObject1.getString("link");
                                    String id = jsonObject1.getString("key");

                                    Avatar avatar1 = new Avatar();
                                    avatar1.setAvatar(avatar);
                                    avatar1.setId(id);

                                    mAvatarList.add(avatar1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }

                            RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(AvatarActivity.this, 3);
                            mRecAvatar.setLayoutManager(mLayoutManager1);
                            mRecAvatar.setAdapter(mAvatarAdapter);
                        }

                        pd.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
