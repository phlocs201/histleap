package tech.phlocs.histleap.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tech.phlocs.histleap.R;
import tech.phlocs.histleap.model.SpotInfo;

public class SpotInfoListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<SpotInfo> data = null;
    private int resource = 0;

    // コンストラクタ
    public SpotInfoListAdapter(Context context, ArrayList<SpotInfo> data, int resource) {
        this.context  = context;
        this.data     = data;
        this.resource = resource;
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
        SpotInfo item = (SpotInfo)getItem(position);
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }
        ((TextView) convertView.findViewById(R.id.spot_info_title)).setText(item.getTitle());
        ((TextView) convertView.findViewById(R.id.spot_info_content)).setText(item.getContent());
        return convertView;
    }
}
