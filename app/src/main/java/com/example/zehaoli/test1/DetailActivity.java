package com.example.zehaoli.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DetailActivity extends AppCompatActivity {


    public static GeoDataClient mGeoDataClient;
    public static PlaceDetectionClient mPlaceDetectionClient;

    public static JSONObject detailresult;
    public static boolean detailavailable=false;



    private ProgressDialog pDialog = null;

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
            R.drawable.info_outline,
            R.drawable.photos,
            R.drawable.maps,
            R.drawable.review
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        String value = getIntent().getStringExtra("detail_id");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching results");
        pDialog.show();


        //for map and photo

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);




        //fetching data
        get_all_detail(value);




        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.detail_container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//
//        final TabLayout dtabLayout = (TabLayout) findViewById(R.id.detail_tabs);
//
//
//
//        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
//        tabOne.setText(" INFO");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.info_outline,0, 0, 0);
//        dtabLayout.getTabAt(0).setCustomView(tabOne);
//
//        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
//        tabTwo.setText(" PHOTOS");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.photos,0, 0, 0);
//        dtabLayout.getTabAt(1).setCustomView(tabTwo);
//
//        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
//        tabThree.setText(" MAP");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.maps,0, 0, 0);
//        dtabLayout.getTabAt(2).setCustomView(tabThree);
//
//        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
//        tabFour.setText(" REVIEW");
//        tabFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.review,0, 0, 0);
//        dtabLayout.getTabAt(3).setCustomView(tabFour);
//
//
//        View root = dtabLayout.getChildAt(0);
//        if (root instanceof LinearLayout) {
//            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setColor(getResources().getColor(R.color.separator));
//            drawable.setSize(2, 1);
//            ((LinearLayout) root).setDividerPadding(10);
//            ((LinearLayout) root).setDividerDrawable(drawable);
//        }
//
//
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(dtabLayout));
//        dtabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));





    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) // Press Back Icon
    {
        finish();
    }

    return super.onOptionsItemSelected(item);
}


    private void get_all_detail(String id){
        try {
            String url = MainActivity.DIR + "submit_placedetail=a&id="+id;
            System.out.println("place_id       "+url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.get("status").equals("OK")) {
                                    System.out.println("okkkkkk for place get");
                                    //JSONArray results=response.getJSONArray("results");
                                    detailavailable=true;
                                    detailresult=response.getJSONObject("result");


                                    String name=detailresult.getString("name");
                                    Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
                                    toolbar.setTitle(name);

                                    settinguppage();

                                    pDialog.dismiss();
                                    return;

                                }

                            } catch (Exception e) {
                                System.out.println(e);
                                Toast.makeText(getApplicationContext(),"volley okay but json errors",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            pDialog.dismiss();
                            System.out.println(error);
                            Toast.makeText(getApplicationContext(),"volley connection errors",Toast.LENGTH_SHORT).show();

                        }
                    });
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        }
        catch(Exception e){
            pDialog.dismiss();
            System.out.println(e);
            Toast.makeText(getApplicationContext(),"parsing errors",Toast.LENGTH_SHORT).show();
        }




    }


    private void settinguppage(){

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.detail_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        final TabLayout dtabLayout = (TabLayout) findViewById(R.id.detail_tabs);



        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
        tabOne.setText(" INFO");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.info_outline,0, 0, 0);
        dtabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
        tabTwo.setText(" PHOTOS");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.photos,0, 0, 0);
        dtabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
        tabThree.setText(" MAP");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.maps,0, 0, 0);
        dtabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
        tabFour.setText(" REVIEW");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.review,0, 0, 0);
        dtabLayout.getTabAt(3).setCustomView(tabFour);


        View root = dtabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.separator));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }


        String name="NO_NAME";
        String addr="NO_ADDRESS";
        String uurl="NO_URL";


        try{
            name=detailresult.getString("name");
        }catch(Exception e){
            name="NO_NAME";
        }

        try{
            addr=detailresult.getString("vicinity");
        }catch(Exception e){
            addr="NO_ADDRESS";
        }

        try{
            uurl=detailresult.getString("website");
        }catch(Exception e){
            uurl="NO_URL";
        }


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(dtabLayout));
        dtabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));



        String twi="Check out "+name+" located at "+addr+". Website: ";

        try {
            final String tcontent="https://twitter.com/intent/tweet?text="+ URLEncoder.encode(twi, "UTF-8")+"&url="+URLEncoder.encode(uurl, "UTF-8")+"&hashtags=TravelAndEntertainmentSearch";
            ImageView share=(ImageView) findViewById(R.id.imageView3);
            share.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    Uri uri = Uri.parse(tcontent);
                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }

            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //like implementation
        final ImageView fav=(ImageView) findViewById(R.id.imageView4);
        try {
            String pppid = detailresult.getString("place_id");
            if (MainActivity.fav_exist(getApplicationContext(),pppid)){
                fav.setImageResource(R.drawable.heart_fill_white);
            }
        }
        catch(Exception e){}

        fav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                try {
                    String placeid= detailresult.getString("place_id");
                    String namecontent =detailresult.getString("name");

                    if (MainActivity.fav_exist(getApplicationContext(),placeid)){
                        MainActivity.fav_remove(getApplicationContext(),detailresult);
                        fav.setImageResource(R.drawable.heart_outline_white);
                        Toast.makeText(getApplicationContext(), namecontent+" was removed from favorites", Toast.LENGTH_LONG).show();
                    }
                    else{
                        MainActivity.fav_insert(getApplicationContext(),detailresult);
                        fav.setImageResource(R.drawable.heart_fill_white);
                        Toast.makeText(getApplicationContext(), namecontent+" was added to favorites", Toast.LENGTH_LONG).show();
                    }


                    //Picasso.with(itemView.getContext()).load(imageUrl).into(image);


                }
                catch(Exception e){
                    System.out.println(e);
                }


            }

        });


    }


    @Override
    protected void onStop(){
        super.onStop();
        pDialog=null;


    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        pDialog=null;


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
                    Tab1info tab1 = new Tab1info();
                    return tab1;
                case 1:
                    Tab2photo tab2 = new Tab2photo();
                    return tab2;
                case 2:
                    Tab3map tab3 = new Tab3map();
                    return tab3;
                case 3:
                    Tab4review tab4 = new Tab4review();
                    return tab4;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
