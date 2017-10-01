package dev.pardeep.healthappointment.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import dev.pardeep.healthappointment.ApiUrls;
import dev.pardeep.healthappointment.OtpVerificationActivity;
import dev.pardeep.healthappointment.R;
import dev.pardeep.healthappointment.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MobileNoSignIn extends Fragment {

    View view;

    EditText editTextMobile;
    Button buttonSignIn;
    LinearLayout linearLayout;

    public MobileNoSignIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_mobile_no_sign_in, container, false);

        editTextMobile=(EditText)view.findViewById(R.id.edittextmobile);
        buttonSignIn=(Button)view.findViewById(R.id.buttonmobilesignin);

        linearLayout=(LinearLayout)view.findViewById(R.id.linearlayoutmobile);


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextMobile.getText().length()==10){
                    sendRequestForOtp(editTextMobile.getText().toString().trim());
                }
                else {
                    Toast.makeText(getActivity(), "Enter 10 digit number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editTextMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        return view;
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void sendRequestForOtp(final String mobileno) {

        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
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
                Toast.makeText(getActivity(), "Something Wrong!Try Again", Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void responseFromServer(JSONObject jsonObject, String mobileno) {
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        sharedPref=getActivity().getSharedPreferences(SharedPrefManager.getSaveOtpSession(),0);
        try{
            String res=jsonObject.getString("Status");
            if (res.equalsIgnoreCase("Success")){
                editor=sharedPref.edit();
                editor.putString("mobile",mobileno);
                editor.putString("sessionkey",jsonObject.getString("Details"));
                editor.commit();
                System.out.println(mobileno);
                System.out.println(jsonObject.getString("Details"));
                System.out.println("Otp recieve");
                startActivity(new Intent(getActivity(), OtpVerificationActivity.class));
            }
            else {
                Toast.makeText(getActivity(), "Plaese Try Again!", Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }


}
