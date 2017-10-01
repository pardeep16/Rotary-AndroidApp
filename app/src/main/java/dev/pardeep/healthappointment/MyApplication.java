package dev.pardeep.healthappointment;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.instamojo.android.Instamojo;

/**
 * Created by pardeep on 21-08-2017.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
        Instamojo.initialize(this);
    }
}
