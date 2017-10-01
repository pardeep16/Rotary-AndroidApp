package dev.pardeep.healthappointment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewAppointmentsAct extends AppCompatActivity {

    ListView listViewMain;
    ArrayList<AppointmentsListContent> arrayList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);


        listViewMain=(ListView)findViewById(R.id.listView);

        Button buttonback=(Button)findViewById(R.id.backButton);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //arrayList=new ArrayList<>();

        /*for (int i=0;i<1;i++){
          //  arrayList.add(new AppointmentsListContent(doc_contact[i],app_id[i],doc_name[i],department[i],app_date[i]));

                Toast.makeText(ViewAppointmentsAct.this, "call 1", Toast.LENGTH_SHORT).show();
                arrayList.add(new AppointmentsListContent("740555421","1210","abc","dentist", ""));

        }
*/
        /*AppointmentListAdapter listAdapter=new AppointmentListAdapter();
        AppointmentListAdapter.setArrayList(arrayList);
        listViewMain.setAdapter(listAdapter);*/


        sendRequestForAppointments();


    }

    private void sendRequestForAppointments() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        sharedPreferences=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("user_id",sharedPreferences.getString("login_id",null));

        JSONObject jsonObject=new JSONObject(hashMap);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, ApiUrls.getGetListAppointments(), jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                showResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(ViewAppointmentsAct.this, "No Internet Connection Found!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);



    }

    private void showResponse(JSONObject jsonObject) {
        try {
            System.out.println(jsonObject);
            if(jsonObject.getBoolean("success")){
                arrayList=new ArrayList<>();
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                if(jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject childObj=jsonArray.getJSONObject(i);
                        arrayList.add(new AppointmentsListContent(childObj.getString("app_id"),childObj.getString("doc_id"),childObj.getString("app_date"),childObj.getString("app_status"),childObj.getString("payment_status"),childObj.getString("app_time")));

                    }
                    AppointmentListAdapter appointmentListAdap=new AppointmentListAdapter(this,listViewMain);
                    AppointmentListAdapter.setArrayList(arrayList);
                    listViewMain.setAdapter(appointmentListAdap);
                }
                else {
                    Toast.makeText(ViewAppointmentsAct.this, "No Appointment Found", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(ViewAppointmentsAct.this, "No Appointment Found", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_appointments, menu);
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
