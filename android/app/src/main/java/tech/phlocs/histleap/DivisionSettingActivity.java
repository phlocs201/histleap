package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tech.phlocs.histleap.adapter.DivisionExpandableListAdapter;
import tech.phlocs.histleap.list_item.DivisionChildListItem;
import tech.phlocs.histleap.list_item.DivisionParentListItem;


public class DivisionSettingActivity extends Activity {
    ExpandableListView ex_listView;
    long selectedDivisionSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division_setting);
        // インテントを取得
        Intent intent = this.getIntent();

        ex_listView = (ExpandableListView) findViewById(R.id.elv_divisionList);

        String[] divisionSetNames = {
                "日本史時代区分",
                "1990年代以降"
        };
        long[] divisionSetIds = {
                42,
                1990
        };

        String[][] divisionNames = {
            {
                "飛鳥以前",
                "奈良",
                "平安",
                "鎌倉",
                "南北朝",
                "室町",
                "安土桃山",
                "江戸",
                "明治・大正",
                "昭和",
                "平成"
            },
            {
                "1900s",
                "1910s",
                "1920s",
                "1930s",
                "1940s",
                "1950s",
                "1960s",
                "1970s",
                "1980s",
                "1990s",
            }
            };
        int[][][] years = {
            {
                {0, 710},
                {710, 719},
                {719, 1185},
                {1185, 1336},
                {1336, 1392},
                {1392, 1573},
                {1573, 1603},
                {1603, 1868},
                {1868, 1926},
                {1926, 1989},
                {1989, 2017}
            },
            {
                {1900, 1909},
                {1910, 1919},
                {1920, 1929},
                {1930, 1939},
                {1940, 1949},
                {1950, 1959},
                {1960, 1969},
                {1970, 1979},
                {1980, 1989},
                {1990, 2000}
            }
        };
        selectedDivisionSetId = 1990;


        ArrayList<DivisionParentListItem> parentList = new ArrayList<>();
        ArrayList<List<DivisionChildListItem>> childList = new ArrayList<>();

        for (int i = 0; i < divisionSetNames.length; i++) {
            DivisionParentListItem parentItem = new DivisionParentListItem(divisionSetNames[i], false);
            parentItem.setId((new Random()).nextLong());
            parentList.add(parentItem);
            ArrayList<DivisionChildListItem> childItems = new ArrayList<>();

            for (int j = 0; j < divisionNames[i].length; j++) {
                DivisionChildListItem childItem = new DivisionChildListItem();
                childItem.setName(divisionNames[i][j]);
                childItem.setStart(years[i][j][0]);
                childItem.setEnd(years[i][j][1]);
                childItem.setId((new Random()).nextLong());
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

}
