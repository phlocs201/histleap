package tech.phlocs.histleap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by matsumura_kazuki on 2017/01/08.
 */

public class SliderPointAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInfrater;
    private int size;
    private Integer[] mSliderPointArray;

    private static class ViewHolder {
        public ImageView sliderPoint;
    }

    public SliderPointAdapter(Context context, int size) {
        mContext = context;
        mLayoutInfrater = LayoutInflater.from(context);
        this.size = size;
        Log.d("hoge", Integer.toString(size));
        this.mSliderPointArray = new Integer[size];
        for (int i = 0; i < size; i++) {
            this.mSliderPointArray[i] = R.drawable.break_point_yellow;
        }
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mSliderPointArray[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInfrater.inflate(R.layout.slider_point, null);
            holder = new ViewHolder();
            holder.sliderPoint = (ImageView)convertView.findViewById(R.id.slider_point);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        Log.d("hoge", mSliderPointArray[position].toString());
        holder.sliderPoint.setImageResource(mSliderPointArray[position]);

        return convertView;
    }
}
