package com.example.zehaoli.test1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Tab3map extends Fragment implements OnMapReadyCallback {


    public MapView mapview;
    public GoogleMap map;
    public Polyline line=null;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    private View mContainer;
    private JSONObject thisresult;
    private static String thisplaceId = "";
    private String placename="";

    private double dest_lat;
    private double dest_lon;
    private double from_lat;
    private double from_lon;

    private boolean firsttime=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.tab3map, container, false);
        from_lat = Tab1Search.fromlat;
        from_lon = Tab1Search.fromlon;


        if (DetailActivity.detailavailable) {

            AutoCompleteTextView autocompleteView = (AutoCompleteTextView) mContainer.findViewById(R.id.autocomplete2);
            autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));
            autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get data associated with the specified position
                    // in the list (AdapterView)
                    Spinner spnLocale = (Spinner)mContainer.findViewById(R.id.travel_mode);

                    int i=0;

                    try {
                        i = spnLocale.getSelectedItemPosition();
                    }
                    catch(Exception e){
                        i=0;
                    }

                    String mode="driving";

                    if (i==0){
                        mode="driving";
                    }
                    else if(i==1){
                        mode="bicycling";
                    }
                    else if(i==2){
                        mode="transit";
                    }
                    else if(i==3){
                        mode="walking";
                    }

                    process_direction(mode);

                    //Toast.makeText(getActivity(), , Toast.LENGTH_SHORT).show();
                }
            });







            thisresult = DetailActivity.detailresult;




            try {


                Spinner spnLocale = (Spinner)mContainer.findViewById(R.id.travel_mode);
                spnLocale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (firsttime) {

                            // Your code here
                            String mode="driving";

                            if (i==0){
                                mode="driving";
                            }
                            else if(i==1){
                                mode="bicycling";
                            }
                            else if(i==2){
                                mode="transit";
                            }
                            else if(i==3){
                                mode="walking";
                            }

                            //Toast.makeText(getActivity().getApplicationContext(), Integer.toString(i) + "  hello there", Toast.LENGTH_SHORT).show();

                            process_direction(mode);



                        }
                        else{
                            firsttime=true;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });



                final String placeId = thisresult.getString("place_id");
                placename=thisresult.getString("name");
                thisplaceId = placeId;
                dest_lat = thisresult.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                dest_lon = thisresult.getJSONObject("geometry").getJSONObject("location").getDouble("lng");



                mapview = (MapView) mContainer.findViewById(R.id.map_content);
                mapview.onCreate(savedInstanceState);
                mapview.getMapAsync(this);


            } catch (Exception e) {
                System.out.println("tab 3 map, oncreate view json error");
            }


        }


        return mContainer;
    }

    @Override
    public void onMapReady(GoogleMap mapa) {
        this.map = mapa;
        LatLng mydest = new LatLng(dest_lat, dest_lon);



        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mydest, 14));

        map.addMarker(new MarkerOptions()
                .title(placename)
                .position(mydest))
                .showInfoWindow();

    }

    @Override
    public void onResume() {
        mapview.onResume();

        if ((map!=null)&&(map!=null)){
            map.clear();
        }
        if (map!=null) {
            LatLng mydest = new LatLng(dest_lat, dest_lon);
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(mydest, 14));

            map.addMarker(new MarkerOptions()
                    .title(placename)
                    .position(mydest))
                    .showInfoWindow();
        }

        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapview.onLowMemory();
    }




    private void process_direction(String mode){
        try {

            AutoCompleteTextView et=(AutoCompleteTextView) mContainer.findViewById(R.id.autocomplete2);
            final String et_here_value = et.getText().toString();

            String url="";
            if (et_here_value.equals("")){
                url = MainActivity.DIR + "submit_direction_latlontopid=a&lat="+Double.toString(from_lat)+"&lon="+Double.toString(from_lon)+"&pid="+thisplaceId+"&mode="+mode;
            }
            else{
                url = MainActivity.DIR + "submit_direction_addrtopid=a&addr="+URLEncoder.encode(et_here_value, "UTF-8")+"&pid="+thisplaceId+"&mode="+mode;
            }


            System.out.println("direction processing       "+url);



            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.get("status").equals("OK")) {
                                    System.out.println("direction processing       ok");
                                    drawPath(response,et_here_value);
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        }
        catch(Exception e){
            System.out.println(e);
            Toast.makeText(getActivity().getApplicationContext(),"parsing errors",Toast.LENGTH_SHORT).show();
        }








    }



    public void drawPath(JSONObject result, String nameoffrom) {
        LatLng endLatLng = new LatLng(dest_lat, dest_lon);



        GoogleMap myMap=map;
        if (line != null) {
            myMap.clear();
        }
        myMap.addMarker(new MarkerOptions()
                .title(placename)
                .position(endLatLng))
                .showInfoWindow();
        //myMap.addMarker(new MarkerOptions().position(startLatLng));
        try {
            // Tranform the string into a json object
            final JSONObject json = result;
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");


            JSONObject tempforstart=routes.getJSONArray("legs").getJSONObject(0).getJSONObject("start_location");
            double a=tempforstart.getDouble("lat");
            double b=tempforstart.getDouble("lng");

            LatLng startLatLng = new LatLng(a,b);
            myMap.addMarker(new MarkerOptions()
                    .title(nameoffrom)
                    .position(startLatLng))
                    .showInfoWindow();

            List<LatLng> list = decodePoly(encodedString);

//            for (int z = 0; z < list.size() - 1; z++) {
//                LatLng src = list.get(z);
//                LatLng dest = list.get(z + 1);
//                line = myMap.addPolyline(new PolylineOptions()
//                        .add(new LatLng(src.latitude, src.longitude),
//                                new LatLng(dest.latitude, dest.longitude))
//                        .width(5).color(Color.BLUE).geodesic(true));
//            }
            PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }
            line = myMap.addPolyline(options);

            zoomRoute(myMap,list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //decode the polylines
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }



    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }



}