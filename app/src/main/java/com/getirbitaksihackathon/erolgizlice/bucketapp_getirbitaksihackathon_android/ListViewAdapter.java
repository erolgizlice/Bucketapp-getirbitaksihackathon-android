package com.getirbitaksihackathon.erolgizlice.bucketapp_getirbitaksihackathon_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by erolg on 26.03.2017.
 */

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    ArrayList<Event> eventsList = new ArrayList<Event>();
    LayoutInflater inflater;

    public ListViewAdapter(Context context, ArrayList<Event> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView eventNameTV;
        TextView eventDateTV;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        eventNameTV = (TextView) itemView.findViewById(R.id.eventName);
        eventDateTV = (TextView) itemView.findViewById(R.id.eventTime);

        eventNameTV.setText(eventsList.get(position).getName());
        eventDateTV.setText(eventsList.get(position).getStart_time());

        itemView.setTag(eventsList.get(position).facebook_event_id);

        return itemView;
    }
}
