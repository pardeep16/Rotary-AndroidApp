package dev.pardeep.healthappointment.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import dev.pardeep.healthappointment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseDoctorFragment extends Fragment implements View.OnClickListener {

    private String[] arrayCategories={"Select Category--","Dentist","Heart Specialist","General OPD"};
    private String[] arrayDoctorsList={"Select Doctor--"};

    Spinner categorieslist,doctorslist;

    Button buttonToBack,submitNextButton;

    View view;


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

        ArrayAdapter<String> arrayAdapterCategory=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayCategories);
        ArrayAdapter<String> arrayAdapterDoctors=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,arrayDoctorsList);

        categorieslist.setAdapter(arrayAdapterCategory);
        doctorslist.setAdapter(arrayAdapterDoctors);

        buttonToBack=(Button)view.findViewById(R.id.backButton);
        submitNextButton=(Button)view.findViewById(R.id.nextButton);
        buttonToBack.setOnClickListener(this);
        submitNextButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                onDestroy();
                break;
            case R.id.nextButton:
                openNextPage();
                break;
            default:
        }
    }

    private void openNextPage() {
        if(categorieslist.getSelectedItemPosition()==0){

        }
    }
}
