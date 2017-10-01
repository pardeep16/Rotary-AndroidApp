package dev.pardeep.healthappointment.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.pardeep.healthappointment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContinuePhoneSignin extends Fragment {

    View view;

    public ContinuePhoneSignin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_continue_phone_signin, container, false);

        return view;
    }


}
