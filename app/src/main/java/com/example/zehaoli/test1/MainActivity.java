package com.example.zehaoli.test1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {


    public static final String DIR="http://hw9testserver-env.us-east-2.elasticbeanstalk.com/?";

    private LocationManager mLocationManager;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            originlat=location.getLatitude();
            originlon=location.getLongitude();
            locationavailable=true;
            //Toast.makeText(getApplicationContext(),"hello there",Toast.LENGTH_SHORT).show();
            System.out.println(location);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }



    };
    public static boolean locationavailable=false;
    public static double originlat;
    public static double originlon;
    private static final int REQUEST = 112;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private int[] tabIcons = {
            R.drawable.search,
            R.drawable.heart_fill_white
    };

    private int[] activetabIcons = {
            R.drawable.search,
            R.drawable.heart_fill_red
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);



        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("SEARCH");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search,0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("FAVORITES");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_fill_white,0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);


        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.separator));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        //noinspection deprecation
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
            // called when tab selected
                int position = tab.getPosition();

                if (position==0){
                }
                else{
                }

            }

            @Override
            public void onTabUnselected(Tab tab) {
            // called when tab unselected
                int position = tab.getPosition();

                if (position==0){
                }
                else{
                }
            }

            @Override
            public void onTabReselected(Tab tab) {
            // called when a tab is reselected
            }
        });


        //location get method
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION};
            if (!hasPermissions(this.getApplicationContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST );
            } else {
                //call get location here
                getlocation();
            }
        } else {
            //call get location here
            getlocation();
        }




//        locationavailable=true;
//        originlat=34.061810;
//        originlon=-118.287273;



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //call get location here
                    getlocation();


                } else {
                    Toast.makeText(this.getApplicationContext(), "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void getlocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else{
            // Write you code here if permission already given
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,1, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5,1, mLocationListener);
        }


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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




    public static boolean fav_exist(Context thisone, String pid){
        SharedPreferences pref = thisone.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!pref.contains("4729510keyarray")){
            Set<String> likelist=new HashSet<String>();


            editor.putStringSet("4729510keyarray",likelist);

            editor.apply();
            return false;
        }


        if (pref.contains(pid)){
            return true;
        }
        else{
            return false;
        }

    }

    public static void fav_insert(Context thisone, JSONObject item){
        SharedPreferences pref = thisone.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        JSONObject detailresult=item;


        try {
            String pid =detailresult.getString("place_id");

            String namecontent = detailresult.getString("name");
            String imageUrl = detailresult.getString("icon");
            String addresscontent = detailresult.getString("vicinity");
            //String placeid = detailresult.getString("place_id");

            Set<String> array = pref.getStringSet("4729510keyarray",null);
            array.add(pid);

            String jsonstring=new JSONObject()
                    .put("name",namecontent)
                    .put("icon",imageUrl)
                    .put("vicinity",addresscontent)
                    .put("place_id",pid).toString();

            editor.putStringSet("4729510keyarray",array);
            editor.putString(pid,jsonstring);
            editor.apply();

        }
        catch(Exception e){
            System.out.println(e);
        }

    }


    public static void fav_remove(Context thisone, JSONObject item){
        SharedPreferences pref = thisone.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        JSONObject detailresult=item;


        try {
            String pid =detailresult.getString("place_id");


            Set<String> array = pref.getStringSet("4729510keyarray",null);
            array.remove(pid);


            editor.putStringSet("4729510keyarray",array);
            editor.remove(pid);
            editor.apply();

        }
        catch(Exception e){
            System.out.println(e);
        }


    }


    public static int fav_length(Context thisone){
        try {
            SharedPreferences pref = thisone.getSharedPreferences("MyPref", MODE_PRIVATE);
            Set<String> array = pref.getStringSet("4729510keyarray", null);
            return array.size();
        }
        catch(Exception e){
            return 0;
        }
    }


    public static List<JSONObject> fav_getall(Context thisone){

        SharedPreferences pref = thisone.getSharedPreferences("MyPref", MODE_PRIVATE);
        Set<String> array = pref.getStringSet("4729510keyarray",null);


        List<JSONObject> favresults=new ArrayList<JSONObject>();
        for (String i: array){
            String jsoncontent=pref.getString(i,null);
            try {
                JSONObject favobject = new JSONObject(jsoncontent);
                favresults.add(favobject);
            }
            catch(Exception e){
                System.out.println("fav parse wrong");
            }

        }

        return favresults;


    }







    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1Search tab1 = new Tab1Search();
                    return tab1;
                case 1:
                    Tab2Favor tab2 = new Tab2Favor();
                    return tab2;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }








}
