package dev.pardeep.healthappointment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class SelectProfileForOpd extends AppCompatActivity {

    static ArrayList<PatientProfile> arrayList=null;

    Button back_Button,skip_Button;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile_for_opd);

        back_Button=(Button)findViewById(R.id.backButton);
        skip_Button=(Button)findViewById(R.id.skipButton);
        listView=(ListView)findViewById(R.id.listView);

        back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectProfileForOpd.this,BookAppointmentAct.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("profile", false);
                startActivity(intent);
            }
        });

        skip_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectProfileForOpd.this,BookAppointmentAct.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("profile", false);
                startActivity(intent);
            }
        });

        sendRequestForProfiles();


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /*HashMap<String,String> hashMap=new HashMap<String, String>();
               hashMap.put("p_id",arrayList.get(position).getPatientId());
               hashMap.put("p_name",arrayList.get(position).getPatientName());
               hashMap.put("p_age",arrayList.get(position).getPatientAge());
               hashMap.put("p_sex",arrayList.get(position).getPatientGender());*/
               Intent intent=new Intent(SelectProfileForOpd.this,BookAppointmentAct.class);
               //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("profile",true);
               intent.putExtra("p_id",arrayList.get(position).getPatientId());
               intent.putExtra("p_name",arrayList.get(position).getPatientName());
               intent.putExtra("p_age",arrayList.get(position).getPatientAge());
               intent.putExtra("p_sex",arrayList.get(position).getPatientGender());
               startActivity(intent);
           }
       });
    }

    private void sendRequestForProfiles() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading profiles..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if(!networkConnection()){
            progressDialog.dismiss();

            Toast.makeText(SelectProfileForOpd.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        String patientProfileUrl=ApiUrls.getDomain()+ApiUrls.getProfilePatients();
        SharedPreferences sharedPreferences=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);
        String apikey=sharedPreferences.getString("apikey", null);
        String phone=sharedPreferences.getString("phone",null);

        String query="?apikey="+apikey+"&phone="+phone;

            patientProfileUrl= patientProfileUrl+query;
            System.out.println("Url :"+patientProfileUrl);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, patientProfileUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                showResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(SelectProfileForOpd.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

    private void showResponse(JSONObject jsonObject) {

        System.out.println("response :"+jsonObject);

        try {
            if(jsonObject.getBoolean("status")){

                JSONArray jsonArray=jsonObject.getJSONArray("data");
                if(jsonArray.length()>0){
                    arrayList=new ArrayList<PatientProfile>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonChild=jsonArray.getJSONObject(i);
                      arrayList.add(new PatientProfile(jsonChild.getString("p_name"),jsonChild.getString("p_age"),jsonChild.getString("p_id"),jsonChild.getString("p_sex")));
                    }
                    ProfileListAdapter profileList=new ProfileListAdapter();
                    listView.setAdapter(profileList);
                }
                else {
                    Toast.makeText(SelectProfileForOpd.this, "No profiles found!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SelectProfileForOpd.this,BookAppointmentAct.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("profile", false);
                    startActivity(intent);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean networkConnection() {
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else if(Build.VERSION.SDK_INT >= 21){
            Network[] info = connectivity.getAllNetworks();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i] != null && connectivity.getNetworkInfo(info[i]).isConnected()) {
                        return true;
                    }
                }
            }
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
            final NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_profile_for_opd, menu);
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

    public class ProfileListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
            View view=layoutInflater.inflate(R.layout.profile_layout_opd, null);
            TextView textViewId=(TextView)view.findViewById(R.id.textViewId);
            TextView textViewName=(TextView)view.findViewById(R.id.textViewName);
            TextView textViewAge=(TextView)view.findViewById(R.id.textViewAge);
            TextView textViiewGen=(TextView)view.findViewById(R.id.textViewGender);



            if(arrayList!=null){
                textViewId.setText(arrayList.get(position).getPatientId());
                textViewName.setText(arrayList.get(position).getPatientName());
                textViewAge.setText(arrayList.get(position).getPatientAge());
                textViiewGen.setText(arrayList.get(position).getPatientGender());
            }

            return view;
        }
    }

    public class PatientProfile{
        String patientName;
        String patientAge;
        String patientId;
        String patientGender;

        public PatientProfile(String patientName, String patientAge, String patientId,String patientGender) {
            this.patientName = patientName;
            this.patientAge = patientAge;
            this.patientId = patientId;
            this.patientGender=patientGender;
        }

        public String getPatientName() {
            return patientName;
        }

        public String getPatientAge() {
            return patientAge;
        }

        public String getPatientId() {
            return patientId;
        }

        public String getPatientGender() {
            return patientGender;
        }
    }
}
