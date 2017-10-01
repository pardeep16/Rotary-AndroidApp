package dev.pardeep.healthappointment;

import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Map;
import java.util.StringTokenizer;

public class OtpVerificationActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver=new SmsReceiverOtp();
    LocalBroadcastManager localBroadcastManager;
    Button registerButton,resendButton;
    EditText editTextViewBroadcast;
    IntentFilter intentFilter;

    SharedPreferences sharedPreference,sharedpref,sharePrefLogin;
    SharedPreferences.Editor editor;

    TextView textViewTimer;
    private static String temp_Save_Otp="verifyotp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        sharedPreference=this.getSharedPreferences(SharedPrefManager.getSaveOtpSession(),0);

        textViewTimer=(TextView)findViewById(R.id.textviewtimer);

       /* if(sharedPreference.contains("signup")){
            String mobile=sharedPreference.getString("mobile",null);
            sendingOtp(mobile);
        }
        else {
           System.out.println("data n't save");
        }*/

        startTimer();

        if(!checkPermission("android.permission.READ_SMS")){
            requestPermission("android.permission.READ_SMS");
        }



        intentFilter=new IntentFilter();
        //requestSmsPermission();

        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(23633665);
        editTextViewBroadcast=(EditText)findViewById(R.id.editTextViewOtp);
        try {
            registerReceiver(broadcastReceiver, intentFilter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        registerButton=(Button)findViewById(R.id.buttonVerifyOtp);
        resendButton=(Button)findViewById(R.id.buttonResendOtp);

        resendButton.setEnabled(false);

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    registerReceiver(broadcastReceiver, intentFilter);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                resendButton.setEnabled(false);
                startTimer();
                sendRequestForOtp(sharedPreference.getString("mobile", null));

                /*if(sharedPreference.contains("signup")){
                    String mobile=sharedPreference.getString("mobile",null);
                    sendingOtp(mobile);
                }
                else {
                    System.out.println("data n't save");
                }*/
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextViewBroadcast.getText().toString().length() > 0) {
                    verifyingOtp(editTextViewBroadcast.getText().toString());
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Please Enter Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                responseFromServer(jsonObject,mobileno);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(OtpVerificationActivity.this, "Something Wrong!Try Again", Toast.LENGTH_SHORT).show();

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
                startTimer();
               // startActivity(new Intent(getActivity(), OtpVerificationActivity.class));
            }
            else {

            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void startTimer() {
        new CountDownTimer(20000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText("Wait for "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("");
                resendButton.setEnabled(true);
            }
        }.start();
    }


    /*private void sendingOtp(String mobile) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sending OTP..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("mobile",mobile);
        JSONObject jsonObject=new JSONObject(hashMap);



        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, ApiUrls.getVerifyOYTP(), jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                try {
                    if(jsonObject.getBoolean("success")){
                        Toast.makeText(OtpVerificationActivity.this, jsonObject.getString("message")+" Enter OTP here.", Toast.LENGTH_SHORT).show();
                        saveTemporary(jsonObject);
                    }
                    else{
                        Toast.makeText(OtpVerificationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(OtpVerificationActivity.this, "Network Not Found!Try Again", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(OtpVerificationActivity.this,SignUpActivity.class));
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }*/

    private void saveTemporary(JSONObject jsonObject) {

        System.out.println("Response :" + jsonObject);
        try {
            JSONObject jsonObject1=jsonObject.getJSONObject("otp");
            sharedpref=this.getSharedPreferences(temp_Save_Otp,0);
            editor=sharedpref.edit();
            editor.putString("otp",jsonObject1.getString("otp"));
            editor.putString("token",jsonObject1.getString("token"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestSmsPermission() {
        String permission = "android.provider.Telephony.SMS_RECEIVED";
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    private void requestWritePer() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,"android.permission.WRITE_EXTERNAL_STORAGE")){

            //Toast.makeText(this,"Read Per",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{"android.permission.READ_EXTERNAL_STORAGE"},2910);
        }
    }

    private boolean checkPermission(String perms){
        int result = ContextCompat.checkSelfPermission(this,perms);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
    private void requestPermission(String perms){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,perms)){

            //Toast.makeText(this,"Read Per",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{perms},100);
        }
    }




    public class SmsReceiverOtp extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
          //  createNotification();
            editTextViewBroadcast=(EditText)findViewById(R.id.editTextViewOtp);

            System.out.println("registered");
            Bundle bundle=intent.getExtras();
            if(bundle!=null){
                Object[] pdus= (Object[]) bundle.get("pdus");
                SmsMessage[] smsMessages=new SmsMessage[pdus.length];
                for(int i=0;i<smsMessages.length;i++ ){
                    smsMessages[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for(SmsMessage message:smsMessages){
                    String messageAddress=message.getOriginatingAddress();
                    String messageDetails=message.getDisplayMessageBody();

                    //System.out.println(messageAddress);
                    Log.d("",messageDetails);


                    //editTextViewBroadcast.setText(messageAddress + "\n" + messageDetails);
                    //Toast.makeText(OtpVerificationActivity.this, "msg :" + messageAddress + "\n" + messageDetails, Toast.LENGTH_SHORT).show();

                    StringTokenizer st = new StringTokenizer(messageDetails,":");
                    String strr=messageDetails.substring(0, 6);

                    Log.d("Otttp :", strr);
                    /*st.nextToken();
                    String key = st.nextToken().trim();*/
                    editTextViewBroadcast.setText(strr);

                    unregisterReceiver(broadcastReceiver);
                    verifyingOtp(strr);
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void verifyingOtp(String key) {
        sharedPreference=this.getSharedPreferences(SharedPrefManager.getSaveOtpSession(),0);


        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("session", sharedPreference.getString("sessionkey",null));
        hashMap.put("mobile",sharedPreference.getString("mobile",null));
        hashMap.put("otp",key);
        JSONObject jsonObject=new JSONObject(hashMap);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Verifying OTP..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,ApiUrls.getVerifyOYTP() ,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                serverResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(OtpVerificationActivity.this, "Not Found!Check your network and enter correct otp", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void serverResponse(JSONObject jsonObject) {
        try {
            //String res=jsonObject.getString("Status");
            if(jsonObject.getBoolean("success")){
                //Toast.makeText(OtpVerificationActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
               // removeAllData();
               // openNewProfile();
                signInWithMobile(jsonObject);
                removeAllData();
            }
            else {
                Toast.makeText(OtpVerificationActivity.this, "Incorrect Code", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signInWithMobile(JSONObject jsonObject1) {
        sharedPreference=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);
        editor=sharedPreference.edit();
        editor.putBoolean("login", true);
        try {

            JSONObject jsonObject=jsonObject1.getJSONObject("data");

           System.out.println("mess...." + jsonObject);
            editor.putString("name", jsonObject.getString("name"));
            editor.putString("emailid",jsonObject.getString("emailid"));
            editor.putString("login_id", jsonObject.getString("userid"));
            editor.putString("apikey",jsonObject.getString("apikey"));
            editor.putString("phone", jsonObject.getString("phone"));
            editor.commit();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                Intent intent=new Intent(OtpVerificationActivity.this,HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent,bndlanimation);
            }
            else {
                Intent intent=new Intent(OtpVerificationActivity.this,HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void removeAllData() {
        sharedPreference=this.getSharedPreferences(SharedPrefManager.getSaveOtpSession(),0);
        /*sharePrefLogin=this.getSharedPreferences(LoginActivity.getSharedProfile(), 0);
        editor=sharePrefLogin.edit();
        editor.putBoolean("login", true);
        editor.putString("name",sharedPreference.getString("name",null));
        editor.putString("auth", sharedPreference.getString("auth", null));
        editor.putString("email", sharedPreference.getString("email",null));
        editor.putString("name",null);
        editor.putString("mobile",sharedPreference.getString("mobile",null));
        editor.putString("id", sharedPreference.getString("id",null));
        editor.commit();*/

        sharedPreference.edit().clear().commit();

        /*sharedpref=this.getSharedPreferences(temp_Save_Otp,0);
        sharedpref.edit().clear().commit();
*/



        /*Intent intent=new Intent(OtpVerificationActivity.this,HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Toast.makeText(OtpVerificationActivity.this, "Welcome to Medimojo", Toast.LENGTH_SHORT).show();
        startActivity(intent);*/
    }

    private void openNewProfile() {

    }


    private void createNotification() {
        Intent intent=new Intent(this,HomeScreen.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder notification=new Notification.Builder(getApplicationContext());
        notification.setAutoCancel(true);
        notification.setContentText("otp receive");
        notification.setContentTitle("Notification");
        notification.setContentIntent(pendingIntent);
        /*notification.setSmallIcon(R.drawable.l);*/
        notification.setSound(sound);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());

    }


}
