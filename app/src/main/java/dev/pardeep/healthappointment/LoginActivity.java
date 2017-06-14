package dev.pardeep.healthappointment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private static final int RC_SIGN_IN = 9001;
    Button loginButton,mobileSignin;

    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient googleApiClient;




    JsonObjectRequest jsonObjectRequest;
    public static final String TAG = "reqTag";
    RequestQueue requestQueue;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static String sharedProfile="userlogin";

    public static String getSharedProfile() {
        return sharedProfile;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences=this.getSharedPreferences(sharedProfile, 0);

        googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestId().requestProfile().build();

        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        if(!sharedPreferences.getBoolean("login",false)){

        }
        else{
            Bundle bndlanimation =
                    ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
            startActivity(new Intent(LoginActivity.this,HomeScreen.class),bndlanimation);
        }

        mobileSignin=(Button)findViewById(R.id.mobilesignin);
        mobileSignin.setOnClickListener(this);


        loginButton=(Button)findViewById(R.id.sign_in_button);

        loginButton.setOnClickListener(this);





    }


    @Override
    public void onClick(View v) {
        if(v==loginButton){
           // startActivity(new Intent(LoginActivity.this,HomeScreen.class));
            signIn();
        }
        else if (v==mobileSignin){
            //startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

        }

    }

    private void signIn() {
        Intent startIntent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(startIntent, RC_SIGN_IN);
    }

    /* private void sendRequestToServer() {
 
         HashMap<String,String> hashMap=new HashMap<String, String>();
         hashMap.put("username", userName.getText().toString());
         hashMap.put("password", userPassword.getText().toString());
 
         JSONObject jsonReq=new JSONObject(hashMap);
 
         final ProgressDialog progressDialog=new ProgressDialog(this);
         progressDialog.setMessage("Please Wait..");
         progressDialog.show();
 
         progressDialog.setCancelable(false);
 
 
 
         jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,ApiUrls.loginUrl,jsonReq, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject jsonObject) {
                 progressDialog.dismiss();
                 serverResponse(jsonObject);
 
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError volleyError) {
                 progressDialog.dismiss();
                 Toast.makeText(LoginActivity.this, "Network Problem!Try Again", Toast.LENGTH_SHORT).show();
             }
         })
             {
                 @Override
                 public Map<String, String> getHeaders() throws AuthFailureError {
                 Map<String, String>  params = new HashMap<String, String>();
                 params.put("method", "json");
                 return params;
             }
         };
 
         requestQueue= Volley.newRequestQueue(this);
         requestQueue.add(jsonObjectRequest);
 
         progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
             @Override
             public void onCancel(DialogInterface dialog) {
                 if (jsonObjectRequest != null) {
                     requestQueue.cancelAll(jsonObjectRequest);
                 }
             }
         });
 
     }
 
     private void serverResponse(JSONObject jsonObject) {
         System.out.println(jsonObject);
         try {
             String message=jsonObject.getString("message");
             if(jsonObject.getString("user").equalsIgnoreCase("false")){
                 Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
             }
             else {
                 JSONObject child=jsonObject.getJSONObject("user");
                 boolean saveData=saveDataToSharePref(child);
                 //
                 if(saveData){
                     Toast.makeText(LoginActivity.this,message, Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(LoginActivity.this,HomeScreen.class));
                 }
                 else {
                     Toast.makeText(LoginActivity.this, "Login Again!", Toast.LENGTH_SHORT).show();
                 }
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }
 
     private boolean saveDataToSharePref(JSONObject child) {
         try {
             String name=child.getString("username");
             String auth=child.getString("auth_token");
             String email=child.getString("email");
             String mobile=child.getString("mobile");
             String id=child.getString("id");
             sharedPreferences=this.getSharedPreferences(sharedProfile,0);
             editor=sharedPreferences.edit();
             editor.putBoolean("login",true);
             editor.putString("name",name);
             editor.putString("auth",auth);
             editor.putString("email",email);
             editor.putString("mobile",mobile);
             editor.putString("id",id);
             editor.commit();
         } catch (JSONException e) {
             e.printStackTrace();
             return false;
         }
         return true;
     }
 */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           /* mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);*/
            String name=acct.getDisplayName().toString();
            String email=acct.getEmail();
            String gmail_id=acct.getId();
            boolean save=saveDataToPref(name,email,gmail_id);


            if(save){
                Toast.makeText(LoginActivity.this, ""+name+"\n"+email+"\n"+gmail_id, Toast.LENGTH_SHORT).show();
               startActivity(new Intent(LoginActivity.this, HomeScreen.class));
            }
            else {
                Toast.makeText(LoginActivity.this, "Failed to Login!Try Again", Toast.LENGTH_SHORT).show();
            }

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            Toast.makeText(LoginActivity.this, "Failed to Login!Try Again", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean saveDataToPref(String name, String email, String gmail_id) {
        try {

            sharedPreferences=this.getSharedPreferences(sharedProfile, 0);
            editor=sharedPreferences.edit();
            editor.putBoolean("login", true);
            editor.putString("name",name);
            editor.putString("email",email);
            editor.putString("login_id",gmail_id);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(jsonObjectRequest!=null) {
            requestQueue.cancelAll(jsonObjectRequest);
            System.out.println("canceld");
        }
    }



    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "Failed to Login!Try Again", Toast.LENGTH_SHORT).show();
    }
}
