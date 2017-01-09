package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.phlocs.histleap.adapter.DivisionExpandableListAdapter;
import tech.phlocs.histleap.list_item.DivisionSetItem;
import tech.phlocs.histleap.model.Division;


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
        String[][][] years = {
            {
                {"0", "710"},
                {"710", "719"},
                {"719", "1185"},
                {"1185", "1336"},
                {"1336", "1392"},
                {"1392", "1573"},
                {"1573", "1603"},
                {"1603", "1868"},
                {"1868", "1926"},
                {"1926", "1989"},
                {"1989", "2017"}
            },
            {
                {"1900", "1909"},
                {"1910", "1919"},
                {"1920", "1929"},
                {"1930", "1939"},
                {"1940", "1949"},
                {"1950", "1959"},
                {"1960", "1969"},
                {"1970", "1979"},
                {"1980", "1989"},
                {"1990", "2000"}
            }
        };
        selectedDivisionSetId = 1990;


        ArrayList<Map<String, String>> list_parent = new ArrayList<>();
        ArrayList<List<Map<String, String>>> list_child = new ArrayList<>();

        for (int i = 0; i < divisionSetNames.length; i++) {
            HashMap<String, String> set = new HashMap<>();
            set.put("divisionSet_name", divisionSetNames[i]);
            list_parent.add(set);
            ArrayList<Map<String, String>> divisions = new ArrayList<>();

            for (int j = 0; j < divisionNames[i].length; j++) {
                HashMap<String, String> division = new HashMap<>();
                division.put("division_name", divisionNames[i][j]);
                division.put("division_start", years[i][j][0]);
                division.put("division_end", years[i][j][1]);
                divisions.add(division);
            }
            list_child.add(divisions);
        }

        // Adapterを準備
        DivisionExpandableListAdapter adapter = new DivisionExpandableListAdapter(
                this,
                list_parent,
                R.layout.division_list_parent,
                new String[] {"divisionSet_name"},
                new int[] {R.id.tv_divisionSetName},
                list_child,
                R.layout.division_list_sub_item,
                new String[] {"division_name", "division_start", "division_end"},
                new int[] {R.id.tv_divisionName, R.id.tv_divisionStart, R.id.tv_divisionEnd},
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

    public void onClickDivisionRadioButton(View view) {
        Log.d("@@@", "radioButton clicked");
    }
}
