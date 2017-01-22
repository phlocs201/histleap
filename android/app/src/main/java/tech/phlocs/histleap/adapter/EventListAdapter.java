package tech.phlocs.histleap.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
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
        // 開始年とイベント概要を、設定
        ((TextView) convertView.findViewById(R.id.event_start_year)).setText(String.valueOf(item.getStartYear()));
        TextView overviewView = (TextView) convertView.findViewById(R.id.event_description);
        overviewView.setText(item.getOverview());

        // 終了年があれば、設定
        String endYearStr = "";
        int endYear = item.getEndYear();
        if (endYear != 0) {
            endYearStr = " 〜 " + endYear;
        }
        ((TextView) convertView.findViewById(R.id.event_end_year)).setText(endYearStr);

        // スライダー範囲内であれば、強調表示
        if (_isInsideRange(item.getStartYear())) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAlmostWhite));
        }
        // spotURLがあれば、リンクを設定
        final String eventUrl = item.getUrl();
        if (eventUrl != null) {
            overviewView.setPaintFlags(overviewView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            overviewView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(eventUrl));
                    context.startActivity(i);
                }
            });
        }

        // 最も下のitemであれば、bottomの境界線を消す
        if (position == getCount() -1) {
            convertView.findViewById(R.id.event_list_border).setVisibility(View.INVISIBLE);
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
