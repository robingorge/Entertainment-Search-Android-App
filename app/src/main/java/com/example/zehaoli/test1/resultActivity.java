package com.example.zehaoli.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class resultActivity extends AppCompatActivity {

    private ProgressDialog pDialog = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private JSONObject CCPAGE;
    private JSONObject currentpage;
    private List<JSONObject> savedresults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching results");
        pDialog.show();








        if (Tab1Search.search_record){
            System.out.println("=====start the  result table     query part");

            new Thread(new Runnable() {
                public void run() {
                    Log.i("Thread", "Running parallely");


                    try {
                        String url = MainActivity.DIR + "submit_placesearch=a&cat="+URLEncoder.encode(Tab1Search.f_cate, "UTF-8")+"&dis="+URLEncoder.encode(Tab1Search.f_distance, "UTF-8")+"&keyword="+URLEncoder.encode(Tab1Search.f_keyword, "UTF-8")+"&lat="+Double.toString(Tab1Search.fromlat)+"&lon="+Double.toString(Tab1Search.fromlon);
                        System.out.println("geocoding       "+url);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.get("status").equals("OK")) {
                                                System.out.println("okkkkkk for place search");

                                                JSONArray results=response.getJSONArray("results");

                                                CCPAGE=response;//////////////


                                                System.out.println(results.length());

                                                success_get_results(response);

                                                pDialog.dismiss();


                                                return;

                                            }

                                        } catch (Exception e) {
                                            System.out.println(e);
                                            Toast.makeText(getApplicationContext(),"volley okay but json errors",Toast.LENGTH_SHORT).show();

                                        }
                                        pDialog.dismiss();

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
            }).start();







        }
        else{
            pDialog.dismiss();
        }






    }



    @Override
    protected void onResume(){
        super.onResume();
        processgeneralpage(CCPAGE);


    }


    private void success_get_results(JSONObject responseall){



            setContentView(R.layout.activity_results);
            mRecyclerView = (RecyclerView) findViewById(R.id.results_table);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);



        savedresults = new ArrayList<JSONObject>();
            processgeneralpage(responseall);


//            List<JSONObject> list = new ArrayList<JSONObject>();
//
//            int i;
//            JSONArray array = allresults;
//            for (i = 0; i < array.length(); i++) {
//                list.add(array.getJSONObject(i));
//            }
//
//
//            if (responseall.has("next_page_token") && !responseall.getString("next_page_token").equals("")){
//                currentpage=responseall;
//
//                savedresults = new ArrayList<JSONObject>();
//
//                Button nbutton = (Button) this.findViewById(R.id.nextpage);
//                nbutton.setEnabled(true);
//                nbutton.setOnClickListener(new Button.OnClickListener() {
//                    public void onClick(View v) {
//                        processnextpage();
//                    }
//                });
//            }
//            else{
//                Button nbutton = (Button) this.findViewById(R.id.nextpage);
//                nbutton.setEnabled(false);
//                nbutton.setOnClickListener(null);
//            }
//
//
//            setResultsForAdapter(list);



        return;


    }




    private void processgeneralpage(JSONObject results){

        try {

            JSONArray allresults = results.getJSONArray("results");



            List<JSONObject> list = new ArrayList<JSONObject>();

            int i;
            JSONArray array = allresults;
            for (i = 0; i < array.length(); i++) {
                list.add(array.getJSONObject(i));
            }

            if (results.has("next_page_token") && !results.getString("next_page_token").equals("")){
                currentpage=results;

                Button nbutton = (Button) this.findViewById(R.id.nextpage);
                nbutton.setEnabled(true);
                nbutton.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        processnextpage();
                    }
                });
            }
            else{
                Button nbutton = (Button) this.findViewById(R.id.nextpage);
                nbutton.setEnabled(false);
                nbutton.setOnClickListener(null);
            }

            int ssize=savedresults.size();
            if (ssize>0){
                Button nbutton = (Button) this.findViewById(R.id.previouspage);
                nbutton.setEnabled(true);
                nbutton.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        processpreviouspage();
                    }
                });


            }
            else{
                Button nbutton = (Button) this.findViewById(R.id.previouspage);
                nbutton.setEnabled(false);
                nbutton.setOnClickListener(null);
            }


            setResultsForAdapter(list);


        }
        catch(Exception e){
            System.out.println(e);
        }

        return;




    }




    private void processpreviouspage(){
        int ssize=savedresults.size();
        if (ssize>0){
            JSONObject needone=savedresults.get(ssize-1);

            CCPAGE=needone;//////////////

            savedresults.remove(ssize-1);
            processgeneralpage(needone);

        }
        return;
    }





    private void processnextpage(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Fetching next page");
        pDialog.show();


        System.out.println("=====start the    nextpage     query part");

        new Thread(new Runnable() {
            public void run() {
                Log.i("Thread", "Running parallely");

                try {
                    String url = MainActivity.DIR + "submit_pagetoken=a&pageid="+currentpage.getString("next_page_token");
                    System.out.println("pagetoken       "+url);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.get("status").equals("OK")) {
                                            System.out.println("okkkkkk for page get");
                                            JSONArray results=response.getJSONArray("results");


                                            CCPAGE=response;//////////////


                                            savedresults.add(currentpage);

                                            processgeneralpage(response);

                                            pDialog.dismiss();


                                            return;

                                        }

                                    } catch (Exception e) {
                                        System.out.println(e);
                                        Toast.makeText(getApplicationContext(),"volley okay but json errors",Toast.LENGTH_SHORT).show();

                                    }
                                    pDialog.dismiss();

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
        }).start();





    }





    private void setResultsForAdapter(List<JSONObject> content){

        mRecyclerView.setAdapter(new ContentAdapter(content, new ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(JSONObject item) {
                //Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_LONG).show();

                try{
                    String detailid=item.getString("place_id");
                    next_level(detailid);
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onItemLike(JSONObject item, ImageView a) {
                try {
                    String namecontent = item.getString("name");
                    String ppid =item.getString("place_id");

                    if (! MainActivity.fav_exist(getApplicationContext(),ppid)) {
                        a.setImageResource(R.drawable.heart_fill_red);
                        MainActivity.fav_insert(getApplicationContext(),item);
                        Toast.makeText(getApplicationContext(), namecontent+" was added to favorites", Toast.LENGTH_LONG).show();
                    } else {
                        a.setImageResource(R.drawable.heart_outline_black);
                        MainActivity.fav_remove(getApplicationContext(),item);
                        Toast.makeText(getApplicationContext(), namecontent+" was removed from favorites", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        }));

    }

    private void next_level(String did){

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail_id",did);
        startActivity(intent);
    }


}
