package dev.pardeep.healthappointment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import dev.pardeep.healthappointment.Fragments.OtpVerifyForNumber;

public class MobileVerifyActivity extends AppCompatActivity {


    EditText mobileEditText;
    Button contButton;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify);

        mobileEditText=(EditText)findViewById(R.id.mobileNumberEditText);
        contButton=(Button)findViewById(R.id.continueButton);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayoutCompleteProf);


        mobileEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return true;
            }
        });

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileEditText.getText().toString().length() == 10) {
                    sendRequestForOtp(mobileEditText.getText().toString());
                } else {
                    mobileEditText.setError("Please Enter your mobile no");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile_verify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager=(InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void sendRequestForOtp(final String mobileno) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sending OTP..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.onStart();

        HashMap<String,String> hashMap=new HashMap();
        hashMap.put("mobile",mobileno);

        final JSONObject jsonObj=new JSONObject(hashMap);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, ApiUrls.getSendOTp(), jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                System.out.println(jsonObject);
                responseFromServer(jsonObject, mobileno);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                System.out.println(volleyError);
                Toast.makeText(MobileVerifyActivity.this, "Something Wrong!Try Again", Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void responseFromServer(JSONObject jsonObject, String mobileno) {
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        sharedPref=this.getSharedPreferences(SharedPrefManager.getSaveOtpSession(), 0);
        try{
            String res=jsonObject.getString("Status");
            if (res.equalsIgnoreCase("Success")){
                editor=sharedPref.edit();
                editor.putString("mobile",mobileno);
                editor.putString("sessionkey", jsonObject.getString("Details"));
                editor.commit();
                System.out.println(mobileno);
                System.out.println(jsonObject.getString("Details"));
                System.out.println("Otp recieve");
                getFragmentManager().popBackStack();
                // startActivity(new Intent(getActivity(), OtpVerificationActivity.class));

                changeFragment(new OtpVerifyForNumber());
            }
            else {
                Toast.makeText(MobileVerifyActivity.this, "Please Try Again!", Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout,fragment).addToBackStack("fragment").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(MobileVerifyActivity.this, "Please Verify Your Mobile Number", Toast.LENGTH_SHORT).show();
    }
}
