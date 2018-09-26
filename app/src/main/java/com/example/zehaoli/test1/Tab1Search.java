package com.example.zehaoli.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.zehaoli.test1.R.id;

public class Tab1Search extends Fragment {


    private ProgressDialog pDialog = null;

    public static double fromlat=0.0000;
    public static double fromlon=0.0000;
    public static String f_keyword="";
    public static String f_distance="";
    public static String f_cate="";
    public static boolean search_record=false;

//    private static String TAG = MainActivity.class.getSimpleName();
//
//    private PlacesAutoCompleteAdapter mAdapter;
//
//    HandlerThread mHandlerThread;
//    Handler mThreadHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1search, container, false);

//        // Construct a GeoDataClient.
//        mGeoDataClient = Places.getGeoDataClient(this.getActivity(), null);
//
//        // Construct a PlaceDetectionClient.
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(this.getActivity(), null);


//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                this.getActivity().getFragmentManager().findFragmentById(id.place_autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//            }
//        });

        final TextView warnone = (TextView) rootView.findViewById(id.searchwarn1);
        final TextView warntwo = (TextView) rootView.findViewById(id.searchwarn2);

        final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(id.locationgroup);
        final RadioButton rb=(RadioButton) rootView.findViewById(id.cl_one);
        final EditText ki=(EditText) rootView.findViewById(id.keywordinput);
        final Spinner cs=(Spinner) rootView.findViewById(id.catespinner);
        final EditText di=(EditText) rootView.findViewById(id.distanceinput);

        final AutoCompleteTextView et=(AutoCompleteTextView) rootView.findViewById(id.autocomplete);      ///////////new
        //final EditText et=(EditText) rootView.findViewById(id.search_place);                            ///////////maybechange
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rb.getId()==checkedId) {
                    System.out.println("000000 check click");

                    et.setText("");
                    et.setEnabled(false);                                                               ///////////maybechange

                    warntwo.setVisibility(View.GONE);
                }
                else{
                    System.out.println("111111 check click");
                    et.setEnabled(true);                                                                ///////////maybechange
                }

            }
        });





        Button buttonOne = (Button) rootView.findViewById(id.search_button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                System.out.println("search button click");

                String cs_value = cs.getSelectedItem().toString();
                //System.out.println(cs_value);
                String ki_value = ki.getText().toString();
                String di_value = di.getText().toString();
                String et_value = et.getText().toString();                                              ///////////maybechange

                String regexx=".*[a-zA-Z]+.*";
                Boolean a=ki_value.matches(regexx);
                Boolean b=et_value.matches(regexx);                                                    ///////////maybechange


                if (!a && !b && !rb.isChecked()){
                    warnone.setVisibility(View.VISIBLE);
                    warntwo.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(),"Please fix all fields with errors",Toast.LENGTH_SHORT).show();
                }
                else if (!a){
                    warnone.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(),"Please fix all fields with errors",Toast.LENGTH_SHORT).show();

                }
                else if (!b && !rb.isChecked()){
                    warntwo.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity().getApplicationContext(),"Please fix all fields with errors",Toast.LENGTH_SHORT).show();
                }
                else{
                    MainActivity thisone=(MainActivity) getActivity();

                    cs_value=cs_value.replaceAll(" ", "_").toLowerCase();
                    //kivalue divalue(if there is) etvalue(if rb checked)

                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Fetching results");
                    pDialog.show();


                    //prepare static value for resultactivity to get results
                    f_keyword=ki_value;
                    f_distance=di_value;
                    f_cate=cs_value;
                    search_record=false;


                    if (rb.isChecked()){
                        search_record=true;

                        fromlat=MainActivity.originlat;
                        fromlon=MainActivity.originlon;

                        Intent intent = new Intent(getActivity(), resultActivity.class);
                        startActivity(intent);
                    }
                    else {
                        //String url = "http://10.0.0.4:3000/?submit_geocoding=aaa&location=universityofsoutherncalifornia";
                        try {
                            String url = MainActivity.DIR + "submit_geocoding=a&location=" + URLEncoder.encode(et_value, "UTF-8");
                            System.out.println("geocoding       "+url);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (response.get("status").equals("OK")) {
                                                    search_record=true;
                                                    //from
                                                    JSONObject thisoneresult=response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                                                    fromlat=thisoneresult.getDouble("lat");
                                                    fromlon=thisoneresult.getDouble("lng");

                                                    System.out.println(Double.toString(fromlat)+" ===== "+Double.toString(fromlon));
                                                }
                                                Intent intent = new Intent(getActivity(), resultActivity.class);
                                                startActivity(intent);

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
                            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
                        }
                        catch(Exception e){
                            System.out.println(e);
                            Toast.makeText(getActivity().getApplicationContext(),"parsing errors",Toast.LENGTH_SHORT).show();
                        }
                    }





                    //System.out.println(thisone.originlat);
                }


            }
        });


        Button buttonTwo = (Button) rootView.findViewById(id.clear_button);
        buttonTwo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                System.out.println("clear button click");


                pDialog = null;

                fromlat=0.0000;
                fromlon=0.0000;
                f_keyword="";
                f_distance="";
                f_cate="";
                search_record=false;




                ki.setText("");
                cs.setSelection(0);
                di.setText("");

                radioGroup.check(R.id.cl_one);

                et.setText("");                                                                         ///////////maybechange
                et.setEnabled(false);                                                                   ///////////maybechange


                warnone.setVisibility(View.GONE);
                warntwo.setVisibility(View.GONE);

            }
        });


        AutoCompleteTextView autocompleteView = (AutoCompleteTextView) rootView.findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));

//        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get data associated with the specified position
//                // in the list (AdapterView)
//                String description = (String) parent.getItemAtPosition(position);
//                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
//            }
//        });


//        autocompleteView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                final String value = s.toString();
//
//                // Remove all callbacks and messages
//                mThreadHandler.removeCallbacksAndMessages(null);
//
//                // Now add a new one
//                mThreadHandler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // Background thread
//
//                        mAdapter.resultList = mAdapter.mPlaceAPI.autocomplete(value);
//
//                        // Footer
//                        if (mAdapter.resultList.size() > 0)
//                            mAdapter.resultList.add("footer");
//
//                        // Post to Main Thread
//                        mThreadHandler.sendEmptyMessage(1);
//                    }
//                }, 500);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                doAfterTextChanged();
//            }
//        });
//
//
//
//
        return rootView;
    }






    @Override
    public void onDestroy() {

        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog=null;
        }

    }

    @Override
    public void onPause() {

        super.onPause();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog=null;
        }

    }




//    public ProfileFragment() {
//        // Required empty public constructor
//
//        if (mThreadHandler == null) {
//            // Initialize and start the HandlerThread
//            // which is basically a Thread with a Looper
//            // attached (hence a MessageQueue)
//            mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
//            mHandlerThread.start();
//
//            // Initialize the Handler
//            mThreadHandler = new Handler(mHandlerThread.getLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    if (msg.what == 1) {
//                        ArrayList<String> results = mAdapter.resultList;
//
//                        if (results != null && results.size() > 0) {
//                            mAdapter.notifyDataSetChanged();
//                        }
//                        else {
//                            mAdapter.notifyDataSetInvalidated();
//                        }
//                    }
//                }
//            };
//        }
//
//    }



}
