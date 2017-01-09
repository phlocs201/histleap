package tech.phlocs.histleap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tech.phlocs.histleap.model.Division;

/**
 * Created by matsumura_kazuki on 2017/01/08.
 */

public class SliderPointAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Division> mDivisions;
    private LayoutInflater mLayoutInfrater;
    private Integer[] mSliderPointArray;
    private int size;

    private static class ViewHolder {
        public ImageView sliderPoint;
        public TextView sliderLabel;
    }

    public SliderPointAdapter(Context context, ArrayList<Division> divisions) {
        mContext = context;
        mLayoutInfrater = LayoutInflater.from(context);
        this.mSliderPointArray = new Integer[divisions.size()];

        this.size = divisions.size();
        this.mDivisions = divisions;

        for (int i = 0; i < this.size; i++) {
            this.mSliderPointArray[i] = R.drawable.break_point_yellow;
        }
    }

    @Override
    public int getCount() {return this.size;}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mDivisions.get(position).getName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInfrater.inflate(R.layout.slider_point, null);
            holder = new ViewHolder();
            holder.sliderPoint = (ImageView)convertView.findViewById(R.id.slider_break_point);
            holder.sliderLabel = (TextView)convertView.findViewById(R.id.slider_label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.sliderPoint.setImageResource(mSliderPointArray[position]);
        holder.sliderLabel.setText(mDivisions.get(position).getVerticalName());
        return convertView;
    }
}
