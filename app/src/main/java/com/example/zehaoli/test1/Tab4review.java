package com.example.zehaoli.test1;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Tab4review extends Fragment {

    private View mContainer;
    private JSONObject thisresult;


    private List<JSONObject> gReviews;
    private List<JSONObject> yReviews;


    private RecyclerView mRecyclerView;
    private TextView mNorecord;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Spinner spnone;
    private Spinner spntwo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.tab4review, container, false);
        thisresult = DetailActivity.detailresult;
        mRecyclerView = (RecyclerView) mContainer.findViewById(R.id.review_content);
        mNorecord = (TextView) mContainer.findViewById(R.id.review_norecord);



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        gReviews=new ArrayList<JSONObject>();
        yReviews=new ArrayList<JSONObject>();


        processgoogle();

        new Thread(new Runnable() {
            public void run() {
                Log.i("Thread", "Running parallely");
                try {
                    // URLEncoder.encode(Tab1Search.f_cate, "UTF-8")

                    String name=thisresult.getString("name");
                    String url = MainActivity.DIR + "submit_review=a&name="+URLEncoder.encode(name, "UTF-8");

                    String city="";
                    String state="";
                    String country="";
                    String address="";

                    JSONArray addcomps=thisresult.getJSONArray("address_components");

                    for (int i=0; i<addcomps.length(); i++){
                        JSONObject curr=addcomps.getJSONObject(i);
                        String tp=curr.getJSONArray("types").getString(0);
                        if (tp.equals("locality")){
                            city=curr.getString("short_name");
                            url=url+"&city="+URLEncoder.encode(city, "UTF-8");

                        }
                        else if (tp.equals("administrative_area_level_1")){
                            state=curr.getString("short_name");
                            url=url+"&state="+URLEncoder.encode(state, "UTF-8");
                        }
                        else if (tp.equals("country")){
                            country=curr.getString("short_name");
                            url=url+"&country="+URLEncoder.encode(country, "UTF-8");
                        }


                    }


                    if (thisresult.has("vicinity")){
                        address=thisresult.getString("vicinity");
                        url=url+"&address="+URLEncoder.encode(address, "UTF-8");
                    }


                    if (city.equals("") && state.equals("") && country.equals("")){
                        throw new Exception("yelp address not complete");
                    }



                    System.out.println("yelp first stage       "+url);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray yelplaces=response.getJSONArray("businesses");
                                        if (yelplaces.length() > 0) {
                                            System.out.println("okkkkkk for yelp 1 search");
                                            String yelpid = yelplaces.getJSONObject(0).getString("id");

                                            //System.out.println(yelpid);
                                            processyelp(yelpid);

                                            return;

                                        }

                                    } catch (Exception e) {
                                        System.out.println(e);
                                        Toast.makeText(getActivity().getApplicationContext(),"volley okay but json errors",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    System.out.println(error);
                                    Toast.makeText(getActivity().getApplicationContext(),"volley connection errors",Toast.LENGTH_SHORT).show();

                                }
                            });
                    // Access the RequestQueue through your singleton class.
                    MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }
                catch(Exception e){
                    System.out.println(e);
                    Toast.makeText(getActivity().getApplicationContext(),"parsing errors",Toast.LENGTH_SHORT).show();
                }


            }
        }).start();


        //spinner1.getSelectedItemPosition();

        spnone = (Spinner)mContainer.findViewById(R.id.review_type);
        spntwo = (Spinner)mContainer.findViewById(R.id.review_order);

        spnone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setResultsForAdapter(3);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        spntwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setResultsForAdapter(3);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        return mContainer;
    }

    @Override
    public void onResume() {
        super.onResume();
        spnone.setSelection(0);
        spntwo.setSelection(0);
        setResultsForAdapter(3);
    }


    //mod 0:google    mod 1: yelp

    private void setResultsForAdapter(int mod){
        int sone=0;
        int stwo=0;

        int setting=0;

        try {
            sone = spnone.getSelectedItemPosition();
        }
        catch(Exception e){
        }

        try {
            stwo=spntwo.getSelectedItemPosition();
        }
        catch(Exception e){
        }

        if (mod>1){
            setting=sone;
        }
        else{
            setting=mod;
        }

        if (setting==0) {


            if (gReviews.size()==0){
                emptycontent();
                return;
            }
            hascontent();


            //List<JSONObject> grev=gReviews;
            List<JSONObject> grev=new ArrayList<JSONObject>();
            for(JSONObject p : gReviews) {
                grev.add(p);
            }



            if ((stwo==1)||(stwo==2)){
                Collections.sort(grev, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject lhs, JSONObject rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        double a=0;
                        double b=0;
                        try{
                            a=lhs.getDouble("rating");
                            b=rhs.getDouble("rating");

                            if (a>b){
                                return 1;
                            }
                            else{
                                return -1;
                            }

                        }
                        catch(Exception e){
                            return 0;
                        }
                    }
                });


                if (stwo==1){
                    Collections.reverse(grev);
                }

            }
            else if ((stwo==3)||(stwo==4)){
                Collections.sort(grev, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject lhs, JSONObject rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        Long a;
                        Long b;
                        try{
                            a=lhs.getLong("time");
                            b=rhs.getLong("time");

                            if (a>b){
                                return 1;
                            }
                            else{
                                return -1;
                            }

                        }
                        catch(Exception e){
                            return 0;
                        }
                    }
                });

                if (stwo==3){
                    Collections.reverse(grev);
                }


            }




            mRecyclerView.setAdapter(new GRviewsAdapter(grev, new GRviewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(JSONObject item) {
                    //Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                    String url="";
                    try{
                        url=item.getString("author_url");
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }

                    Uri uri = Uri.parse(url);
                    Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }


            }));
        }
        else if (setting==1){
            if (yReviews.size()==0){
                emptycontent();
                return;
            }
            hascontent();





            List<JSONObject> yrev=new ArrayList<JSONObject>();
            for(JSONObject p : yReviews) {
                yrev.add(p);
            }

            if ((stwo==1)||(stwo==2)){
                Collections.sort(yrev, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject lhs, JSONObject rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        double a=0;
                        double b=0;
                        try{
                            a=lhs.getDouble("rating");
                            b=rhs.getDouble("rating");

                            if (a>b){
                                return 1;
                            }
                            else{
                                return -1;
                            }

                        }
                        catch(Exception e){
                            return 0;
                        }
                    }
                });


                if (stwo==1){
                    Collections.reverse(yrev);
                }

            }
            else if ((stwo==3)||(stwo==4)){
                Collections.sort(yrev, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject lhs, JSONObject rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending

                        String aword;
                        String bword;

                        Long a;
                        Long b;
                        try{
                            aword=lhs.getString("time_created");
                            bword=rhs.getString("time_created");

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = format.parse(aword);
                            a = date.getTime();

                            date = format.parse(bword);
                            b = date.getTime();

                            if (a>b){
                                return 1;
                            }
                            else{
                                return -1;
                            }

                        }
                        catch(Exception e){
                            return 0;
                        }
                    }
                });

                if (stwo==3){
                    Collections.reverse(yrev);
                }


            }







            mRecyclerView.setAdapter(new YRviewsAdapter(yrev, new YRviewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(JSONObject item) {
                    //Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                    String url="";
                    try{
                        if (item.has("url")) {

                            url = item.getString("url");
                            Uri uri = Uri.parse(url);
                            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(intent);
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }

                }


            }));


        }

    }







    private void processgoogle(){
        try {


            JSONArray tempreviews = thisresult.getJSONArray("reviews");
            if (tempreviews.length() > 0) {
                System.out.println("okkkkkk for google reviews");
                for (int i = 0; i < tempreviews.length(); i++) {
                    JSONObject curr = tempreviews.getJSONObject(i);
                    gReviews.add(curr);
                }

                hascontent();
                setResultsForAdapter(0);

                return;
            }


        }
        catch(Exception e){
            System.out.println(e);
            Toast.makeText(getActivity().getApplicationContext(),"google parsing errors",Toast.LENGTH_SHORT).show();
        }

    }


    private void processyelp(String id){
        try {
            String url = MainActivity.DIR + "submit_yelpid=a&id="+ id;

            System.out.println("yelp stage 2    get reviews       "+url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {


                                JSONArray tempreviews=response.getJSONArray("reviews");
                                if (tempreviews.length()>0) {

                                    System.out.println("okkkkkk for yelp2 search");
                                    for (int i=0; i< tempreviews.length();i++){
                                        JSONObject curr=tempreviews.getJSONObject(i);
                                        yReviews.add(curr);
                                    }


                                    return;

                                }

                            } catch (Exception e) {
                                System.out.println(e);
                                Toast.makeText(getActivity().getApplicationContext(),"volley okay but json errors",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println(error);
                            Toast.makeText(getActivity().getApplicationContext(),"volley connection errors",Toast.LENGTH_SHORT).show();

                        }
                    });
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        }
        catch(Exception e){
            System.out.println(e);
            Toast.makeText(getActivity().getApplicationContext(),"parsing errors",Toast.LENGTH_SHORT).show();
        }

    }







    private void emptycontent(){
        mRecyclerView.setVisibility(View.GONE);
        mNorecord.setVisibility(View.VISIBLE);
    }

    private void hascontent(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mNorecord.setVisibility(View.GONE);
    }










}
