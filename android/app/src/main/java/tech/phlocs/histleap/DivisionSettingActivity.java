package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.phlocs.histleap.adapter.DivisionExpandableListAdapter;
import tech.phlocs.histleap.list_item.DivisionChildListItem;
import tech.phlocs.histleap.list_item.DivisionParentListItem;
import tech.phlocs.histleap.model.Division;
import tech.phlocs.histleap.model.DivisionSet;
import tech.phlocs.histleap.util.JsonHandler;


public class DivisionSettingActivity extends Activity {
    ExpandableListView ex_listView;
    long selectedDivisionSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division_setting);
        // インテントを取得
        Intent intent = this.getIntent();
        // 現在選択された時代区分を取得
        selectedDivisionSetId = 2;

        ex_listView = (ExpandableListView) findViewById(R.id.elv_divisionList);

        JsonHandler jh = new JsonHandler(this);
        JSONObject json = jh.makeJsonFromRawFile(R.raw.preset_division_sets);
        ArrayList<DivisionSet> divisionSets = jh.makeDivisionSetsFromJson(json);

//        ArrayList<DivisionSet> divisionSets = new ArrayList<>();
//        JSONArray divisionSetsArray = jh.getJsonArrayInJson(json, "divisionSets");
//        ArrayList<JSONObject> divisionSetObjs = jh.makeArrayListFromJsonArray(divisionSetsArray);
//        for (int i = 0; i < divisionSetObjs.size(); i++) {
//            divisionSets.add(jh.makeDivisionSetFromJson(divisionSetObjs.get(i)));
//        }

//        String[] divisionSetNames = {
//                "日本史時代区分",
//                "1990年代以降"
//        };
//        long[] divisionSetIds = {
//                42,
//                1990
//        };
//
//        String[][] divisionNames = {
//            {
//                "飛鳥以前",
//                "奈良",
//                "平安",
//                "鎌倉",
//                "南北朝",
//                "室町",
//                "安土桃山",
//                "江戸",
//                "明治・大正",
//                "昭和",
//                "平成"
//            },
//            {
//                "1900s",
//                "1910s",
//                "1920s",
//                "1930s",
//                "1940s",
//                "1950s",
//                "1960s",
//                "1970s",
//                "1980s",
//                "1990s",
//            }
//            };
//        int[][][] years = {
//            {
//                {0, 710},
//                {710, 719},
//                {719, 1185},
//                {1185, 1336},
//                {1336, 1392},
//                {1392, 1573},
//                {1573, 1603},
//                {1603, 1868},
//                {1868, 1926},
//                {1926, 1989},
//                {1989, 2017}
//            },
//            {
//                {1900, 1909},
//                {1910, 1919},
//                {1920, 1929},
//                {1930, 1939},
//                {1940, 1949},
//                {1950, 1959},
//                {1960, 1969},
//                {1970, 1979},
//                {1980, 1989},
//                {1990, 2000}
//            }
//        };


        ArrayList<DivisionParentListItem> parentList = new ArrayList<>();
        ArrayList<List<DivisionChildListItem>> childList = new ArrayList<>();

        for (int i = 0; i < divisionSets.size(); i++) {
            DivisionParentListItem parentItem = new DivisionParentListItem();
            DivisionSet divisionSet = divisionSets.get(i);
            parentItem.setId(i);
            parentItem.setName(divisionSet.getName());

            // 現在の時代区分を選択状態にする
            if (parentItem.getId() == selectedDivisionSetId) {
                parentItem.setSelected(true);
            } else {
                parentItem.setSelected(false);
            }
            parentList.add(parentItem);
            ArrayList<DivisionChildListItem> childItems = new ArrayList<>();

            ArrayList<Division> divisions = divisionSet.getDivisions();
            for (int j = 0; j < divisions.size(); j++) {
                DivisionChildListItem childItem = new DivisionChildListItem();
                Division division = divisions.get(j);
                childItem.setName(division.getName());
                childItem.setStart(division.getStart());
                childItem.setEnd(division.getEnd());
                childItem.setId(j);
                childItems.add(childItem);
            }
            childList.add(childItems);
        }

        // Adapterを準備
        DivisionExpandableListAdapter adapter = new DivisionExpandableListAdapter(
                this,
                parentList,
                childList,
                selectedDivisionSetId
        );
        ex_listView.setAdapter(adapter);
        // 子項目クリック時のEventListener
        ex_listView.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                        return false;
                    }
                }
        );
    }

    public void setSelectedDivisionSetId(long selectedId) {
        selectedDivisionSetId = selectedId;
    }

}
