package dev.pardeep.healthappointment.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.pardeep.healthappointment.HomeScreen;
import dev.pardeep.healthappointment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorFragment extends Fragment {


    public ErrorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        getFragmentManager().popBackStack();
        Intent intent=new Intent(getActivity(), HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
