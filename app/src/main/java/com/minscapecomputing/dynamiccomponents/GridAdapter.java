package com.minscapecomputing.dynamiccomponents;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

    private Activity context;
    private VideoPlayListener videoPlayListener;

    public void setVideoPlayListener(VideoPlayListener videoPlayListener) {
        this.videoPlayListener = videoPlayListener;
    }

    private class ViewHolder {

        ImageView imgThumb;
        ImageView imgVideoPlay;
    }

    private JSONArray array;
    private LayoutInflater mInflater;

    private int viewHeight = 0;

    public GridAdapter(Activity context, JSONArray array) {

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        if (array != null) {
            return array.length();
        }
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if (array != null && position >= 0 && position < getCount()) {
            return array.optJSONObject(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {

            view = mInflater.inflate(R.layout.grid_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgThumb = (ImageView) view.findViewById(R.id.imgThumb);
            viewHolder.imgVideoPlay = (ImageView) view.findViewById(R.id.imgVideoPlay);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final JSONObject item = array.optJSONObject(position);

        Picasso.with(view.getContext())
                .load(item.optString("thumb_image"))
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.no_image)
                .into(viewHolder.imgThumb);

        final boolean isVideo = item.optString("type").equalsIgnoreCase("video");
        viewHolder.imgVideoPlay.setVisibility(isVideo ? View.VISIBLE : View.GONE);
        viewHolder.imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoPlayListener != null) videoPlayListener.onVideoPlayClicked(item, isVideo);
            }
        });

        return view;
    }

    public interface VideoPlayListener {
        void onVideoPlayClicked(JSONObject item, boolean isVideo);
    }
}

