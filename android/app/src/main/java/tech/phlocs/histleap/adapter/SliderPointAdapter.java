package tech.phlocs.histleap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tech.phlocs.histleap.R;
import tech.phlocs.histleap.model.Division;
import tech.phlocs.histleap.model.Slider;

/**
 * Created by matsumura_kazuki on 2017/01/08.
 */

public class SliderPointAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<Division> mDivisions = null;
    private LayoutInflater mLayoutInfrater;
    private Integer[] mSliderPointArray;
    private int size;
    private ArrayList<Integer> range;
    private Slider slider;

    private static class ViewHolder {
        public ImageView sliderPoint;
        public TextView sliderLabel;
        public ImageView sliderBar;
    }

    public SliderPointAdapter(Context context, Slider slider) {
        this.slider = slider;
        mContext = context;
        mLayoutInfrater = LayoutInflater.from(context);
        ArrayList<Division> divisions = slider.getDivisions();
        this.mSliderPointArray = new Integer[divisions.size()];

        this.size = divisions.size();
        this.mDivisions = divisions;
        this.range = slider.getRange();
        for (int i = 0; i < this.size; i++) {
            if (i == range.get(0) || i == range.get(1)) {
                this.mSliderPointArray[i] = R.drawable.slider_lever2;
            } else if (i > this.range.get(0) && i < this.range.get(1)) {
                this.mSliderPointArray[i] = R.drawable.break_point_white;
            } else {
                this.mSliderPointArray[i] = R.drawable.break_point_yellow;
            }
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
            holder.sliderBar = (ImageView)convertView.findViewById(R.id.slider_bar_yellow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (mSliderPointArray[position] == R.drawable.slider_lever2) {
            holder.sliderPoint.setScaleX((float) 1.1);
            holder.sliderPoint.setScaleY((float) 1.1);
        }

        if (!slider.isSamePoint()) {
            if (position > this.range.get(0) && position < this.range.get(1)) {
                holder.sliderBar.setVisibility(View.VISIBLE);
            } else if (position == this.range.get(0)) {
                holder.sliderBar.setImageResource(R.drawable.slider_bar_yellow_start);
                holder.sliderBar.setVisibility(View.VISIBLE);
            } else if (position == this.range.get(1)) {
                holder.sliderBar.setImageResource(R.drawable.slider_bar_yellow_end);
                holder.sliderBar.setVisibility(View.VISIBLE);
            }
        }

        holder.sliderPoint.setImageResource(mSliderPointArray[position]);
        holder.sliderLabel.setText(mDivisions.get(position).getVerticalName());
        return convertView;
    }
}
