package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;

import tech.phlocs.histleap.adapter.EventListAdapter;
import tech.phlocs.histleap.adapter.SpotInfoListAdapter;
import tech.phlocs.histleap.async_task.GetImageAsyncTask;
import tech.phlocs.histleap.model.Event;
import tech.phlocs.histleap.list_item.SpotInfoListItem;
import tech.phlocs.histleap.model.Spot;
import tech.phlocs.histleap.util.JsonHandler;


public class SpotDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);

        

        // インテントを取得
        Intent intent = this.getIntent();
        //String spot = intent.getStringExtra("spot");
        //TextView tv_spotName = (TextView)findViewById(R.id.tv_spotName);
        //tv_spotName.setText(spot);

        // ダミーデータを取得
        Spot currentSpot = _getDummySpot();
        ArrayList<Integer> range = new ArrayList<>();
        range.add(1700);
        range.add(1899);

        // ヘッダー文言を設定
        _setHeaderText(currentSpot.getName());

        // 画像を非同期に取得
        String urlStr = currentSpot.getImageUrl();
        RelativeLayout progressContainer = (RelativeLayout) findViewById(R.id.progress);
        ImageView imageView = (ImageView) findViewById(R.id.iv_spotImage);
        RelativeLayout noImageContainer = (RelativeLayout)findViewById(R.id.noImageContainer);

        if ("".equals(urlStr)) {
            progressContainer.setVisibility(View.INVISIBLE);
            noImageContainer.setVisibility(View.VISIBLE);
        } else {
            noImageContainer.setVisibility(View.INVISIBLE);
            _getImage(urlStr, imageView, progressContainer);
        }

        // スポット情報リストにデータを登録
        _setDataToSpotInfoList(currentSpot);

        // イベントリストにデータを登録
        _setDataToEventList(currentSpot, range);
    }

    private Spot _getDummySpot() {
        JsonHandler jh = new JsonHandler(this);
        JSONObject json = jh.makeJsonFromRawFile(R.raw.spots);
        JSONArray spotObjArray = jh.getJsonArrayInJson(json, "spots");
        ArrayList<JSONObject> spotObjList = jh.makeArrayListFromJsonArray(spotObjArray);
        ArrayList<Spot> spots = new ArrayList<>();

        for (int i = 0; i < spotObjList.size(); i++) {
            spots.add(jh.makeSpotFromJson(spotObjList.get(i)));
        }
        int random = (int)(Math.random()*15);
        //random = 1;
        return spots.get(random);
    }

    private void _setHeaderText(String txt) {
        TextView txtView = (TextView) findViewById(R.id.tv_spotName);
        txtView.setText(txt);
    }

    private void _getImage(String urlStr, ImageView imageView, RelativeLayout progressView) {
        Uri uri = Uri.parse(urlStr);
        Uri.Builder builder = uri.buildUpon();
        GetImageAsyncTask task = new GetImageAsyncTask(imageView, progressView);
        task.execute(builder);
    }

    // SpotInfoListにデータを登録
    private void _setDataToSpotInfoList(Spot spot) {
        String titles[] = {"住所", "概要"};
        String contents[] = {spot.getAddress(), spot.getOverview()};
        int iconSrcs[] = {R.drawable.pin, R.drawable.description};
        String urls[] = {null, spot.getUrl()};

        ArrayList<SpotInfoListItem> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            SpotInfoListItem spotInfo = new SpotInfoListItem();
            spotInfo.setId((new Random()).nextLong());
            spotInfo.setTitle(titles[i]);
            spotInfo.setContent(contents[i]);
            spotInfo.setIconSrc(iconSrcs[i]);
            spotInfo.setUrl(urls[i]);
            data.add(spotInfo);
        }
        SpotInfoListAdapter adapter = new SpotInfoListAdapter(this, data, R.layout.list_item_spot_info);
        ListView spotInfoListView = (ListView) findViewById(R.id.lv_spotInfoList);
        spotInfoListView.setAdapter(adapter);
    }

    // EventListにデータを登録
    private void _setDataToEventList(Spot spot, ArrayList<Integer> range) {
        ArrayList<Event> data = spot.getEventList();
        EventListAdapter adapter = new EventListAdapter(this, data, R.layout.list_item_event, range);
        ListView eventList = (ListView) findViewById(R.id.lv_eventList);
        eventList.setAdapter(adapter);
    }
}
