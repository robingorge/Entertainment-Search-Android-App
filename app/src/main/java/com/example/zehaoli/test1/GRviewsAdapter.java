package com.example.zehaoli.test1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class GRviewsAdapter extends RecyclerView.Adapter<GRviewsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(JSONObject item);
    }

    private final List<JSONObject> items;
    private final OnItemClickListener listener;

    public GRviewsAdapter(List<JSONObject> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.google_single_review, parent, false);
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

        private int like=0;


        private TextView user;
        private ImageView proimage;
        private RatingBar rating;

        private TextView time;
        private TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.greview_user);
            proimage = (ImageView) itemView.findViewById(R.id.greview_proimage);
            rating = (RatingBar) itemView.findViewById(R.id.greview_rating);

            time = (TextView) itemView.findViewById(R.id.greview_time);
            content = (TextView) itemView.findViewById(R.id.greview_content);
        }

        public void bind(final JSONObject item, final OnItemClickListener listener) {

            String tempuser="";
            String tempurl="";
            String tempprofile="";
            double temprate=0;

            Long temptime;
            String tempcontent="";

            try {

                if (item.has("author_name")) {
                    tempuser = item.getString("author_name");
                }

                if (item.has("profile_photo_url")) {
                    tempprofile = item.getString("profile_photo_url");
                }

                if (item.has("rating")) {
                    temprate = item.getDouble("rating");
                }

                if (item.has("time")) {
                    temptime = item.getLong("time");
                    String temptime_word="";

                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(temptime*1000);
                    TimeZone tz = TimeZone.getDefault();
                    sdf.setTimeZone(tz);
                    temptime_word=sdf.format(calendar.getTime());

                    time.setText(temptime_word);

                }

                if (item.has("text")) {
                    tempcontent = item.getString("text");
                }

                if (item.has("author_url")) {
                    tempurl = item.getString("author_url");
                }

            }
            catch(JSONException e){
                System.out.println(e);
            }




            user.setText(tempuser);
            rating.setRating((float) temprate);

            content.setText(tempcontent);

            Picasso.with(itemView.getContext()).load(tempprofile).into(proimage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });



        }
    }
}
