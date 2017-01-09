package tech.phlocs.histleap.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tech.phlocs.histleap.R;
import tech.phlocs.histleap.model.Event;

public class EventListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<Event> data = null;
    private int resource = 0;
    private ArrayList<Integer> range = null;

    // コンストラクタ
    public EventListAdapter(Context context, ArrayList<Event> data, int resource, ArrayList<Integer> range) {
        this.context  = context;
        this.data     = data;
        this.resource = resource;
        this.range = range;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity)context;
        Event item = (Event)getItem(position);
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }
        ((TextView) convertView.findViewById(R.id.event_year)).setText(String.valueOf(item.getStartYear()));
        ((TextView) convertView.findViewById(R.id.event_description)).setText(item.getOverview());

        if (_isInsideRange(item.getStartYear())) {
            convertView.setBackgroundColor(Color.parseColor("#FFB300"));
        }
        return convertView;
    }

    private boolean _isInsideRange(int eventStartYear) {
        boolean isInside = false;
        if (eventStartYear >= range.get(0)) {
            if (eventStartYear <= range.get(1)) {
                isInside = true;
            }
        }
        return isInside;
    }
}
