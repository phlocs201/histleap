package tech.phlocs.histleap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import tech.phlocs.histleap.adapter.EventListAdapter;
import tech.phlocs.histleap.adapter.SpotInfoListAdapter;
import tech.phlocs.histleap.async_task.GetImageAsyncTask;
import tech.phlocs.histleap.model.Event;
import tech.phlocs.histleap.list_item.SpotInfoListItem;
import tech.phlocs.histleap.model.Spot;


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

        // ヘッダー文言を設定
        _setHeaderText(currentSpot.getName());

        // 画像を非同期に取得
        String urlStr = currentSpot.getImageUrl();
        ImageView imageView = (ImageView)findViewById(R.id.iv_spotImage);
        RelativeLayout progressView = (RelativeLayout)findViewById(R.id.progress);
        _getImage(urlStr, imageView, progressView);

        // スポット情報リストにデータを登録
        _setDataToSpotInfoList(currentSpot);

        // イベントリストにデータを登録
        _setDataToEventList(currentSpot);
    }

    private Spot _getDummySpot() {
        // EventListを用意
        int years[] = {1193, 1300, 1735};
        ArrayList<Event> eventList = new ArrayList<>();
        for (int i = 0; i < years.length; i++) {
            Event event = new Event();
            event.setId((new Random()).nextLong());
            event.setStartYear(years[i]);
            event.setOverview("ダミーテキストです。この文字は全角８０文字ですよ。" +
                    "ダミーテキストです。この文字は全角８０文字ですよ。" +
                    "ダミーテキストです。この文字は全角８０文字ですよ。");
            eventList.add(event);
        }

        Spot spot = new Spot(
                String.valueOf((new Random()).nextLong()),
                "品川神社",
                0,
                0,
                "〒140-0001 東京都品川区北品川 3-7-15",
                "http://google.com",
                "https://upload.wikimedia.org/wikipedia/commons/4/40/ShinagawaJinja_Honden.jpg",
                "元准勅祭社として東京十社のひとつでもある。 また東海七福神の一社として、大黒天を祀る。",
                eventList);
        return spot;
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

        ArrayList<SpotInfoListItem> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            SpotInfoListItem spotInfo = new SpotInfoListItem();
            spotInfo.setId((new Random()).nextLong());
            spotInfo.setTitle(titles[i]);
            spotInfo.setContent(contents[i]);
            spotInfo.setIconSrc(iconSrcs[i]);
            data.add(spotInfo);
        }
        SpotInfoListAdapter adapter = new SpotInfoListAdapter(this, data, R.layout.list_item_spot_info);
        ListView spotInfoListView = (ListView) findViewById(R.id.lv_spotInfoList);
        spotInfoListView.setAdapter(adapter);
    }

    // EventListにデータを登録
    private void _setDataToEventList(Spot spot) {
        ArrayList<Event> data = spot.getEventList();
        EventListAdapter adapter = new EventListAdapter(this, data, R.layout.list_item_event);
        ListView eventList = (ListView) findViewById(R.id.lv_eventList);
        eventList.setAdapter(adapter);
    }
}
