package dev.pardeep.healthappointment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gurwinder on 14-Jun-17.
 */

public class AppointmentListAdapter extends BaseAdapter {

    Context context=null;

    private static ArrayList<AppointmentsListContent> arrayList=null;

    public static ArrayList<AppointmentsListContent> getArrayList() {
        return arrayList;
    }

    ListView listview=null;

    public static void setArrayList(ArrayList<AppointmentsListContent> arrayList) {
        AppointmentListAdapter.arrayList = arrayList;
    }

    public AppointmentListAdapter(Context context, ListView listViewMain) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.appointmentlist_adapter, parent, false);
        TextView appointment_id=(TextView)view.findViewById(R.id.textViewid);
        TextView app_status=(TextView)view.findViewById(R.id.textviewappstatus);
        TextView pay_status=(TextView)view.findViewById(R.id.textviewpayment);
        TextView app_date=(TextView)view.findViewById(R.id.textviewdate);
        TextView app_time=(TextView)view.findViewById(R.id.textviewapptime);
        Button button=(Button)view.findViewById(R.id.buttoncancel);

        appointment_id.setText(arrayList.get(position).getApp_id());
        app_status.setText("Apppointment :"+arrayList.get(position).getApp_status());
        pay_status.setText("Payment :"+arrayList.get(position).getPayment_status());
        app_date.setText(arrayList.get(position).getApp_date());
        app_time.setText("Appointment Time :" + arrayList.get(position).getApp_time());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(position).getApp_status().equalsIgnoreCase("Pending")) {
                    showdialog(arrayList.get(position).getApp_id());
                }
            }
        });
        if(arrayList.get(position).getPayment_status().equalsIgnoreCase("False")){
            pay_status.setText("Payment :"+"Pending");
        }
        else if(arrayList.get(position).getPayment_status().equalsIgnoreCase("Received")){
           //button.setEnabled(false);
        }

        if(arrayList.get(position).getApp_status().equalsIgnoreCase("Cancelled")){
            button.setVisibility(View.GONE);
        }
        else if(arrayList.get(position).getApp_status().equalsIgnoreCase("Confirmed")){
            button.setText("Confirmed");
            button.setEnabled(false);


        }
        /*appointment_id.setText(arrayList.get(position).getApp_id());
        doc_name.setText(arrayList.get(position).getDoc_name());
        doc_contact.setText(arrayList.get(position).getDoc_contact());
        app_date.setText( arrayList.get(position).getApp_date());
        department.setText(arrayList.get(position).getDepartment());*/
        return view;
    }

    private void showdialog(final String app_id) {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setMessage("Do you confirm to cancel Appointment?");
        alertDialog.setTitle("Alert");
        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelAppointment(app_id);
            }
        });
        alertDialog.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert=alertDialog.create();
        alert.show();


    }

    private void cancelAppointment(String app_id) {
        final ProgressDialog progress=new ProgressDialog(context);
        progress.setMessage("Cancelling Appointment");
        progress.setCancelable(false);
        progress.show();

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("app_id",app_id);

        JSONObject jsonObject=new JSONObject(hashMap);

        JsonObjectRequest jsonReq=new JsonObjectRequest(Request.Method.POST, "http://139.59.74.116:8080/cancel/appt", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progress.dismiss();
                serverResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
                Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQ= Volley.newRequestQueue(context);
        requestQ.add(jsonReq);
    }

    private void serverResponse(JSONObject jsonObject) {
        try {
            System.out.println(jsonObject);
            if(jsonObject.getBoolean("sucess")){
                try {
                    Intent intent=new Intent(context,ViewAppointmentsAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                Toast.makeText(context, "Successfully Cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Not Cancelled", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
