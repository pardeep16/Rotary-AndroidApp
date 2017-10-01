package dev.pardeep.healthappointment.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.pardeep.healthappointment.ApiUrls;
import dev.pardeep.healthappointment.R;
import dev.pardeep.healthappointment.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteProfileGmail extends Fragment {

    View view;

    EditText mobileEditText;
    Button contButton;

    LinearLayout linearLayout;


    public CompleteProfileGmail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_complete_profile_gmail, container, false);

        mobileEditText=(EditText)view.findViewById(R.id.mobileNumberEditText);
        contButton=(Button)view.findViewById(R.id.continueButton);
        linearLayout=(LinearLayout)view.findViewById(R.id.linearLayoutCompleteProf);


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

       /* getView().setFocusableInTouchMode(true);
        ;*/
       /* getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction()==KeyEvent.KEYCODE_BACK){
                    Toast.makeText(getActivity(), "Enter your mobile number", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });*/
        /*getFragmentManager().findFragmentByTag("fragmentphone").getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.KEYCODE_BACK){
                    Toast.makeText(getActivity(), "Enter your mobile number", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });*/





        return view;
    }

    private boolean validateEmail(String email) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
                Toast.makeText(getActivity(), "Please Try Again!", Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.relativelayout,fragment).addToBackStack("fragment").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }




}
