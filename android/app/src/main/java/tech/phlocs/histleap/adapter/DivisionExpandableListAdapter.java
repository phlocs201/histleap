package tech.phlocs.histleap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleExpandableListAdapter;

import java.util.List;
import java.util.Map;

import tech.phlocs.histleap.R;

public class DivisionExpandableListAdapter extends SimpleExpandableListAdapter {
    private Context context = null;
    private long selectedDivisionSetId = 0;

    public DivisionExpandableListAdapter(Context context,
                                         List<? extends Map<String, ?>> groupData, int groupLayout,
                                         String[] groupFrom, int[] groupTo,
                                         List<? extends List<? extends Map<String, ?>>> childData,
                                         int childLayout, String[] childFrom, int[] childTo, long selectedId) {
        super(context, groupData, groupLayout, groupLayout, groupFrom, groupTo, childData,
                childLayout, childLayout, childFrom, childTo);
        this.context = context;
        this.selectedDivisionSetId = selectedId;
    }
    

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        View groupView = super.getGroupView(groupPosition, isExpanded, convertView, parent);

        final ExpandableListView ex_listView = (ExpandableListView)parent;

        // 矢印画像を設定
        final ImageView indicatorView = (ImageView)groupView.findViewById(R.id.iv_Indicator);
        if (ex_listView.isGroupExpanded(groupPosition)) {
            indicatorView.setImageResource(R.drawable.indicator_up);
        } else {
            indicatorView.setImageResource(R.drawable.indicator_down);
        }
        // 矢印画像に、クリックイベントを付与
        indicatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("@@@", "groupPosition:" + groupPosition + ", isGroupExpanded: " + ex_listView.isGroupExpanded(groupPosition));
                if (ex_listView.isGroupExpanded(groupPosition)) {
                    ex_listView.collapseGroup(groupPosition);
                    ((ImageView)view).setImageResource(R.drawable.indicator_down);
                    Log.d("@@@", "set DOWN image");
                } else {
                    ex_listView.expandGroup(groupPosition);
                    ((ImageView)view).setImageResource(R.drawable.indicator_up);
                    Log.d("@@@", "set UP image");
                }
            }
        });

        return groupView;
    }
}
