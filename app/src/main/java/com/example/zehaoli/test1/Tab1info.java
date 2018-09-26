package com.example.zehaoli.test1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Arrays;

public class Tab1info extends Fragment {
    private View mContainer;
    private JSONObject thisresult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        if (DetailActivity.detailavailable){
            mContainer  = inflater.inflate(R.layout.tab1info, container, false);

            thisresult= DetailActivity.detailresult;



            try{
                String a =thisresult.getString("formatted_address");
                TextView thisone = (TextView) mContainer.findViewById(R.id.info_addr_content);
                thisone.setText(a);
            }
            catch(Exception e){
                TableRow add = (TableRow) mContainer.findViewById(R.id.info_addr);
                add.setVisibility(View.GONE);
            }

            try{
                String a =thisresult.getString("formatted_phone_number");
                TextView thisone = (TextView) mContainer.findViewById(R.id.info_phone_content);
                thisone.setText(a);
            }
            catch(Exception e){
                TableRow add = (TableRow) mContainer.findViewById(R.id.info_phone);
                add.setVisibility(View.GONE);
            }

            try{
                int a =thisresult.getInt("price_level");
                TextView thisone = (TextView) mContainer.findViewById(R.id.info_price_content);
                String b=new String(new char[a]).replace("\0", "$");;
                thisone.setText(b);
            }
            catch(Exception e){
                TableRow add = (TableRow) mContainer.findViewById(R.id.info_price);
                add.setVisibility(View.GONE);
            }

            try{
                float a =(float) thisresult.getDouble("rating");
                RatingBar thisone = (RatingBar) mContainer.findViewById(R.id.info_rate_content);
                System.out.println(a);
                thisone.setRating(a);
            }
            catch(Exception e){
                TableRow add = (TableRow) mContainer.findViewById(R.id.info_rate);
                add.setVisibility(View.GONE);
            }



            try{
                String a =thisresult.getString("url");
                TextView thisone = (TextView) mContainer.findViewById(R.id.info_google_content);
                thisone.setText(a);
            }
            catch(Exception e){
                TableRow add = (TableRow) mContainer.findViewById(R.id.info_google);
                add.setVisibility(View.GONE);
            }


            try{
                String a =thisresult.getString("website");
                TextView thisone = (TextView) mContainer.findViewById(R.id.info_web_content);
                thisone.setText(a);
            }
            catch(Exception e){
                TableRow add = (TableRow) mContainer.findViewById(R.id.info_web);
                add.setVisibility(View.GONE);
            }







        }
        else{
            mContainer  = inflater.inflate(R.layout.tab1info_norecord, container, false);
        }










        return mContainer;
    }





}
