package dev.pardeep.healthappointment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShowTimingsWithDate extends AppCompatActivity {

    TextView textViewDateShow;
    ViewPager viewPager;

    Button prev,next;

     ArrayList<String> arrayListDates=getDates();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    static String currentDate="";
    ExpandableListAdapter expandableListAdapter=null;

    SharedPreferences sharedPreferences,sharedPref1;
    SharedPreferences.Editor editor;

    HashMap<String,String[]> hashMapTimeSlots=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timings_with_date);

        textViewDateShow=(TextView)findViewById(R.id.textViewDates);
        viewPager=(ViewPager)findViewById(R.id.viewpager);

        prev=(Button)findViewById(R.id.prevButton);
        next=(Button)findViewById(R.id.nextButton);

        prev.setEnabled(false);
        /*prev.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);*/
        currentDate=getCurrentDate();
        MyViewPagerAdapter myViewPagerAdapter=new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        if(arrayListDates!=null){
            textViewDateShow.setText(arrayListDates.get(0));
        }




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textViewDateShow.setText(arrayListDates.get(position));

                if (position == 0) {
                    // prev.setVisibility(View.GONE);
                    prev.setEnabled(false);
                } else {
                    prev.setVisibility(View.VISIBLE);
                    prev.setEnabled(true);
                }

                if (position == arrayListDates.size() - 1) {
                    next.setVisibility(View.GONE);
                    next.setEnabled(false);
                } else {
                    next.setVisibility(View.VISIBLE);
                    next.setEnabled(true);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = getItem(+1);
                System.out.println("item :" + item);
                if (item < arrayListDates.size()) {
                    viewPager.setCurrentItem(item);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current=getItem(-1);
                System.out.println(current);
                if(current>=0){
                    viewPager.setCurrentItem(current);
                }
            }
        });


        getTimeSlotsFromServer(arrayListDates.get(0).toString(), arrayListDates.get(arrayListDates.size() - 1).toString());




    }

    private void getTimeSlotsFromServer(String start_date, String end_date) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching Slots..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        sharedPreferences=this.getSharedPreferences(SharedPrefManager.getTempAppointmentProf(),0);
        String doc_id=sharedPreferences.getString("doc_id",null);
        sharedPref1=this.getSharedPreferences(LoginActivity.getSharedProfile(), 0);
        String apikey=sharedPref1.getString("apikey",null);

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("apikey", apikey);
        hashMap.put("start_date", start_date);
        hashMap.put("end_date", end_date);
        hashMap.put("doc_id", doc_id);

        JSONObject jsonObject=new JSONObject(hashMap);

        JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.POST,ApiUrls.getGetTimeSlots(), jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                    progressDialog.dismiss();
                    responseFromServer(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(ShowTimingsWithDate.this, "Connection Error!Try Again", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonRequest);
    }

    private void responseFromServer(JSONObject jsonObject) {
        try {
            System.out.println(jsonObject);
            if(jsonObject.getBoolean("success")){
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                if(jsonArray.length()>0){
                    hashMapTimeSlots=new HashMap<>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject childObj=jsonArray.getJSONObject(i);
                        int len=childObj.getJSONArray("app_time").length();
                        String[] newslot=new String[len];
                        JSONArray childarray=childObj.getJSONArray("app_time");
                        for(int j=0;j<len;j++){
                            newslot[j]=childarray.get(j).toString();
                        }
                        hashMapTimeSlots.put(childObj.getString("app_date"),newslot);
                    }
                    System.out.println(hashMapTimeSlots);
                }
                else {
                    hashMapTimeSlots=null;
                }
            }
            else {


                String msgg=jsonObject.getString("msg");

                Toast.makeText(ShowTimingsWithDate.this, ""+msgg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDate() {
        Calendar calendar=Calendar.getInstance();
        calendar.set(calendar.DATE,calendar.get(Calendar.DATE));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
        calendar.add(Calendar.DATE, 0);
        String datee=simpleDateFormat.format(calendar.getTime());

        return datee;
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem()+i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_timings_with_date, menu);
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


    public class MyViewPagerAdapter extends PagerAdapter{

        ArrayList<String> dateArrays=getDates();

        LayoutInflater layoutInflater;

        TextView textViewTime;


        @Override
        public int getCount() {
            return dateArrays.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View view = layoutInflater.inflate(R.layout.timeslotsview, container, false);

                textViewTime=(TextView)view.findViewById(R.id.textViewSlot);
                //textViewTime.setText("Selected :"+position);
            expListView=(ExpandableListView)view.findViewById(R.id.expandListView);
            prepareListData();

            expandableListAdapter=new ExpandableListAdapter(getApplicationContext(),listDataHeader,listDataChild);

            expListView.setAdapter(expandableListAdapter);

            expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    switch (groupPosition){
                        case 0:
                            int currentpost=viewPager.getCurrentItem();
                            String date=arrayListDates.get(currentpost).toString();
                            String time=expandableListAdapter.getChild(groupPosition,childPosition).toString();
                            Toast.makeText(getApplicationContext(), "Date :"+date+"\n"+"Time :"+time, Toast.LENGTH_SHORT).show();
                            return true;
                        case 1:
                            int currentpost1=viewPager.getCurrentItem();
                            String date1=arrayListDates.get(currentpost1).toString();
                            String time1=expandableListAdapter.getChild(groupPosition,childPosition).toString();
                            Toast.makeText(getApplicationContext(), "Date :" + date1 + "\n" + "Time :" + time1, Toast.LENGTH_SHORT).show();
                            return true;
                        case 2:
                            int currentpost2=viewPager.getCurrentItem();
                            String date2=arrayListDates.get(currentpost2).toString();
                            String time2=expandableListAdapter.getChild(groupPosition,childPosition).toString();
                            Toast.makeText(getApplicationContext(), "Date :"+date2+"\n"+"Time :"+time2, Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
            });


            
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Morning");
        listDataHeader.add("Afternoon");
        listDataHeader.add("Evening");

        // Adding child data
        List<String> morningList = new ArrayList<String>();

        morningList.add("10:00 AM");
        morningList.add("10:15 AM");
        morningList.add("10:30 AM");
        morningList.add("10:45 AM");
        morningList.add("11:00 AM");
        morningList.add("11:15 AM");
        morningList.add("11:30 AM");
        morningList.add("11:45 AM");


        List<String> noonList = new ArrayList<String>();
        noonList.add("12:00 PM");
        noonList.add("12:15 PM");
        noonList.add("12:30 PM");
        noonList.add("12:45 PM");
        noonList.add("01:00 PM");
        noonList.add("01:15 PM");

        List<String> evenongList = new ArrayList<String>();
        evenongList.add("05:00 PM");
        evenongList.add("05:15 PM");
        evenongList.add("05:30 PM");
        evenongList.add("05:45 PM");
        evenongList.add("06:00 PM");
       /* evenongList.add("09:15 PM");
        evenongList.add("09:30 PM");*/
      //  evenongList.add("11:50 PM");

        listDataChild.put(listDataHeader.get(0), morningList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), noonList);
        listDataChild.put(listDataHeader.get(2), evenongList);

    }


    private ArrayList<String> getDates(){
        ArrayList<String> arrayList = new ArrayList<String>();

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);

        Calendar calendar=Calendar.getInstance();
        /*calendar.set(calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK));
        calendar.set(calendar.DAY_OF_WEEK_IN_MONTH,calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));*/

        /*calendar.set(calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(calendar.DATE,calendar.get(Calendar.DATE));

*/


        for(int i=0;i<7;i++){
            if(i==0){
                calendar.add(Calendar.DATE,0);
                String day=simpleDateFormat.format(calendar.getTime());

                System.out.println(day);
                arrayList.add(i,day);
            }
            else {
                calendar.add(Calendar.DATE, 1);
                String day = simpleDateFormat.format(calendar.getTime());

                System.out.println(day);
                arrayList.add(i, day);
            }

        }



        return arrayList;
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter{

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;
        ArrayList<List<String>> arrayList=new ArrayList<>();
        List<String> arrayListChild=null;

        public ExpandableListAdapter(Context _context, List<String> _listDataHeader, HashMap<String, List<String>> _listDataChild) {
            this._context = _context;
            this._listDataHeader = _listDataHeader;
            this._listDataChild = _listDataChild;
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            String headerTitle=(String)getGroup(groupPosition);
                LayoutInflater layoutInflater=(LayoutInflater)this._context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView=layoutInflater.inflate(R.layout.timegridviewchilds,null);

            TextView textView=(TextView)convertView.findViewById(R.id.textViewHeader);
            textView.setText(headerTitle);


            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String childTitle=(String)getChild(groupPosition, childPosition);

                LayoutInflater layoutInflater=(LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=layoutInflater.inflate(R.layout.timegridchilditem,null);


            Button textView=(Button)convertView.findViewById(R.id.textViewgriditem);
            textView.setText(childTitle);
            textView.setTextColor(Color.BLACK);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentpost1=viewPager.getCurrentItem();
                    String date1=arrayListDates.get(currentpost1).toString();
                    String time1=expandableListAdapter.getChild(groupPosition,childPosition).toString();
                    //Toast.makeText(getApplicationContext(), "Date :" + date1 + "\n" + "Time :" + time1, Toast.LENGTH_SHORT).show();
                    showDialogOption(date1,time1);
                }
            });


            currentDate=getCurrentDate();
            if(viewPager.getCurrentItem()==0){
                SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                try {
                    Date d1=dateFormat.parse(childTitle);
                    System.out.println(d1);
                    Date d2=dateFormat.parse(currentDate);
                    System.out.println(d2);
                    if(d1.before(d2) || d1.equals(d2)) {
                        textView.setEnabled(false);
                        textView.setTextColor(Color.RED);
                        textView.setClickable(false);

                        System.out.println("less");


                       // textView.setVisibility(View.GONE);
                        //


                    }
                    else {

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(hashMapTimeSlots!=null){
                int size=hashMapTimeSlots.size();
                int viewpag=viewPager.getCurrentItem();
                String curdate=arrayListDates.get(viewPager.getCurrentItem());
                System.out.println("Curr  date :"+curdate);
                SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                if(hashMapTimeSlots.containsKey(curdate.toString())){
                    System.out.println("contains");
                    String[] getArray=hashMapTimeSlots.get(curdate);
                    for (int tt=0;tt<getArray.length;tt++){
                        try {
                            Date d1=dateFormat.parse(childTitle);
                            System.out.println(d1);
                            Date d2=dateFormat.parse(getArray[tt]);
                            System.out.println(d2);
                            if(d1.equals(d2)) {
                                textView.setEnabled(false);
                                textView.setTextColor(Color.RED);
                                textView.setClickable(false);

                                System.out.println("less");


                                // textView.setVisibility(View.GONE);
                                //


                            }
                            else {

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }






            /*GridView gridView=(GridView)convertView.findViewById(R.id.gridViewSlotItem);


            if(childPosition==0){
                arrayListChild=new ArrayList<>();
            }

            arrayList.add(_listDataChild.get(_listDataHeader.get(groupPosition)));
           if(childPosition==getChildrenCount(groupPosition)-1){
               System.out.println("Child position :" + childPosition);
               GridViewAdapter gridViewAdapter=new GridViewAdapter(arrayList.get(groupPosition));
               gridView.setAdapter(gridViewAdapter);
           }*/





            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }


    }

    private void showDialogOption(final String date1, final String time1) {
        sharedPreferences=this.getSharedPreferences(SharedPrefManager.getTempAppointmentProf(),0);
        sharedPref1=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);
        final String doc_id=sharedPreferences.getString("doc_id",null);
        String doc_name=sharedPreferences.getString("doc_name",null);
        final String user_id=sharedPref1.getString("login_id",null);
        String spec=sharedPreferences.getString("spec",null);
        final String patient_name=sharedPreferences.getString("name",null);
        final String patient_email=sharedPreferences.getString("email",null);
        final String patient_contact=sharedPreferences.getString("mobile",null);
        final String patient_gender=sharedPreferences.getString("gender",null);
        final String patient_age=sharedPreferences.getString("age",null);

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Do you Confirm to book Appointment?");
        alert.setMessage("Appointment Date :" + date1 + "\n" + "Appointment Time :" + time1 + "\n\n" + "Doctor :" + doc_name + "\n" + "Specialization :" + spec);
        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                bookAppointment(date1, time1, patient_name, patient_email, patient_contact, patient_gender, user_id, doc_id,patient_age);
            }
        });
        alert.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=alert.create();
        dialog.show();
    }

    private void bookAppointment(String app_date, String app_time, String patient_name, String patient_email, String patient_contact, String patient_gender, String user_id, String doc_id, String patient_age) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Booking Appointment..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String,String> appointMap=new HashMap<>();
        appointMap.put("app_date",app_date);
        appointMap.put("user_id",user_id);
        appointMap.put("doc_id",doc_id);
        appointMap.put("app_time",app_time);
        appointMap.put("patient_name",patient_name);
        appointMap.put("patient_email", patient_email);
        appointMap.put("patient_contact", patient_contact);
        appointMap.put("patient_gender", patient_gender);
        appointMap.put("patient_age",patient_age);

        System.out.println(appointMap);


        JSONObject jsonObj=new JSONObject(appointMap);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,ApiUrls.getBookAppointment(), jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                showResponse(jsonObject);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(ShowTimingsWithDate.this, "Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void showResponse(JSONObject jsonObject) {
        try {
            System.out.println(jsonObject);
            if(jsonObject.getBoolean("success")){
                showSuccessDialog();
            }
            else {
                showErrorDialog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showErrorDialog() {
        /*LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view=layoutInflater.inflate(R.layout.successdialog, null);*/
/*
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.errorappointment);

        dialog.setCancelable(false);
        Button button=(Button)dialog.findViewById(R.id.buttonok1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();*/

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Sorry Your appointment has not been confirmed ");
        alert.setMessage("Note:Only 2 Appointments are allowed to user, for particular date.\nEnsure previous appointments payment are not pending.\n ");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=alert.create();
        dialog.show();


    }

    private void showSuccessDialog() {

        /*final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.successdialog);

        dialog.setCancelable(false);
        Button button=(Button)dialog.findViewById(R.id.buttonok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences=getApplicationContext().getSharedPreferences(SharedPrefManager.getTempAppointmentProf(),0);
                sharedPreferences.edit().clear().commit();
                Intent intent=new Intent(ShowTimingsWithDate.this,HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        dialog.show();*/

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Thanks");
        alert.setMessage("Your Appointment has been Confirmed");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sharedPreferences = getApplicationContext().getSharedPreferences(SharedPrefManager.getTempAppointmentProf(), 0);
                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(ShowTimingsWithDate.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        /*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        AlertDialog dialog=alert.create();
        dialog.show();



    }

    public class GridViewAdapter extends BaseAdapter{

        List<String> arrayList=null;

        public GridViewAdapter(List<String> arrayList) {
            this.arrayList = arrayList;
        }

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

            View view=convertView;
            if(view==null){
                LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
                view=layoutInflater.inflate(R.layout.timegridchilditem,null);
            }

            TextView textView=(TextView)view.findViewById(R.id.textViewgriditem);
            textView.setText(arrayList.get(position));
            return view;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTimeSlotsFromServer(arrayListDates.get(0).toString(), arrayListDates.get(arrayListDates.size() - 1).toString());

    }
}
