package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;

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
    int currentDivisionSetIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division_setting);
        // インテントを取得
        Intent intent = this.getIntent();
        // 現在選択された時代区分を取得
        currentDivisionSetIndex = intent.getIntExtra("currentDivisionSetIndex", 0);

        // 時代区分リストにデータを登録
        _setDataToDivisionList();
    }
    private void _setDataToDivisionList() {
        ex_listView = (ExpandableListView) findViewById(R.id.elv_divisionList);

        JsonHandler jh = new JsonHandler(this);
        JSONObject json = jh.makeJsonFromRawFile(R.raw.preset_division_sets);
        ArrayList<DivisionSet> divisionSets = jh.makeDivisionSetsFromJson(json);
        ArrayList<DivisionParentListItem> parentList = new ArrayList<>();
        ArrayList<List<DivisionChildListItem>> childList = new ArrayList<>();

        for (int i = 0; i < divisionSets.size(); i++) {
            DivisionParentListItem parentItem = new DivisionParentListItem();
            DivisionSet divisionSet = divisionSets.get(i);
            parentItem.setId(i);
            parentItem.setName(divisionSet.getName());

            // 現在の時代区分を選択状態にする
            if (parentItem.getId() == currentDivisionSetIndex) {
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
                currentDivisionSetIndex
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

    public void setCurrentDivisionSetIndex(int currentIndex) {
        currentDivisionSetIndex = currentIndex;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 戻るボタン押下時の処理
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent();
            i.putExtra("currentDivisionSetIndex", currentDivisionSetIndex);
            setResult(RESULT_OK, i);
        }
        return super.onKeyDown(keyCode, event);
    }
}
