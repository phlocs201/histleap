package tech.phlocs.histleap.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import tech.phlocs.histleap.DivisionSettingActivity;
import tech.phlocs.histleap.R;
import tech.phlocs.histleap.list_item.DivisionChildListItem;
import tech.phlocs.histleap.list_item.DivisionParentListItem;

public class DivisionExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context = null;
    private List<DivisionParentListItem> groupData;
    private List<List<DivisionChildListItem>> childData;
    private long selectedDivisionSetIndex = 0;

    public DivisionExpandableListAdapter(Context context, List<DivisionParentListItem> groupData,
                                         List<List<DivisionChildListItem>> childData, long selectedId) {
        this.context = context;
        this.groupData = groupData;
        this.childData = childData;
        this.selectedDivisionSetIndex = selectedId;
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
        DivisionChildListItem item = childData.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item_division_child, null);
        }
        // 時代区分名を設定
        ((TextView) convertView.findViewById(R.id.tv_divisionName)).setText(item.getName());

        // "N 年"を設定
        String startStr = _makeTextOfYear(item.getStart());
        String endStr = _makeTextOfYear(item.getEnd());
        ((TextView) convertView.findViewById(R.id.tv_divisionStart)).setText(startStr);
        ((TextView) convertView.findViewById(R.id.tv_divisionEnd)).setText(endStr);
        return convertView;
    }

    private String _makeTextOfYear(int year) {
        String str = "";
        if (year != 0) {
            str = String.valueOf(year) + " 年";
        }
        return str;
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
        final DivisionSettingActivity activity = (DivisionSettingActivity)context;
        final DivisionParentListItem item = groupData.get(groupPosition);

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item_division_parent, null);
        }

        // 名前を設定
        ((TextView) convertView.findViewById(R.id.tv_divisionSetName)).setText(item.getName());


        final ExpandableListView ex_listView = (ExpandableListView)parent;

        // 矢印画像を設定
        final ImageView indicatorView = (ImageView)convertView.findViewById(R.id.iv_Indicator);
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
                } else {
                    ex_listView.expandGroup(groupPosition);
                    ((ImageView)view).setImageResource(R.drawable.indicator_up);
                    Log.d("@@@", "set UP image");
                }
            }
        });

        // ラジオボタンを設定
        final RadioButton radioButton = ((RadioButton) convertView.findViewById(R.id.rb_division));
        radioButton.setChecked(item.isSelected());

        // ラジオボタンにクリックイベントを付与
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < groupData.size(); i++) {
                    groupData.get(i).setSelected(false);
                }
                item.setSelected(true);
                // 選択された時代区分を、Activity側に渡す
                ((DivisionSettingActivity) context).setCurrentDivisionSetIndex((int)item.getId());
                notifyDataSetChanged();
            }
        });

        if (item.isSelected()) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBasicBeige));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
