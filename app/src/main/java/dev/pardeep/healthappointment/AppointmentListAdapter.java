package dev.pardeep.healthappointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gurwinder on 14-Jun-17.
 */

public class AppointmentListAdapter extends BaseAdapter {

    private static ArrayList<AppointmentsListContent> arrayList=null;

    public static ArrayList<AppointmentsListContent> getArrayList() {
        return arrayList;
    }

    public static void setArrayList(ArrayList<AppointmentsListContent> arrayList) {
        AppointmentListAdapter.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.appointmentlist_adapter, parent, false);
        TextView appointment_id=(TextView)view.findViewById(R.id.app_id);
        TextView doc_name=(TextView)view.findViewById(R.id.doc_name);
        TextView doc_contact=(TextView)view.findViewById(R.id.contact);
        TextView app_date=(TextView)view.findViewById(R.id.app_date);
        TextView department=(TextView)view.findViewById(R.id.department);
        appointment_id.setText(arrayList.get(position).getApp_id());
        doc_name.setText(arrayList.get(position).getDoc_name());
        doc_contact.setText(arrayList.get(position).getDoc_name());
        app_date.setText((CharSequence) arrayList.get(position).getApp_date());
        department.setText(arrayList.get(position).getDepartment());
        return view;
    }
}
