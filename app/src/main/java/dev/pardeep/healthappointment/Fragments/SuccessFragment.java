package dev.pardeep.healthappointment.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import dev.pardeep.healthappointment.HomeScreen;
import dev.pardeep.healthappointment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {

    static JSONObject jsonObject;
    TextView opd,patient,payment;
    public SuccessFragment() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_success, container, false);

        opd=(TextView)view.findViewById(R.id.opdid);
        patient=(TextView)view.findViewById(R.id.patientid);
        payment=(TextView)view.findViewById(R.id.paymentid);


        boolean status=getArguments().getBoolean("status");
        if(status){
            String opd_id=getArguments().getString("opd_id");
            String p_id=getArguments().getString("p_id");
            String pay_id=getArguments().getString("payment_id");
            opd.setText("OPD ID :"+opd_id);
            patient.setText("Patient ID:-"+p_id);
            payment.setText("Payment ID:"+pay_id);
        }

        return view;
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
