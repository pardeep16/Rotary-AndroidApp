package dev.pardeep.healthappointment;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.pardeep.healthappointment.Fragments.ChooseDoctorFragment;

public class BookAppointmentAct extends AppCompatActivity {

    EditText editTextUserName,editTextUserEmail,editTextPhone,editTextAge;
    RadioGroup radioGroup;
    Button buttonNext;

    RelativeLayout relativeLayout;

    LinearLayout linearLayout;

    SharedPreferences sharedPreferences,sharedPreff;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPatientProf;

    TextView textViewPatientId;

    RadioButton maleRadioButton,femaleRadioButton,otherRadioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        maleRadioButton=(RadioButton)findViewById(R.id.radioButtonmale);
        femaleRadioButton=(RadioButton)findViewById(R.id.radioButtonfemale);
        otherRadioButton=(RadioButton)findViewById(R.id.radioButtonother);




        sharedPreferences=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);

        sharedPatientProf=this.getSharedPreferences(SharedPrefManager.getAppointmentPatientProf(),0);




        final Bundle data=getIntent().getExtras();

        textViewPatientId=(TextView)findViewById(R.id.patientId);







        editTextUserName=(EditText)findViewById(R.id.username);
        editTextUserEmail=(EditText)findViewById(R.id.emailid);
        editTextPhone=(EditText)findViewById(R.id.mobilenumber);
        editTextAge=(EditText)findViewById(R.id.userage);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);

        buttonNext=(Button)findViewById(R.id.proceednextbutton);

        System.out.println(sharedPreferences.getAll().toString());

        Button buttonback=(Button)findViewById(R.id.backButton);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(data.containsKey("profile") && data.getBoolean("profile")){


            editTextUserName.setText(data.getString("p_name",null));
            if (!sharedPreferences.getString("emailid", null).equalsIgnoreCase("null")) {
                editTextUserEmail.setText(sharedPreferences.getString("emailid", null));
                editTextUserEmail.setEnabled(false);
            }
            if (!sharedPreferences.getString("phone", null).toString().equalsIgnoreCase("null")) {
                editTextPhone.setText(sharedPreferences.getString("phone", null));
                editTextPhone.setEnabled(false);
            }

            editTextAge.setText(data.getString("p_age",null));
            textViewPatientId.setText("Patient ID:- "+data.getString("p_id",null));

            String gend=data.getString("p_sex",null);

            gend=gend.toLowerCase();
            switch (gend){
                case "male":
                    int id=maleRadioButton.getId();
                    radioGroup.check(id);
                    break;
                case "female":
                    int id1=femaleRadioButton.getId();
                    radioGroup.check(id1);
                    break;
                case "other":
                    int id2=otherRadioButton.getId();
                    radioGroup.check(id2);
                    break;
                default:

            }


        }
        else {


            if (!sharedPreferences.getString("name", null).equalsIgnoreCase("null")) {
                editTextUserName.setText(sharedPreferences.getString("name", null));
            }
            if (!sharedPreferences.getString("emailid", null).equalsIgnoreCase("null")) {
                editTextUserEmail.setText(sharedPreferences.getString("emailid", null));
                editTextUserEmail.setEnabled(false);
            }
            if (!sharedPreferences.getString("phone", null).toString().equalsIgnoreCase("null")) {
                editTextPhone.setText(sharedPreferences.getString("phone", null));
                editTextPhone.setEnabled(false);
            }

            textViewPatientId.setText("New Patient");
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.getText().toString().equalsIgnoreCase("")) {
                    editTextUserName.setError("Enter your name");
                } else if (!validateEmail(editTextUserEmail.getText().toString())) {
                    editTextUserEmail.setError("Enter your email");
                } else if (editTextPhone.getText().toString().length() != 10) {
                    editTextPhone.setError("Enter correct mob number");
                } else if (editTextAge.getText().toString().equalsIgnoreCase("")) {
                    editTextAge.setError("Incorrect Age");
                } else {
                    sharedPreff=getApplicationContext().getSharedPreferences(SharedPrefManager.getTempAppointmentProf(),0);
                    editor=sharedPreff.edit();
                    editor.putBoolean("bookapp",false);
                    editor.putString("name",editTextUserName.getText().toString());
                    editor.putString("email",editTextUserEmail.getText().toString().trim());
                    editor.putString("mobile",editTextPhone.getText().toString().trim());
                    editor.putString("age",editTextAge.getText().toString().trim());
                    if(textViewPatientId.getText().toString().equalsIgnoreCase("New Patient")){
                        editor.putString("p_id",null);
                    }
                    else {
                       editor.putString("p_id",data.getString("p_id"));
                    }

                    switch (radioGroup.getCheckedRadioButtonId()){
                        case R.id.radioButtonmale:
                            editor.putString("gender","male");
                            break;
                        case R.id.radioButtonfemale:
                            editor.putString("gender","female");
                            break;
                        case R.id.radioButtonother:
                            editor.putString("gender","other");
                            break;
                        default:
                            editor.putString("gender","");
                    }
                    editor.commit();
                    startFragment(new ChooseDoctorFragment());
                }
            }
        });

        linearLayout=(LinearLayout)findViewById(R.id.linearlayoutmain);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return true;
            }
        });

        /*editTextUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        editTextPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });
        editTextAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });*/

        showAppointMentDialog();





    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

    private void startFragment(Fragment chooseDoctorFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,chooseDoctorFragment).addToBackStack("fragment").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    private void showAppointMentDialog() {
        final Dialog  dialog=new Dialog(this);
        dialog.setContentView(R.layout.appoint_dialog);
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button button=(Button)dialog.findViewById(R.id.proceedbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_appointment, menu);
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
