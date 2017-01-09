package tech.phlocs.histleap.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import tech.phlocs.histleap.R;
import tech.phlocs.histleap.list_item.DivisionListChildItem;
import tech.phlocs.histleap.list_item.DivisionListParentItem;
import tech.phlocs.histleap.list_item.SpotInfoListItem;

public class DivisionExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context = null;
    private List<DivisionListParentItem> groupData;
    private List<List<DivisionListChildItem>> childData;
    private long selectedDivisionSetId = 0;

    public DivisionExpandableListAdapter(Context context, List<DivisionListParentItem> groupData,
                                         List<List<DivisionListChildItem>> childData, long selectedId) {
        this.context = context;
        this.groupData = groupData;
        this.childData = childData;
        this.selectedDivisionSetId = selectedId;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition).getId();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition).size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Activity activity = (Activity)context;
        DivisionListChildItem item = childData.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.division_list_child_item, null);
        }
        ((TextView) convertView.findViewById(R.id.tv_divisionName)).setText(item.getName());
        ((TextView) convertView.findViewById(R.id.tv_divisionStart)).setText(String.valueOf(item.getStart()));
        ((TextView) convertView.findViewById(R.id.tv_divisionEnd)).setText(String.valueOf(item.getEnd()));
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupData.get(groupPosition).getId();
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Activity activity = (Activity)context;
        DivisionListParentItem item = groupData.get(groupPosition);

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.division_list_parent_item, null);
        }

        // 名前を設定
        ((TextView) convertView.findViewById(R.id.tv_divisionSetName)).setText(item.getName());

        // その他の部品を取得
        final RadioButton radioButton = ((RadioButton) convertView.findViewById(R.id.rb_division));
        final ImageView indicatorView = (ImageView)convertView.findViewById(R.id.iv_Indicator);

        final ExpandableListView ex_listView = (ExpandableListView)parent;

        // 矢印画像を設定
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

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
