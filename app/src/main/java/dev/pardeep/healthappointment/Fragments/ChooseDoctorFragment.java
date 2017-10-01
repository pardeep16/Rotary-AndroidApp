package dev.pardeep.healthappointment.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import dev.pardeep.healthappointment.ApiUrls;
import dev.pardeep.healthappointment.DoctorInfo;
import dev.pardeep.healthappointment.LoginActivity;
import dev.pardeep.healthappointment.OpdCounter;
import dev.pardeep.healthappointment.R;
import dev.pardeep.healthappointment.SharedPrefManager;
import dev.pardeep.healthappointment.ShowTimingsWithDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseDoctorFragment extends Fragment implements View.OnClickListener {

    private static String[] arrayCategories={"Select Category--"};
    private static String[] doct_specialist={"","","",};
    private static String[] arrayDoctorsList={"Select Doctor--"};


    Spinner categorieslist,doctorslist;

    Button buttonToBack,submitNextButton;

    View view;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    static ArrayList<DoctorInfo> arrayListDoctorInfo=null;
    ArrayAdapter<String> arrayAdapterDoctors=null;
    ArrayAdapter<String> arrayAdapterCategory=null;

    String doc_name="";
    String doct_id="";


    public ChooseDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_choose_doctor, container, false);
        categorieslist=(Spinner)view.findViewById(R.id.spinnercategory);
        doctorslist=(Spinner)view.findViewById(R.id.spinnerdoctor);

        arrayAdapterCategory=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayCategories);
        arrayAdapterDoctors=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayDoctorsList);

       // categorieslist.setAdapter(arrayAdapterCategory);

        getDepartmenList();
        doctorslist.setAdapter(arrayAdapterDoctors);

        buttonToBack=(Button)view.findViewById(R.id.backButton);
        submitNextButton=(Button)view.findViewById(R.id.nextButton);
        buttonToBack.setOnClickListener(this);
        submitNextButton.setEnabled(false);

        buttonToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                onDestroy();
            }
        });


        categorieslist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    getDoctorsDataList(arrayCategories[position]);
                }
                else {
                    submitNextButton.setEnabled(false);
                    arrayDoctorsList=new String[1];
                    arrayDoctorsList[0]="No doctors Available";
                    arrayAdapterDoctors=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayDoctorsList);
                    doctorslist.setAdapter(arrayAdapterDoctors);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doctorslist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categorieslist.getSelectedItemPosition()!=0){
                    doc_name=arrayDoctorsList[doctorslist.getSelectedItemPosition()];
                    doct_id=arrayListDoctorInfo.get(doctorslist.getSelectedItemPosition()).getDoctor_id();

                    sharedPreferences=getActivity().getSharedPreferences(SharedPrefManager.getTempAppointmentProf(), 0);
                    editor=sharedPreferences.edit();
                    editor.putString("doc_name",doc_name);
                    editor.putString("doc_id",doct_id);
                    editor.putString("spec",arrayCategories[categorieslist.getSelectedItemPosition()]);
                    editor.commit();

                    System.out.println(sharedPreferences.getAll().toString());
                    startActivity(new Intent(getActivity(), OpdCounter.class));

                    

                }
            }
        });




        return view;
    }

    private void getDepartmenList() {

        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET,ApiUrls.getGetDeptList(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                responseDept(s,progressDialog);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Something Wrong Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void responseDept(String s, ProgressDialog progressDialog) {
        try {
            ProgressDialog progress=progressDialog;
            JSONObject jsonObject=new JSONObject(s);
            progress.dismiss();
            if(jsonObject.getBoolean("success")) {
                arrayCategories = new String[jsonObject.getJSONArray("data").length()];
                if (jsonObject.getJSONArray("data").length() > 0) {
                    JSONArray jsonArr = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        if (i==0){
                            arrayCategories[i]="Select Specialization--";
                        }
                        else{
                            arrayCategories[i] = jsonArr.getString(i);
                        }

                    }
                    categorieslist.setEnabled(true);
                    arrayAdapterCategory = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayCategories);
                    categorieslist.setAdapter(arrayAdapterCategory);


                } else {
                    categorieslist.setEnabled(false);
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                categorieslist.setEnabled(false);
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDoctorsDataList(String arrayCategory) {

        final ProgressDialog progress=new ProgressDialog(getActivity());
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        sharedPreferences=getActivity().getSharedPreferences(LoginActivity.getSharedProfile(),0);

        String apikey=sharedPreferences.getString("apikey",null);
        JSONObject jsonObject=new JSONObject();

        String urlget= null;
        try {
            urlget = ApiUrls.getGetDoctorsList()+"?category="+ URLEncoder.encode(arrayCategory, "UTF-8")+"&apikey="+apikey;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(urlget);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,urlget, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progress.dismiss();
                showResponse(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
                Toast.makeText(getActivity(), "Not Connected!Try Again", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestq= Volley.newRequestQueue(getActivity());
        requestq.add(jsonObjectRequest);

    }

    private void showResponse(JSONObject jsonObject) {
        try {
            if(jsonObject.getBoolean("success")){
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                arrayListDoctorInfo=new ArrayList<>();
                arrayDoctorsList=new String[jsonArray.length()];
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject childObj=jsonArray.getJSONObject(i);
                    arrayListDoctorInfo.add(new DoctorInfo(childObj.getString("doc_id"),childObj.getString("doc_name")));
                    arrayDoctorsList[i]=childObj.getString("doc_name");
                }

                submitNextButton.setEnabled(true);

                Toast.makeText(getActivity(), ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                arrayAdapterDoctors=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayDoctorsList);
                doctorslist.setAdapter(arrayAdapterDoctors);
            }
            else {
                submitNextButton.setEnabled(false);
                arrayDoctorsList=new String[1];
                arrayDoctorsList[0]="No doctors Available";
                arrayAdapterDoctors=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayDoctorsList);
                doctorslist.setAdapter(arrayAdapterDoctors);
                Toast.makeText(getActivity(), ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                onDestroy();
                break;
            /*case R.id.nextButton:
                openNextPage();
                break;*/
            default:
        }
    }

    private void openNextPage() {
       //changeFragment(new ShowDoctorTimings());

        startActivity(new Intent(getActivity(),ShowTimingsWithDate.class));
    }

    private void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.linearlayoutchoosedoc,fragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
