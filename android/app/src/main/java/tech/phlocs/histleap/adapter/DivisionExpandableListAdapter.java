package tech.phlocs.histleap.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import tech.phlocs.histleap.R;

public class DivisionExpandableListAdapter extends SimpleExpandableListAdapter {
    private Context context = null;

    public DivisionExpandableListAdapter(Context context,
                                         List<? extends Map<String, ?>> groupData, int groupLayout,
                                         String[] groupFrom, int[] groupTo,
                                         List<? extends List<? extends Map<String, ?>>> childData,
                                         int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupLayout, groupFrom, groupTo, childData,
                childLayout, childLayout, childFrom, childTo);
        this.context = context;
    }
    

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Activity activity = (Activity)this.context;

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.division_list_parent, null);
        }

        ImageView indicatorView = (ImageView) convertView.findViewById(R.id.iv_Indicator);
        if (isExpanded) {
            indicatorView.setImageResource(R.drawable.pull_down2);
        } else {
            indicatorView.setImageResource(R.drawable.pull_down);
        }
        return super.getGroupView(groupPosition, isExpanded, convertView, parent);
    }
}
