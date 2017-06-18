package dev.pardeep.healthappointment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.pardeep.healthappointment.Fragments.ChooseDoctorFragment;

public class BookAppointmentAct extends AppCompatActivity {

    EditText editTextUserName,editTextUserEmail,editTextPhone,editTextAge;
    RadioGroup radioGroup;
    Button buttonNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);


        showAppointMentDialog();

        editTextUserName=(EditText)findViewById(R.id.username);
        editTextUserEmail=(EditText)findViewById(R.id.emailid);
        editTextPhone=(EditText)findViewById(R.id.mobilenumber);
        editTextAge=(EditText)findViewById(R.id.userage);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);

        buttonNext=(Button)findViewById(R.id.proceednextbutton);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    startFragment(new ChooseDoctorFragment());
                }
            }
        });


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
