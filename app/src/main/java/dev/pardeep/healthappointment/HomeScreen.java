package dev.pardeep.healthappointment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import dev.pardeep.healthappointment.Fragments.ProfileFragment;

public class HomeScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button menuButton;

    ListView listViewMain;

    private static ArrayList<HomeListContent> arrayList=null;

    private static String[] titles={"Book an OPD","View/Cancel Appointment","Ask any Query","Lab Reports"};

    private static int[] icons={R.drawable.calender,R.drawable.cancelsub,R.drawable.askques,R.drawable.labreport};

    private static String[] description={"New/Existing Patients book your appointment","Manage your previous appointments","Get answers from doctors and experts","Upload bills, prescriptions and more"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        listViewMain=(ListView)findViewById(R.id.listView);

        arrayList=new ArrayList<>();

        for (int i=0;i<titles.length;i++){
            arrayList.add(new HomeListContent(titles[i],icons[i],description[i]));

        }

        ListAdapter listAdapter=new ListAdapter();
        ListAdapter.setArrayList(arrayList);

        listViewMain.setAdapter(listAdapter);

        sharedPreferences=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);

        menuButton=(Button)findViewById(R.id.buttonmenu);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(HomeScreen.this, menuButton);

                popupMenu.getMenuInflater().inflate(R.menu.home_menu_side, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.profile:
                                showProfileFrag(new ProfileFragment());
                                break;
                            case R.id.signout:
                                logOut();
                                break;

                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });


        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                      //  startActivity(new Intent(HomeScreen.this,BookAppointmentAct.class));
                        startActivity(new Intent(HomeScreen.this,SelectProfileForOpd.class));

                        break;
                    case 1:
                        startActivity(new Intent(HomeScreen.this,ViewAppointmentsAct.class));
                        break;
                    default:
                }

            }
        });

        if(sharedPreferences.getString("phone",null).toString().equalsIgnoreCase("null") || sharedPreferences.getString("phone",null).toString().length()<=0){
            //changeFragment(new CompleteProfileGmail());
            startActivity(new Intent(HomeScreen.this,MobileVerifyActivity.class));
        }
        else if(!sharedPreferences.contains("gmail_token") || sharedPreferences.getString("emailid",null).toString().equalsIgnoreCase("null")){


        }
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout,fragment).addToBackStack("fragmentphone").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }

    private void showProfileFrag(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout,fragment).addToBackStack("fragment").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    private void logOut() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("SignOut");
        alertDialog.setIcon(R.drawable.logout);
        alertDialog.setMessage("Do you Confirm to SignOut?");
        alertDialog.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeAllData();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert=alertDialog.create();
        alert.show();

    }

    private void removeAllData() {
        try {
            SharedPreferences sharedPreferences;
            sharedPreferences=this.getSharedPreferences(LoginActivity.getSharedProfile(),0);
            sharedPreferences.edit().clear().commit();

            sharedPreferences=this.getSharedPreferences(SharedPrefManager.getSaveprofile(),0);
            sharedPreferences.edit().clear().commit();

            Intent intent=new Intent(HomeScreen.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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

    @Override
    public void onBackPressed() {


        super.onBackPressed();
        /*int val = getSupportFragmentManager().getBackStackEntryCount();
        if(val>=1){
            getSupportFragmentManager().popBackStack();
            return;
        }
        if(sharedPreferences.getBoolean("login", false)){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            startActivity(a);
        }*/
        /*else {

        }*/

    }
}
