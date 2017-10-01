package dev.pardeep.healthappointment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import dev.pardeep.healthappointment.Fragments.ErrorFragment;
import dev.pardeep.healthappointment.Fragments.SuccessFragment;
import dev.pardeep.healthappointment.PaymentActivities.ChoosePaymentOptions;


public class OpdCounter extends AppCompatActivity {


    SharedPreferences sharedPreferences,sharedPreferences1;

    TextView textViewDocName,textViewCounter;

    Button bookButton;

    Order order=null;

    private static String doc_id= null;

    private  HashMap<String,String> opd_details=null;
    Socket socket;

    {

            try {
                socket= IO.socket(ApiUrls.getOpd_counter_socket());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opd_counter);
        bookButton=(Button)findViewById(R.id.bookopdbutton);

        Instamojo.initialize(this);


        Instamojo.setBaseUrl("https://api.instamojo.com/");
        Instamojo.setLogLevel(Log.DEBUG);





        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* startPreCreatedUI(order);*/

               // callToPaymentRequest();

                showAlertPaymentOption();
            }
        });



        sharedPreferences=this.getSharedPreferences(SharedPrefManager.getTempAppointmentProf(), 0);
        doc_id=sharedPreferences.getString("doc_id", null);

        textViewDocName=(TextView)findViewById(R.id.doc_name);
        textViewCounter=(TextView)findViewById(R.id.opd_counter);

        textViewDocName.setText(sharedPreferences.getString("doc_name",null));
        textViewCounter.setText("Wait to Load..");

        socket.connect();

        System.out.println(sharedPreferences.getAll());

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("doc_id",doc_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("getcounter", jsonObject);

        socket.on("counter", counterget);

        socket.on("updatecounter", updatecounter);
        
    }

    private void showAlertPaymentOption() {
        LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());
        View view=layoutInflater.inflate(R.layout.pay_dialog, null);

        final Dialog dialog=new Dialog(this);
        dialog.setContentView(view);

        TextView textView_opdcounter,textView_opdAmount;
        Button button_payment,button_cancel;

        textView_opdcounter=(TextView)view.findViewById(R.id.opd_counter);
        textView_opdAmount=(TextView)view.findViewById(R.id.textViewpayamount);
        button_payment=(Button)view.findViewById(R.id.paymentbutton);
        button_cancel=(Button)view.findViewById(R.id.paymentcancelbutton);

        int counter=Integer.parseInt(String.valueOf(textViewCounter.getText()));
        counter+=1;
        textView_opdcounter.setText("" + counter);

        textView_opdAmount.setText("Pay Amount : Rs " + "10");
        button_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getPaymentRequest();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void getPaymentRequest() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        sharedPreferences1=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);

        final String api_key=sharedPreferences1.getString("apikey",null);

        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(com.android.volley.Request.Method.POST,ApiUrls.getPaymentRequestApi(),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                showResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(OpdCounter.this, "No Internet Found!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", api_key);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);



    }

    private void showResponse(JSONObject jsonObject) {

        Log.e("app", "" + jsonObject);
        try {
            if(jsonObject.getBoolean("success")){
                String transc_id=jsonObject.getString("trans_id");
                String obj=jsonObject.getString("data");
                JSONObject childObj=new JSONObject(obj);
                String access_token=childObj.getString("access_token");

                System.out.println("Access token :"+access_token);
                sharedPreferences1=this.getSharedPreferences(LoginActivity.getSharedProfile(), 0);
                String name=sharedPreferences1.getString("name",null);
                String emailid=sharedPreferences1.getString("emailid",null);
                String phone=sharedPreferences1.getString("phone",null);
                order=new Order(access_token,transc_id,name,"pk.raswant@gmail.com","8059261421","10","paymentopd");

                order.setRedirectionUrl("https://www.instamojo.com/integrations/android/redirect/");
                callToPaymentRequest(order);

            }
            else {
                Toast.makeText(OpdCounter.this, "Server down Try Again Later", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callToPaymentRequest(final Order order1) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        sharedPreferences=this.getSharedPreferences(SharedPrefManager.getTempAppointmentProf(),0);

        String email=sharedPreferences.getString("email",null);

        System.out.println(order.getId());


        if (!order.isValid()){
            //oops order validation failed. Pinpoint the issue(s).
            progressDialog.dismiss();
            Log.e("App","Not valid");

            /*if (!order.isValidName()){
                progressDialog.dismiss();
                Log.e("App", "Buyer name is invalid");
            }

            if (!order.isValidEmail()){
                progressDialog.dismiss();
                Log.e("App", "Buyer email is invalid");
            }

            if (!order.isValidPhone()){
                progressDialog.dismiss();
                Log.e("App", "Buyer phone is invalid");
            }*/

            if (!order.isValidAmount()){
                progressDialog.dismiss();
                Log.e("App", "Amount is invalid");
            }

            if (!order.isValidDescription()){
                progressDialog.dismiss();
                Log.e("App", "description is invalid");
            }

            if (!order.isValidTransactionID()){
                progressDialog.dismiss();
                Log.e("App", "Transaction ID is invalid");
            }

            if (!order.isValidRedirectURL()){
                progressDialog.dismiss();
                Log.e("App", "Redirection URL is invalid");
            }

            if (!order.isValidWebhook()) {
                progressDialog.dismiss();
                showToast("Webhook URL is invalid");
            }
            showToast("Invalid Request Try Again!");
            return;
        }
        else {
            Request request = new Request(order, new OrderRequestCallBack() {
                @Override
                public void onFinish(Order order, Exception error) {
                    //dismiss the dialog if showed

                    // Make sure the follwoing code is called on UI thread to show Toasts or to
                    //update UI elements
                    if (error != null) {
                        if (error instanceof Errors.ConnectionError) {
                            progressDialog.dismiss();
                            Log.e("App", "No internet connection");
                            showToast("No internet connection");
                        } else if (error instanceof Errors.ServerError) {
                            progressDialog.dismiss();
                            Log.e("App", "Server Error. Try again");
                            showToast("Server Error. Try again");
                        } else if (error instanceof Errors.AuthenticationError) {
                            progressDialog.dismiss();
                            Log.e("App", "Access token is invalid or expired");
                            showToast("Access token is invalid or expired");
                        } else if (error instanceof Errors.ValidationError) {
                            // Cast object to validation to pinpoint the issue
                            Errors.ValidationError validationError = (Errors.ValidationError) error;
                            if (!validationError.isValidTransactionID()) {
                                progressDialog.dismiss();
                                Log.e("App", "Transaction ID is not Unique");
                                showToast("Transaction ID is not Unique");
                                return;
                            }
                            if (!validationError.isValidRedirectURL()) {
                                Log.e("App", "Redirect url is invalid");
                                return;
                            }


                            if (!validationError.isValidWebhook()) {
                                //showToast("Webhook url is invalid");
                                return;
                            }

                            if (!validationError.isValidPhone()) {
                                progressDialog.dismiss();
                                Log.e("App", "Buyer's Phone Number is invalid/empty");
                                return;
                            }
                            if (!validationError.isValidEmail()) {
                                progressDialog.dismiss();
                                Log.e("App", "Buyer's Email is invalid/empty");
                                return;
                            }
                            if (!validationError.isValidAmount()) {
                                Log.e("App", "Amount is either less than Rs.9 or has more than two decimal places");
                                return;
                            }
                            if (!validationError.isValidName()) {
                                Log.e("App", "Buyer's Name is required");
                                return;
                            }
                        } else {
                            Log.e("App", error.getMessage());
                        }
                        return;
                    }

                    //startPreCreatedUI(order);
                    progressDialog.dismiss();
                    startPreCreatedUI(order1);
                }
            });

            request.execute();


        }
    }


    private void startCustomUI(Order order) {
        Intent intent = new Intent(getBaseContext(), ChoosePaymentOptions.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private void showToast(String s) {
        Toast.makeText(OpdCounter.this, s, Toast.LENGTH_SHORT).show();
    }

    private void startPreCreatedUI(Order order) {
        Intent intent=new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER,order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);

            System.out.println("orderr"+orderID);
            System.out.println("transcc"+transactionID);
            System.out.println("data "+data);
            System.out.println("payid"+paymentID);

           // System.out.println(order.getAuthToken());

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (orderID != null && transactionID != null && paymentID != null) {
                //Check for Payment status with Order ID or Transaction ID

                checkForPaymentStatus(order.getAuthToken(),orderID,transactionID,paymentID);

            } else {
                System.out.println("null value");
                showAlert("Oops!! Payment was cancelled");
               // Toast.makeText(OpdCounter.this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
                //Oops!! Payment was cancelled
            }
        }
        else {
            showAlert("Oops!! Payment was cancelled");
        }
    }

    private void checkForPaymentStatus(final String authToken, String orderID, String transactionID, final String paymentID) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..Booking OPD");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String paymentUrlStatus=ApiUrls.getDomain()+ApiUrls.getCheckPaymentStatus();

        sharedPreferences=this.getSharedPreferences(SharedPrefManager.getTempAppointmentProf(),0);

        sharedPreferences1=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);


        String doc_id=sharedPreferences.getString("doc_id",null);
        String doc_name=sharedPreferences.getString("doc_name",null);
        int opd_counter=Integer.parseInt(textViewCounter.getText().toString())+1;
        String name=sharedPreferences.getString("name",null);
        String emailid=sharedPreferences.getString("email",null);
        String phone=sharedPreferences.getString("mobile",null);
        String age=sharedPreferences.getString("age",null);
        String user_id=sharedPreferences1.getString("login_id",null);
        String p_id=sharedPreferences.getString("p_id",null);
        String gender=sharedPreferences.getString("gender",null);



        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("emailid",emailid);
        hashMap.put("p_age",age);
        hashMap.put("doc_id",doc_id);
        hashMap.put("doc_name",doc_name);
        hashMap.put("opd_counter",""+opd_counter);
        hashMap.put("phone",phone);
        hashMap.put("pay_id",paymentID);
        hashMap.put("trans_id",transactionID);
        hashMap.put("order_id",orderID);
        hashMap.put("user_id",user_id);
        hashMap.put("p_id",p_id);
        hashMap.put("p_sex",gender);




        JSONObject jsonObject=new JSONObject(hashMap);

        System.out.println(jsonObject);




        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(com.android.volley.Request.Method.POST,paymentUrlStatus, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                showPaymentResponse(jsonObject,paymentID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                showAlert(volleyError.toString()+"\n\n"+"Please Contact to Support team");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization",authToken);
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void showPaymentResponse(JSONObject jsonObject, String paymentID) {
        System.out.println(jsonObject);
        //sharedPreferences1=this.getSharedPreferences(SharedPrefManager.getTempAppointmentProf(),0);
        try {
            if (jsonObject.getBoolean("status")){
                JSONObject childObj=jsonObject.getJSONObject("data");
                Bundle bundle=new Bundle();
                bundle.putBoolean("status", true);
                bundle.putString("opd_id", childObj.getString("opd_id"));
                bundle.putString("p_id",childObj.getString("p_id"));
                bundle.putString("payment_id",childObj.getString("payment_id"));
                SuccessFragment successFragment=new SuccessFragment();
                successFragment.setArguments(bundle);
               changeFragment(successFragment);

            }
            else {
                ErrorFragment errorFragment=new ErrorFragment();
                changeFragment(errorFragment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.relativelayout,fragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("fragment").commit();

    }

    private void showAlert(String s) {
        AlertDialog.Builder alertDialogg=new AlertDialog.Builder(this);
        alertDialogg.setTitle("Status");
        alertDialogg.setMessage(s);
        alertDialogg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog1=alertDialogg.create();
        dialog1.show();
    }

    private Emitter.Listener updatecounter=new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject=(JSONObject)(args[0]);

                    String token="";
                    String doc_id_get="";

                    try {
                        token=jsonObject.getString("token");
                        doc_id_get=jsonObject.getString("doc_id");
                        System.out.println(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(doc_id);
                    if(doc_id.equalsIgnoreCase(doc_id_get)){
                        displayCounter(token);
                    }
                }
            });

        }
    };

    private Emitter.Listener counterget=new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject=(JSONObject)(args[0]);

                    String token="";
                    String date;

                    try {
                        token=jsonObject.getString("opd_token");
                        System.out.println(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    displayCounter(token);
                }
            });
        }
    };

    private void displayCounter(String token) {
        textViewCounter=(TextView)findViewById(R.id.opd_counter);
        textViewCounter.setText(token);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opd_counter, menu);
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
}
