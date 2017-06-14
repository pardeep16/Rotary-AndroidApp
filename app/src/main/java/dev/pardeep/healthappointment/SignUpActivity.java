package dev.pardeep.healthappointment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

  /*  TextView alreadySignIn;
    boolean flag=false;
    boolean flagPhone;

    EditText userNameEdit,userEmailEdit,userPassword,userConfirmPassword,userMobile;

    CheckBox checkBoxTerm;
    Button signUpButton;
*/
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    private static String temp_Signup="profile";

    public static String getTemp_Signup() {
        return temp_Signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       /* alreadySignIn=(TextView)findViewById(R.id.textSignIn);
        alreadySignIn.setOnClickListener(this);

        userNameEdit=(EditText)findViewById(R.id.username);
        userEmailEdit=(EditText)findViewById(R.id.emailid);
        userPassword=(EditText)findViewById(R.id.password);
        userConfirmPassword=(EditText)findViewById(R.id.passwordCoonfirm);
        userMobile=(EditText)findViewById(R.id.mobilenumber);

        checkBoxTerm=(CheckBox)findViewById(R.id.checkBoxTerms);
        signUpButton=(Button)findViewById(R.id.buttonSignUp);

        signUpButton.setOnClickListener(this);*/

       /* userEmailEdit.setFocusableInTouchMode(true);
        userEmailEdit.requestFocus();

        userMobile.setFocusableInTouchMode(true);
        userMobile.requestFocus();*/

        /*userEmailEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getAction() == KeyEvent.KEYCODE_ENTER)) {
                    emailVerify(userEmailEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });*/
        /*userMobile.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getAction() == KeyEvent.KEYCODE_ENTER)) {
                    verifyMobile(userMobile.getText().toString());
                    return true;
                }
                return false;
            }
        });*/

       /* userMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getAction() == KeyEvent.KEYCODE_ENTER)) {
                    verifyMobile(userMobile.getText().toString());
                    return true;
                }
                return false;
            }
        });*/

       /* userEmailEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_GO||actionId==EditorInfo.IME_ACTION_NEXT||actionId==EditorInfo.IME_ACTION_SEARCH ||(event.getAction()==KeyEvent.ACTION_DOWN && event.getAction()==KeyEvent.KEYCODE_ENTER)) {

                        // the user is done typing.
                    if(verifyEmail(userEmailEdit.getText().toString())) {
                        emailVerify(userEmailEdit.getText().toString());
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Please enter correct emailid", Toast.LENGTH_SHORT).show();
                    }

                        return true; // consume.


                }
                return false;
            }
        });

        userEmailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (verifyEmail(userEmailEdit.getText().toString())) {
                        emailVerify(userEmailEdit.getText().toString());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please enter correct emailid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        userMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH || (event.getAction() == KeyEvent.ACTION_DOWN && event.getAction() == KeyEvent.KEYCODE_ENTER)) {

                    // the user is done typing.
                    if (userMobile.getText().toString().length() == 10) {
                        verifyMobile(userMobile.getText().toString());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Mobile no must be 10 digit", Toast.LENGTH_SHORT).show();
                    }

                    return true; // consume.


                }
                return false;
            }
        });

        userMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if (userMobile.getText().toString().length() == 10) {
                        verifyMobile(userMobile.getText().toString());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Mobile no must be 10 digit", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/


       /* userEmailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailVerify(userEmailEdit.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });*/

        /*userMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyMobile(userMobile.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/



    }
    private void hideKeyboard(EditText editText)
    {
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }





    @Override
    public void onClick(View v) {
       /* if(v==alreadySignIn){
            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
           // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(v==signUpButton){
            //startActivity(new Intent(SignUpActivity.this,ProfileActivity.class));
            if(userNameEdit.getText().length()>0){
                if(verifyEmail(userEmailEdit.getText().toString().trim())){
                    if(userPassword.getText().toString().equals(userConfirmPassword.getText().toString())){
                        if(userMobile.getText().toString().length()==10){
                           if(checkBoxTerm.isChecked()){
                               sendRequestToServer(userNameEdit.getText().toString(),userEmailEdit.getText().toString().trim(),userPassword.getText().toString(),userMobile.getText().toString());

                           }
                            else {
                               Toast.makeText(SignUpActivity.this, "Please Check T&C", Toast.LENGTH_SHORT).show();
                           }
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Incorrect Mobile No", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Incorrect EmailId", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(SignUpActivity.this, "Please Enter Correct Name", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void sendRequestToServer(String username, String email, String password, String mobile) {
        System.out.println(email);
        System.out.println(username);
        System.out.println(password);
        System.out.println(mobile);
      //  emailVerify(email);

      //  registeringDetailsToServer(username, email, password, mobile);

    }

   /* private void registeringDetailsToServer(final String username, String email, final String password, String mobile) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering Details..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("mobile_number", mobile);
        JSONObject jsonObject3=new JSONObject(hashMap);

        System.out.println(jsonObject3);



        JsonObjectRequest jsonObjectRequest3=new JsonObjectRequest(Request.Method.POST,ApiUrls.getSignupUrl(), jsonObject3, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println(jsonObject);
                progressDialog.dismiss();
                    serverResponse(jsonObject,username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                System.out.println(volleyError);
                Toast.makeText(SignUpActivity.this, "Something Wrong!Try Again", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue3= Volley.newRequestQueue(this);

        jsonObjectRequest3.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest3.setShouldCache(false);
        requestQueue3.add(jsonObjectRequest3);
    }

    private void serverResponse(JSONObject jsonObject, String username) {
        try {
            boolean res=jsonObject.getBoolean("success");
            if(res){
               sharedPreference=this.getSharedPreferences(temp_Signup, 0);
                editor=sharedPreference.edit();
                JSONObject child=jsonObject.getJSONObject("data");
                String auth=child.getString("auth_token");
                String email=child.getString("email");
                String mobile=child.getString("mobile");
                String id=child.getString("userId");


                editor.putString("name",username);
                editor.putBoolean("signup",true);
                editor.putString("auth", auth);
                editor.putString("email",email);
                editor.putString("mobile",mobile);
                editor.putString("id",id);
                editor.commit();

                Toast.makeText(SignUpActivity.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this,OtpVerificationActivity.class));
            }
            else {
                Toast.makeText(SignUpActivity.this, "Failed to SignUp!Please Try Again", Toast.LENGTH_SHORT).show();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verifyMobile(final String mobile) {
        final ProgressDialog progressDialog11=new ProgressDialog(this);
        progressDialog11.setMessage("Verifying Mobile..");
        progressDialog11.show();
        progressDialog11.setCancelable(false);
        HashMap<String,String> hashMapMobile=new HashMap<String, String>();
        hashMapMobile.put("mobile",mobile);
        JSONObject jsonObject1=new JSONObject(hashMapMobile);
        


        JsonObjectRequest jsonObjectRequest2=new JsonObjectRequest(Request.Method.POST, ApiUrls.getVerifyMobileUrl(), jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog11.dismiss();
                System.out.println(jsonObject);
                mobileResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog11.dismiss();
                System.out.println(volleyError);
                Toast.makeText(SignUpActivity.this, "Not Found!", Toast.LENGTH_SHORT).show();
                userMobile.setText("");

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue1= Volley.newRequestQueue(this);

        jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest2.setShouldCache(false);
        requestQueue1.add(jsonObjectRequest2);


    }

    private void mobileResponse(JSONObject jsonObject) {
        try {
            if(jsonObject.getBoolean("success")){

                hideKeyboard(userMobile);
                // registeringDetailsToServer(userNameEdit.getText().toString(), userEmailEdit.getText().toString().trim(), userPassword.getText().toString(), mobile.toString().trim());
            }
            else{
                Toast.makeText(SignUpActivity.this, "mobile no already exist", Toast.LENGTH_SHORT).show();
                userMobile.setText("");
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private void emailVerify(String email) {

        System.out.println(email);

        HashMap<String,String> hashMapEmail=new HashMap<String, String>();
        hashMapEmail.put("email", email);
        JSONObject jsonObjectEmail=new JSONObject(hashMapEmail);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Verifying Email..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        System.out.println(jsonObjectEmail);

        JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(Request.Method.POST,ApiUrls.getVerifyEmailUrl() ,jsonObjectEmail, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                System.out.println(jsonObject);
                emailResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                System.out.println(volleyError);
                Toast.makeText(SignUpActivity.this, "Not Found!", Toast.LENGTH_SHORT).show();
                userEmailEdit.setText("");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueueEmail= Volley.newRequestQueue(this);

        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest1.setShouldCache(false);
        requestQueueEmail.add(jsonObjectRequest1);



    }

    private void emailResponse(JSONObject jsonObject) {
        try {
            if(jsonObject.getBoolean("success")){
                hideKeyboard(userEmailEdit);
                //  verifyMobile(userMobile.getText().toString());
            } else{
                Toast.makeText(SignUpActivity.this, jsonObject.getString("Email already exist!"), Toast.LENGTH_SHORT).show();
                userEmailEdit.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean verifyEmail(String email) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
