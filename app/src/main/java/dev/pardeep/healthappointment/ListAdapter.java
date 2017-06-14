package dev.pardeep.healthappointment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pardeep on 08-06-2017.
 */
public class ListAdapter extends BaseAdapter {

    private static ArrayList<HomeListContent> arrayList=null;

    public static ArrayList<HomeListContent> getArrayList() {
        return arrayList;
    }

    public static void setArrayList(ArrayList<HomeListContent> arrayList) {
        ListAdapter.arrayList = arrayList;
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

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.list_adapter, parent, false);

        TextView titleTextView=(TextView)view.findViewById(R.id.mainTitle);
        TextView descriptionTextView=(TextView)view.findViewById(R.id.description);
        ImageView imageView=(ImageView)view.findViewById(R.id.icon);

        titleTextView.setText(arrayList.get(position).getListTitle());
        descriptionTextView.setText(arrayList.get(position).getListContent());
        imageView.setImageResource(arrayList.get(position).getIcon());

        return view;
    }
}
