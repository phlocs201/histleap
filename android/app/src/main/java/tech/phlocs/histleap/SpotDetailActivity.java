package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import tech.phlocs.histleap.adapter.EventListAdapter;
import tech.phlocs.histleap.adapter.SpotInfoListAdapter;
import tech.phlocs.histleap.async_task.GetImageAsyncTask;
import tech.phlocs.histleap.model.Event;
import tech.phlocs.histleap.model.EventInfo;
import tech.phlocs.histleap.model.SpotInfo;


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

        // 画像を取得
        String urlStr = "https://upload.wikimedia.org/wikipedia/commons/4/40/ShinagawaJinja_Honden.jpg";
        ImageView imageView = (ImageView)findViewById(R.id.iv_spotImage);
        RelativeLayout progressView = (RelativeLayout)findViewById(R.id.progress);
        _getImage(urlStr, imageView, progressView);

        // スポット情報リストにデータを登録
        _setDataToSpotInfoList();

        // イベントリストにデータを登録
        _setDataToEventList();
    }

    private void _getImage(String urlStr, ImageView imageView, RelativeLayout progressView) {
        Uri uri = Uri.parse(urlStr);
        Uri.Builder builder = uri.buildUpon();
        GetImageAsyncTask task = new GetImageAsyncTask(imageView, progressView);
        task.execute(builder);
    }

    private void _setDataToSpotInfoList() {
        // SpotInfoListにデータを登録
        String titles[] = {"住所", "概要"};
        String contents[] = {"〒140-0001 東京都品川区北品川3-7-15",
                "元准勅祭社として東京十社のひとつでもある。 また東海七福神の一社として、大黒天を祀る。"};
        ArrayList<SpotInfo> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            SpotInfo spotInfo = new SpotInfo();
            spotInfo.setId((new Random()).nextLong());
            spotInfo.setTitle(titles[i]);
            spotInfo.setContent(contents[i]);
            data.add(spotInfo);
        }
        SpotInfoListAdapter adapter = new SpotInfoListAdapter(this, data, R.layout.spot_info);
        ListView spotInfoListView = (ListView) findViewById(R.id.lv_spotInfoList);
        spotInfoListView.setAdapter(adapter);
    }

    private void _setDataToEventList() {
        // EventListにデータを登録
        String years[] = {"1193", "1300", "1735"};
        ArrayList<EventInfo> data = new ArrayList<>();
        for (int i = 0; i < years.length; i++) {
            EventInfo event = new EventInfo();
            event.setId((new Random()).nextLong());
            event.setYear(years[i]);
            event.setDescription("ダミーテキストです。この文字は全角８０文字ですよ。" +
                    "ダミーテキストです。この文字は全角８０文字ですよ。" +
                    "ダミーテキストです。この文字は全角８０文字ですよ。");
            data.add(event);
        }
        EventListAdapter adapter = new EventListAdapter(this, data, R.layout.event);
        ListView eventList = (ListView) findViewById(R.id.lv_eventList);
        eventList.setAdapter(adapter);
    }
}
