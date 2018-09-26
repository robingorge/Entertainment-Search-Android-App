package com.example.zehaoli.test1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(JSONObject item);

        void onItemLike(JSONObject item, ImageView a);
    }

    private final List<JSONObject> items;
    private final OnItemClickListener listener;

    public ContentAdapter(List<JSONObject> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView image;
        private ImageView fav;
        private TextView addr;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.itemname);
            image = (ImageView) itemView.findViewById(R.id.itemimage);
            fav = (ImageView) itemView.findViewById(R.id.itemfav);
            addr = (TextView) itemView.findViewById(R.id.itemaddress);

        }

        public void bind(final JSONObject item, final OnItemClickListener listener) {

            try {
                //about like
                //if shared place has this item, them set to red_heart

                String namecontent = item.getString("name");
                String imageUrl = item.getString("icon");
                String addresscontent = item.getString("vicinity");
                String ppid=item.getString("place_id");


                name.setText(namecontent);
                addr.setText(addresscontent);
                Picasso.with(itemView.getContext()).load(imageUrl).into(image);

                if (MainActivity.fav_exist(itemView.getContext(),ppid)){
                    fav.setImageResource(R.drawable.heart_fill_red);
                }
                else{
                    fav.setImageResource(R.drawable.heart_outline_black);
                }


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });


                //about like
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemLike(item,fav);
                    }
                });



            }
            catch(JSONException e){
                System.out.println(e);
            }
        }
    }
}
