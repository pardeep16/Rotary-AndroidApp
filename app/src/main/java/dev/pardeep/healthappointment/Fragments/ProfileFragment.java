package dev.pardeep.healthappointment.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.pardeep.healthappointment.LoginActivity;
import dev.pardeep.healthappointment.R;
import dev.pardeep.healthappointment.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText editTextUserName,editTextUserEmail,editTextPhone,editTextAge;
    RadioGroup radioGroup;
    Button saveButton,editButton;
    RadioButton maleRadioB,femaleRadioB,otherRadioB;

    SharedPreferences sharedPreferences,sharedpref1;
    SharedPreferences.Editor editor,editor1;

    View view;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_profile, container, false);
        editTextUserName=(EditText)view.findViewById(R.id.username);
        editTextUserEmail=(EditText)view.findViewById(R.id.emailid);
        editTextPhone=(EditText)view.findViewById(R.id.mobilenumber);
        editTextAge=(EditText)view.findViewById(R.id.userage);
        radioGroup=(RadioGroup)view.findViewById(R.id.radiogroup);

        maleRadioB=(RadioButton)view.findViewById(R.id.radioButtonmale);
        femaleRadioB=(RadioButton)view.findViewById(R.id.radioButtonfemale);
        otherRadioB=(RadioButton)view.findViewById(R.id.radioButtonother);

        saveButton=(Button)view.findViewById(R.id.proceednextbutton);
        editButton=(Button)view.findViewById(R.id.buttonEditProfile);

        sharedPreferences=getActivity().getSharedPreferences(LoginActivity.getSharedProfile(),0);

        if(sharedPreferences.contains("gmail_token")){
            editTextUserName.setText(sharedPreferences.getString("name",null));
            editTextUserEmail.setText(sharedPreferences.getString("emailid",null));


        }
        else{
            editTextPhone.setText(sharedPreferences.getString("phone",null));
        }

        saveButton.setEnabled(false);

        sharedpref1=getActivity().getSharedPreferences(SharedPrefManager.getSaveprofile(),0);

        if(sharedPreferences.contains("saveprofile")){
            System.out.println("saveee");

            if (sharedPreferences.getBoolean("saveprofile",false)){
                editTextUserName.setText(sharedpref1.getString("name",null));
                editTextUserEmail.setText(sharedpref1.getString("emailid",null));
                editTextPhone.setText(sharedpref1.getString("phone",null));
                editTextAge.setText(sharedpref1.getString("age",null));
                String gg=sharedpref1.getString("gender",null);

                System.out.println(sharedpref1.getAll().toString());
                switch (gg.toString()){
                    case "male":
                        maleRadioB.setEnabled(true);
                        femaleRadioB.setEnabled(false);
                        otherRadioB.setEnabled(false);
                        break;
                    case "female":
                        femaleRadioB.setEnabled(true);
                        maleRadioB.setEnabled(false);
                        otherRadioB.setEnabled(false);
                        break;
                    case "other":
                        otherRadioB.setEnabled(true);
                        maleRadioB.setEnabled(false);
                        femaleRadioB.setEnabled(false);
                        break;
                    default:

                }
            }
            else {
                radioGroup.setEnabled(false);
                maleRadioB.setEnabled(false);
                femaleRadioB.setEnabled(false);
                otherRadioB.setEnabled(false);
            }

        }

        editTextUserName.setEnabled(false);
        editTextUserEmail.setEnabled(false);
        editTextPhone.setEnabled(false);

        editTextAge.setEnabled(false);
       // radioGroup.setEnabled(false);




        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveButton.setEnabled(true);
                radioGroup.setEnabled(true);
                maleRadioB.setEnabled(true);
                femaleRadioB.setEnabled(true);
                otherRadioB.setEnabled(true);

                editTextUserName.setEnabled(true);
                editTextAge.setEnabled(true);
                radioGroup.setEnabled(true);
                editTextPhone.setEnabled(true);

                editTextUserName.setFocusable(true);

               /* if (sharedPreferences.getString("phone",null).toString().length()>0) {
                    editTextPhone.setEnabled(false);
                } else {
                    editTextPhone.setEnabled(true);
                }*/

                if(sharedPreferences.getString("phone",null).toString().length()==10){
                    editTextPhone.setEnabled(false);
                }
                else {
                    editTextPhone.setEnabled(true);
                }
                if (sharedPreferences.contains("gmail_token")) {
                    if (sharedPreferences.getString("gmail_token", null).length() > 0) {
                        editTextUserEmail.setEnabled(false);

                    } else {
                        editTextUserEmail.setEnabled(true);
                    }
                } else {
                    editTextUserEmail.setEnabled(true);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editButton.setEnabled(false);
                if(editTextUserName.getText().toString().equalsIgnoreCase("")){
                    editTextUserName.setError("Enter your name");
                }
                else if(!validateEmail(editTextUserEmail.getText().toString())){
                    editTextUserEmail.setError("Enter your email");
                }
                else if(editTextPhone.getText().toString().length()!=10){
                    editTextPhone.setError("Enter correct mob number");
                }
                else if(editTextAge.getText().toString().equalsIgnoreCase("")){
                    editTextAge.setError("Incorrect Age");
                }
                else {
                    sharedPreferences=getActivity().getSharedPreferences(LoginActivity.getSharedProfile(), 0);
                    editor=sharedPreferences.edit();

                    editor.putBoolean("saveprofile", true);
                    editor.commit();

                    sharedpref1=getActivity().getSharedPreferences(SharedPrefManager.getSaveprofile(), 0);
                    editor1=sharedpref1.edit();
                    editor1.putString("name",editTextUserName.getText().toString());
                    editor1.putString("age",editTextAge.getText().toString());
                    switch (radioGroup.getCheckedRadioButtonId()){
                        case R.id.radioButtonmale:
                            editor1.putString("gender","male");
                            break;
                        case R.id.radioButtonfemale:
                            editor1.putString("gender","female");
                            break;
                        case R.id.radioButtonother:
                            editor1.putString("gender","other");
                            break;
                        default:
                            editor1.putString("gender","");

                    }
                    if(!editTextPhone.isEnabled()){
                        editor1.putString("phone",editTextPhone.getText().toString());
                    }
                    else {
                       verifyNumber(editTextPhone.getText().toString());
                    }
                    if(sharedPreferences.contains("gmail_token") && sharedPreferences.getString("gmail_token",null).toString().length()>0){
                        editor1.putString("emailid",sharedPreferences.getString("emailid",null));
                    } else {
                        verifyEmailAddress(editTextUserEmail.getText().toString());
                    }


                    editor1.commit();

                    editTextUserName.setEnabled(false);
                    editTextUserEmail.setEnabled(false);
                    editTextPhone.setEnabled(false);

                    editTextAge.setEnabled(false);
                    radioGroup.setEnabled(false);
                    Toast.makeText(getActivity(), "Profile Saved!", Toast.LENGTH_SHORT).show();
                    editButton.setEnabled(true);
                    saveButton.setEnabled(false);

                }
            }

        });







        return view;
    }

    private void verifyEmailAddress(String s) {
        return;
    }

    private void verifyNumber(String s) {
        return;
    }

    private boolean validateEmail(String email) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
