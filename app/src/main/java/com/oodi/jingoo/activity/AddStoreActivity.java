package com.oodi.jingoo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Locale;
import java.util.Map;

public class AddStoreActivity extends AppCompatActivity  implements OnMapReadyCallback{

    MapView mMapView;
    private GoogleMap googleMap;
    ImageView imgBack , mImgDelete;
    TextView mTxtHome , mTxtDelete ;
    public static Activity addStoreActivity ;
    LinearLayout mLnrHome ;
    AppUtils appUtils ;
    TextView  mTxtMapLocation , mTxtState;
    EditText mEdtDistrict , mEdtDepot , mEdtLicenceNumber , mEdtLocation;
    String name  , type , token , store_id = "" , depot , dist ,license ;
    double lat , lng ;
    Geocoder geocoder;
    List<Address> addresses;
    Button mBtnDone ;
    String state = "" ;
    List<StoreList> storeLists = new ArrayList<>();
    android.app.AlertDialog alertDialog ;
    String stateName = "" ;

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
        setContentView(R.layout.activity_add_store);

        init();

        SharedPreferences prefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        token = prefs.getString("token" , "");

        try {
            Intent intent = getIntent();

            name = intent.getStringExtra("name");
            store_id = intent.getStringExtra("store");
            type = intent.getStringExtra("type");
            depot = intent.getStringExtra("depot");
            dist = intent.getStringExtra("dist");
            license = intent.getStringExtra("license");
            stateName = intent.getStringExtra("state");
            lat = Double.parseDouble(intent.getStringExtra("lat"));
            lng = Double.parseDouble(intent.getStringExtra("lng"));

        }catch (Exception e){
        }

        if (type.equals("edit")){
            mEdtLocation.setText(name);
            mEdtLicenceNumber.setText(license);
            mEdtDepot.setText(depot);
            mEdtDistrict.setText(dist);
            mTxtState.setText(stateName);
        }else if (type.equals("add")){
            lat = 20.5937 ;
            lng = 78.9629 ;

            mTxtDelete.setVisibility(View.GONE);
            mImgDelete.setVisibility(View.GONE);
        }

        addStoreActivity = this;

        mLnrHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreActivity.storeActivity.finish();
                ManageStoreActivity.manageStoreActivity.finish();
                addStoreActivity.finish();
            }
        });

        mTxtHome.setText(getResources().getString(R.string.take_stock));

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletecsm();
            }
        });

        mTxtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletecsm();
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValide()){
                    if (type.equals("edit")){
                        editCSM();
                    }else {
                        addCSM();
                    }
                }
            }
        });

        mTxtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state();
            }
        });

        statelist();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }

    public void init(){

        mMapView = (MapView) findViewById(R.id.mapView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        mTxtHome = (TextView) findViewById(R.id.txtHome);
        mTxtDelete = (TextView) findViewById(R.id.txtDelete);
        mEdtLocation = (EditText) findViewById(R.id.txtLocation);
        mEdtDistrict = (EditText) findViewById(R.id.edtDistrict);
        mEdtDepot = (EditText) findViewById(R.id.edtDepot);
        mEdtLicenceNumber = (EditText) findViewById(R.id.edtLicenceNumber);
        mLnrHome = (LinearLayout) findViewById(R.id.lnrHome);
        mImgDelete = (ImageView) findViewById(R.id.imgDelete);
        mTxtMapLocation = (TextView) findViewById(R.id.txtMapLocation);
        mBtnDone = (Button) findViewById(R.id.btnDone);
        mTxtState = (TextView) findViewById(R.id.txtState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void addCSM(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = AddStoreActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/addstore";

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
                            Toast.makeText(AddStoreActivity.this,msg,Toast.LENGTH_LONG).show();

                        }

                        if(status.equals("1"))
                        {
                            Toast.makeText(AddStoreActivity.this,msg,Toast.LENGTH_LONG).show();
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
                params.put("name",mEdtLocation.getText().toString());
                params.put("district",mEdtDistrict.getText().toString());
                params.put("depot",mEdtDepot.getText().toString());
                params.put("state",stateName);
                params.put("license_no",mEdtLicenceNumber.getText().toString());
                params.put("lat" , String.valueOf(lat));
                params.put("lng" , String.valueOf(lng));
                params.put("token" , token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void editCSM(){

        appUtils.showProgressBarLoading();

        String REGISTER_URL = AddStoreActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/editstore";

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
                            Toast.makeText(AddStoreActivity.this,msg,Toast.LENGTH_LONG).show();
                        }

                        if(status.equals("1"))
                        {
                            Toast.makeText(AddStoreActivity.this,msg,Toast.LENGTH_LONG).show();
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
                params.put("name",mEdtLocation.getText().toString());
                params.put("district",mEdtDistrict.getText().toString());
                params.put("depot",mEdtDepot.getText().toString());
                params.put("state",stateName);
                params.put("license_no",mEdtLicenceNumber.getText().toString());
                params.put("lat" , String.valueOf(lat));
                params.put("lng" , String.valueOf(lng));
                params.put("token" , token);
                params.put("store_id" , store_id);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddStoreActivity.this);
        requestQueue.add(stringRequest);
    }

    public void deletecsm(){

        final ProgressDialog pd = new ProgressDialog(AddStoreActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = AddStoreActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/deletestore";

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
                            Toast.makeText(AddStoreActivity.this,msg,Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(AddStoreActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,token);
                params.put("store_id" , store_id );

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddStoreActivity.this);
        requestQueue.add(stringRequest);
    }

    public boolean isValide() {

        if (mEdtLocation.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter name");
            return false;
        } else if (mEdtDistrict.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter district");
            return false;
        }else if (mEdtDepot.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter depot");
            return false;
        }else if (mEdtLicenceNumber.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please enter licence number");
            return false;
        }else if (mTxtState.getText().toString().equalsIgnoreCase("")) {
            makeToast("Please select state");
            return false;
        }

        return true;
    }

    public void makeToast(String name) {

        Toast.makeText(AddStoreActivity.this, name, Toast.LENGTH_SHORT).show();

    }

    private void statelist(){

        final ProgressDialog pd = new ProgressDialog(AddStoreActivity.this);
        pd.setMessage("loading");
        pd.show();

        String REGISTER_URL = AddStoreActivity.this.getResources().getString(R.string.base_url) + "v1/index.php/appshopboth/statelist";

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
                            Toast.makeText(AddStoreActivity.this,msg,Toast.LENGTH_LONG).show();
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

                                    //String id = c.getString("id");
                                    String name = c.getString("name");

                                    StoreList storeList = new StoreList();

                                    storeList.setId("1");
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
                        //Toast.makeText(AddStoreActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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

    public void state(){

        LayoutInflater layoutInflater = LayoutInflater.from(AddStoreActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.custom_version_dialogbox, null);

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddStoreActivity.this);
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

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("item_category")){
            }
            else {
                stateName = intent.getStringExtra("item_name");

                mTxtState.setText(stateName);

                try {
                    alertDialog.dismiss();
                }catch (Exception e){

                }

            }
        }
    };

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        //mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(lat, lng);
        //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition ;
        if (type.equals("edit")){
            cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        }else {
            cameraPosition = new CameraPosition.Builder().target(sydney).zoom(3).build();
        }
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.e("TAG", googleMap.getCameraPosition().target.toString());

                lat = googleMap.getCameraPosition().target.latitude ;
                lng = googleMap.getCameraPosition().target.longitude;
            }
        });

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(final CameraPosition arg0) {
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        LatLng latLng= arg0.target;

                        lat = latLng.latitude ;
                        lng = latLng.longitude;

                        try {
                            addresses = geocoder.getFromLocation(lat, lng, 1);
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();

                            Log.d("TAG", address.toString() + city + state );

                            mTxtMapLocation.setText(address+", "+city+", "+state);

                            Log.d("TAG", latLng.toString());// Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //Toast.makeText(this, latLng.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
