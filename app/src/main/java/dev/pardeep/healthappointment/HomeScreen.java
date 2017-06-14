package dev.pardeep.healthappointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button menuButton;

    ListView listViewMain;

    private static ArrayList<HomeListContent> arrayList=null;

    private static String[] titles={"Book an Appointment","View/Cancel Appointment","Ask any Query","Lab Reports"};

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
                PopupMenu popupMenu=new PopupMenu(HomeScreen.this,menuButton);

                popupMenu.getMenuInflater().inflate(R.menu.home_menu_side,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
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
                        startActivity(new Intent(HomeScreen.this,BookAppointmentAct.class));
                        break;
                    default:
                }

            }
        });

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

        if(sharedPreferences.getBoolean("login",false)){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        else {
            super.onBackPressed();
        }
    }
}
