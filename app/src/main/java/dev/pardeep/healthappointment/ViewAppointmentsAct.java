package dev.pardeep.healthappointment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import static dev.pardeep.healthappointment.R.color.icons;
import static dev.pardeep.healthappointment.R.id.description;

public class ViewAppointmentsAct extends AppCompatActivity {

    ListView listViewMain;
    private static ArrayList<AppointmentsListContent> arrayList=null;
    private static String[] doc_contact,app_id,doc_name,department;
    private static Date[] app_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        listViewMain=(ListView)findViewById(R.id.listView);
        arrayList=new ArrayList<>();

        for (int i=0;i<app_id.length;i++){
            arrayList.add(new AppointmentsListContent(doc_contact[i],app_id[i],doc_name[i],department[i],app_date[i]));
        }

        AppointmentListAdapter listAdapter=new AppointmentListAdapter();
        AppointmentListAdapter.setArrayList(arrayList);
        listViewMain.setAdapter(listAdapter);

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
