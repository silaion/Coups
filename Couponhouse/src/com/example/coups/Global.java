package com.example.coups;

import android.app.Activity;
import android.app.Application;

        import java.util.ArrayList;

/**
 * Created by windy on 2014-10-18.
 */
public class Global extends Application{
    public boolean start = false;
    static String name, gender, phoneNum, birth, change = "1";
    static String c_Number = "1";
    static String s_Number;
    static String imsi_string1, imsi_string2;
    static ArrayList<Activity> actList = new ArrayList<Activity>();
}
