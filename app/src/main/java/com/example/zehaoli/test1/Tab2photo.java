package com.example.zehaoli.test1;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Tab2photo extends Fragment {
    protected GeoDataClient mGeoDataClient;
    //protected PlaceDetectionClient mPlaceDetectionClient;


    private View mContainer;
    private JSONObject thisresult;
    private static String thisplaceId="";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Bitmap> photolist;

    private boolean okay=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (DetailActivity.detailavailable) {
            mContainer = inflater.inflate(R.layout.tab2photo, container, false);

            mRecyclerView = (RecyclerView) mContainer.findViewById(R.id.photo_table);


            thisresult = DetailActivity.detailresult;

            if (!thisresult.has("photos")){
                mContainer = inflater.inflate(R.layout.tab2photo_norecord, container, false);
                return mContainer;
            }


            try {

                // Construct a GeoDataClient.
                mGeoDataClient = DetailActivity.mGeoDataClient;

                // Construct a PlaceDetectionClient.
                //mPlaceDetectionClient = DetailActivity.mPlaceDetectionClient;


                final String placeId=thisresult.getString("place_id");
                thisplaceId=placeId;


                final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
                photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                        // Get the list of photos.
                        PlacePhotoMetadataResponse photos = task.getResult();
                        // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();

                        final int length=photoMetadataBuffer.getCount();

                        if (length>0) {
                            photolist = new ArrayList<Bitmap>();

                            for (int i = 0; i < length; i++) {
                                // Get the i photo in the list.
                                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                                // Get a full-size bitmap for the photo.
                                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                                    @Override
                                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                        PlacePhotoResponse photo = task.getResult();
                                        Bitmap bitmap = photo.getBitmap();
                                        photolist.add(bitmap);
                                        if (photolist.size()==length){
                                            success_get_image(photolist);

                                            //okay=true;
                                        }

                                    }
                                });
                            }
                        }



                    }
                });


            } catch (Exception e) {
                System.out.println(e);
                mContainer = inflater.inflate(R.layout.tab2photo_norecord, container, false);
            }


            //add
            //mContainer = inflater.inflate(R.layout.tab1info_norecord, container, false);

        }
        else{
            mContainer = inflater.inflate(R.layout.tab2photo_norecord, container, false);

        }


//        if (!okay){
//            mContainer = inflater.inflate(R.layout.tab1info_norecord, container, false);
//        }





        return mContainer;
    }





    private void success_get_image(List<Bitmap> items){
        //mRecyclerView = (RecyclerView) mContainer.findViewById(R.id.photo_table);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new PhotoAdapter(items);
        mRecyclerView.setAdapter(mAdapter);



    }


}
