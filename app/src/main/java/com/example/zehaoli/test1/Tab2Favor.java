package com.example.zehaoli.test1;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

public class Tab2Favor extends Fragment {
    private View mContainer;
    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.tab2favor, container, false);
        //mContainer = inflater.inflate(R.layout.tab1info_norecord, container, false);

        mRecyclerView = (RecyclerView) mContainer.findViewById(R.id.fav_view);
        mTextView = (TextView) mContainer.findViewById(R.id.fav_norecord);


        processfavpage();


        return mContainer;
    }



    @Override
    public void onResume(){
        super.onResume();
        processfavpage();
    }



    private void processfavpage(){

        if (MainActivity.fav_length(getActivity().getApplicationContext())>0){
            List<JSONObject> allfav=MainActivity.fav_getall(getActivity().getApplicationContext());
            if (allfav.size()>0){

                mRecyclerView.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.GONE);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new ContentAdapter(allfav, new ContentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(JSONObject item) {
                        //Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_LONG).show();

                        try{
                            String detailid=item.getString("place_id");
                            next_level(detailid);
                        }
                        catch(Exception e){
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onItemLike(JSONObject item, ImageView a) {
                        try {
                            String namecontent = item.getString("name");
                            String ppid =item.getString("place_id");

                            if (! MainActivity.fav_exist(getActivity().getApplicationContext(),ppid)) {
                                a.setImageResource(R.drawable.heart_fill_red);
                                MainActivity.fav_insert(getActivity().getApplicationContext(),item);
                                Toast.makeText(getActivity().getApplicationContext(), namecontent+" was added to favorites", Toast.LENGTH_LONG).show();
                            } else {
                                a.setImageResource(R.drawable.heart_outline_black);
                                MainActivity.fav_remove(getActivity().getApplicationContext(),item);
                                Toast.makeText(getActivity().getApplicationContext(), namecontent+" was removed from favorites", Toast.LENGTH_LONG).show();
                            }
                            processfavpage();

                        }
                        catch(Exception e){
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                });

                mRecyclerView.setAdapter(mAdapter);


                return;


            }


        }




        mRecyclerView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        return;


    }











    private void next_level(String did){

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("detail_id",did);
        startActivity(intent);
    }
}
