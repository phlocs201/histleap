package tech.phlocs.histleap;

import android.Manifest;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import tech.phlocs.histleap.adapter.SliderPointAdapter;
import tech.phlocs.histleap.model.Division;
import tech.phlocs.histleap.model.DivisionSet;
import tech.phlocs.histleap.model.Slider;
import tech.phlocs.histleap.model.SliderArea;
import tech.phlocs.histleap.model.Spot;
import tech.phlocs.histleap.util.JsonHandler;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Slider slider;
    private GestureDetector gd;
    private SliderArea sa;
    private ArrayList<Spot> spots;
    private ArrayList<Marker> markers = new ArrayList<>();
    private RelativeLayout saLayout;
    private ArrayList<DivisionSet> divisionSets;
    private int currentDivisionSetIndex;
    public static final String PREFS_NAME = "SliderPreference";
    private int scrollFrom = -1;
    private boolean firstOnDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gd = new GestureDetector(this,
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    int point = sa.getPosition((int)e.getX(), (int)e.getY());
                    if (point != -1) {
                        handleClickToChangeSliderRange(point);
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    firstOnDown = true;
                    int point = sa.getPosition((int)e.getX(), (int)e.getY());
                    scrollFrom = -1;
                    if (point != -1) {
                        handleClickToChangeSliderRange(point);
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (!firstOnDown) {
                        return false;
                    }
                    int from = sa.getPosition((int) e1.getX(), (int) e1.getY());
                    int to = sa.getPosition((int) e2.getX(), (int) e2.getY());
                    if (scrollFrom != -1) {
                        if ((from != -1 || to != -1) &&
                                (scrollFrom != to) &&
                                to < slider.getDivisions().size() &&
                                scrollFrom >= 0) {
                            handleScrollToChangeSliderRange(scrollFrom, to);
                            scrollFrom = to;
                            return true;
                        } else {
                            return false;
                        }
                    }
                    scrollFrom = to;
                    if ((from != -1 || to != -1) &&
                            (from != to) &&
                            to < slider.getDivisions().size() &&
                            from >= 0) {
                        handleScrollToChangeSliderRange(from, to);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        );
        // 時代区分セットをjsonから取得
        JsonHandler jh = new JsonHandler(this);
        JSONObject json = jh.makeJsonFromRawFile(R.raw.preset_division_sets);
        divisionSets = jh.makeDivisionSetsFromJson(json);
        this.currentDivisionSetIndex = 0;
        
        DivisionSet divisionSet = divisionSets.get(this.currentDivisionSetIndex);

        ArrayList<Division> divisions = divisionSet.getDivisions();
        this.slider = new Slider(divisions);

        // spotsをセットから取得
        JSONObject jsonSpots = jh.makeJsonFromRawFile(R.raw.spots);
        JSONArray spotObjArray = jh.getJsonArrayInJson(jsonSpots, "spots");
        ArrayList<JSONObject> spotObjList = jh.makeArrayListFromJsonArray(spotObjArray);
        this.spots = new ArrayList<>();
        for (int i = 0; i < spotObjList.size(); i++) {
            spots.add(jh.makeSpotFromJson(spotObjList.get(i)));
        }
        // ボタンの設定
        _setTouchListeners();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int position = sa.getPosition((int)event.getX(), (int)event.getY());
        if (position == -1 || saLayout.getVisibility() == View.INVISIBLE) {
            super.dispatchTouchEvent(event);
            return true;
        }
        gd.onTouchEvent(event);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        GridView sliderPoints = (GridView) findViewById(R.id.slider_points);
        int sliderAreaWidth = sliderPoints.getWidth();
        sliderPoints.setColumnWidth(sliderAreaWidth / slider.getDivisions().size());
        sliderPoints.setAdapter(new SliderPointAdapter(this, slider));

        final int[] anchorPos = new int[2];
        sliderPoints.getLocationOnScreen(anchorPos);

        sa = new SliderArea(
                anchorPos[1],
                sliderPoints.getHeight(),
                sliderAreaWidth,
                slider.getDivisions().size()
        );
        saLayout = (RelativeLayout)findViewById(R.id.slider_area);
        resetMarkers();
        changeHeaderText();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // FIXME: 一時的に、スポットの多い北品川駅に設定
        LatLng initialLocation = new LatLng(35.622551,139.7371093);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, (float)15));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String spotName = marker.getTitle();
                _startSpotDetailActivity(spotName);
                // info windowを表示する場合は、falseにする
                return true;
            }
        });
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Log.d("@@@", "ACCESS_FINE_LOCATION = false");
                }
            }
        }
    }

    public void onClickToggleSlider(View view) {
        View sliderAreaShadow = findViewById(R.id.slider_area_shadow);
        if (saLayout.getVisibility() == View.VISIBLE) {
            saLayout.setVisibility(View.INVISIBLE);
            sliderAreaShadow.setVisibility(View.INVISIBLE);
        } else {
            saLayout.setVisibility(View.VISIBLE);
            sliderAreaShadow.setVisibility(View.VISIBLE);
        }
    }

    private void _startSpotDetailActivity(String spotName) {
        Intent i = new Intent(this, SpotDetailActivity.class);
        i.putExtra("spotName", spotName);
        ArrayList<Integer> edgeYear = slider.getEdgeYear();
        int startYear = (int)edgeYear.get(0);
        int endYear = (int)edgeYear.get(1);

        // 最後のdivisionにendYearが無い場合
        if (endYear == 0) {
            endYear = 9999;
        }
        i.putExtra("startYear", startYear);
        i.putExtra("endYear", endYear);
        startActivityForResult(i, 1);
    }

    public void btn_settingOnClick(View view) {
        Intent i = new Intent(this, DivisionSettingActivity.class);
        i.putExtra("currentDivisionSetIndex", currentDivisionSetIndex);
        startActivityForResult(i, 1);
    }

    public boolean handleClickToChangeSliderRange(int position) {
        slider.setRangeByPushPoint(position);
        GridView sliderPoints = (GridView) findViewById(R.id.slider_points);
        if (saLayout.getVisibility() == View.INVISIBLE) {
            return true;
        }
        sliderPoints.setAdapter(new SliderPointAdapter(MapsActivity.this, slider));
        resetMarkers();
        changeHeaderText();
        return false;
    }

    public boolean handleScrollToChangeSliderRange(int from , int to) {
        slider.setRangeByScroll(from, to);
        GridView sliderPoints = (GridView) findViewById(R.id.slider_points);
        sliderPoints.setAdapter(new SliderPointAdapter(MapsActivity.this, slider));
        resetMarkers();
        changeHeaderText();
        return false;
    }

    private void resetMarkers() {
        for (Marker m : this.markers) {
            m.remove();
        }
        markers = new ArrayList<>();
        ArrayList<Spot> filteredSpots = this.slider.getFilteredSpots(this.spots);
        for (Spot s : filteredSpots) {
            this.markers.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLatitude(), s.getLongitude()))
// マーカータップ時の吹き出しを仮削除
                    .title(s.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_red))
            ));
        }
    }

    public void changeHeaderText() {
        TextView headerText = (TextView) findViewById(R.id.range);
        String t;
        if (slider.isSamePoint()) {
            t = slider.getDivisions().get(slider.getRange().get(0)).getName();
        } else {
            t = slider.getDivisions().get(slider.getRange().get(0)).getName()
                + " 〜 "
                + slider.getDivisions().get(slider.getRange().get(1)).getName();
        }

        if (t.length() > 15) {
            headerText.setTextSize(View.resolveSize(15, View.SYSTEM_UI_FLAG_LOW_PROFILE));
        } else {
            headerText.setTextSize(View.resolveSize(18, View.SYSTEM_UI_FLAG_LOW_PROFILE));
        }

        headerText.setText(t);
    }
    public void _setTouchListeners() {
        // 設定ボタン
        View btn_settingView = findViewById(R.id.btn_setting);
        btn_settingView.setOnTouchListener(new SettingTouchListener());
        View img_settingView = findViewById(R.id.img_setting);
        img_settingView.setOnTouchListener(new SettingTouchListener());
        // スライダーボタン
        View btn_sliderView = findViewById(R.id.btn_slider);
        btn_sliderView.setOnTouchListener(new SliderButtonTouchListener());
        View img_sliderView = findViewById(R.id.img_slider);
        img_sliderView.setOnTouchListener(new SliderButtonTouchListener());

    }
    private class SettingTouchListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            ImageView img_settingView = (ImageView) findViewById(R.id.img_setting);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    img_settingView.setImageResource(R.drawable.setting_pushed);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    img_settingView.setImageResource(R.drawable.setting_white);
                    break;
            }
            return false;
        }
    }
    private class SliderButtonTouchListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            ImageView img_sliderView = (ImageView) findViewById(R.id.img_slider);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    img_sliderView.setImageResource(R.drawable.slider_pushed);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    img_sliderView.setImageResource(R.drawable.slider_white);
                    break;
            }
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // スライダー設定画面で選択したDivisionSetを、スライダーにセット
        if (requestCode == 1 && resultCode == RESULT_OK) {
            currentDivisionSetIndex = data.getIntExtra("currentDivisionSetIndex", 0);
            DivisionSet divisionSet = divisionSets.get(currentDivisionSetIndex);
            ArrayList<Division> divisions = divisionSet.getDivisions();
            this.slider = new Slider(divisions);

            boolean isChanged = data.getBooleanExtra("isChanged", true);
            // もしDivisionSetが変更された場合は、Rangeをデフォルト(最初から最後まで)に戻す
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            if (isChanged) {
                int divisionsSize = divisions.size();
                editor.putInt("currentRangeStart", 0);
                editor.putInt("currentRangeEnd", divisionsSize-1);
            }
            editor.putInt("currentDivisionSetIndex", this.currentDivisionSetIndex);
            editor.commit();
        }
    }
    @Override
    protected void onResume() {
        // 保存したRangeがあれば、設定する
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Integer currentRangeStart = settings.getInt("currentRangeStart", -1);
        Integer currentRangeEnd = settings.getInt("currentRangeEnd", -1);
        if (settings.getInt("currentDivisionSetIndex", -1) != -1) {
            this.currentDivisionSetIndex = settings.getInt("currentDivisionSetIndex", -1);
        }
        this.slider = new Slider(divisionSets.get(this.currentDivisionSetIndex).getDivisions());
        ArrayList<Integer> range = new ArrayList<>();
        if (currentRangeStart != -1 && currentRangeEnd != -1) {
            range.add(currentRangeStart);
            range.add(currentRangeEnd);
            slider.setRange(range);
        }
        scrollFrom = -1;
        firstOnDown = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 現在のスライダー位置を保存
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentRangeStart", slider.getRange().get(0));
        editor.putInt("currentRangeEnd", slider.getRange().get(1));
        editor.putInt("currentDivisionSetIndex", this.currentDivisionSetIndex);
        editor.commit();
        super.onPause();
    }
}
