package tech.phlocs.histleap;

import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
        int defaultDivisionSetIndex = 0;


        DivisionSet divisionSet = divisionSets.get(defaultDivisionSetIndex);
        ArrayList<Division> divisions = divisionSet.getDivisions();
        this.slider = new Slider(divisions);
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
        JsonHandler jh = new JsonHandler(this);
        JSONObject json = jh.makeJsonFromRawFile(R.raw.spots);
        JSONArray spotObjArray = jh.getJsonArrayInJson(json, "spots");
        ArrayList<JSONObject> spotObjList = jh.makeArrayListFromJsonArray(spotObjArray);
        this.spots = new ArrayList<>();
        for (int i = 0; i < spotObjList.size(); i++) {
            spots.add(jh.makeSpotFromJson(spotObjList.get(i)));
        }

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
        LatLng initialLocation = new LatLng(35.608834, 139.730238);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, (float)15));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String spotName = marker.getTitle();
                _startSpotDetailActivity(spotName);
                return false;
            }
        });
    }

    public void onClickToggleSlider(View view) {
        if (saLayout.getVisibility() == View.VISIBLE) {
            saLayout.setVisibility(View.INVISIBLE);
        } else {
            saLayout.setVisibility(View.VISIBLE);
        }
    }

    private void _startSpotDetailActivity(String spotName) {
        Intent i = new Intent(this, SpotDetailActivity.class);
        i.putExtra("spotName", spotName);

        int startYear = slider.getEdgeYear(0);
        int endYear = slider.getEdgeYear(1);
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

        for (Marker m : this.markers) {
            m.remove();
        }
        markers = new ArrayList<>();

        ArrayList<Spot> filteredSpots = this.slider.getFilteredSpots(this.spots);
        for (Spot s : filteredSpots) {
            this.markers.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLatitude(), s.getLongitude()))
                    .title(s.getName())
            ));
        }

        changeHeaderText();
        return false;
    }

    public boolean handleScrollToChangeSliderRange(int from , int to) {
        slider.setRangeByScroll(from, to);
        GridView sliderPoints = (GridView) findViewById(R.id.slider_points);
//        if (saLayout.getVisibility() == View.INVISIBLE) {
//            return true;
//        }
        sliderPoints.setAdapter(new SliderPointAdapter(MapsActivity.this, slider));

        for (Marker m : this.markers) {
            m.remove();
        }
        markers = new ArrayList<>();

        ArrayList<Spot> filteredSpots = this.slider.getFilteredSpots(this.spots);
        for (Spot s : filteredSpots) {
            this.markers.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLatitude(), s.getLongitude()))
                    .title(s.getName())
            ));
        }

        changeHeaderText();
        return false;
    }

    public void changeHeaderText() {
        TextView headerText = (TextView) findViewById(R.id.range);
        if (slider.isSamePoint()) {
            headerText.setText(slider.getDivisions().get(slider.getRange().get(0)).getName());
        } else {
            headerText.setText(
                    slider.getDivisions().get(slider.getRange().get(0)).getName()
                            + " 〜 "
                            + slider.getDivisions().get(slider.getRange().get(1)).getName()
            );
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
            if (isChanged) {
                int divisionsSize = divisions.size();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("currentRangeStart", 0);
                editor.putInt("currentRangeEnd", divisionsSize-1);
                editor.commit();
            }
            onWindowFocusChanged(true);
        }
    }
    @Override
    protected void onResume() {
        // 保存したRangeがあれば、設定する
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Integer currentRangeStart = settings.getInt("currentRangeStart", -1);
        Integer currentRangeEnd = settings.getInt("currentRangeEnd", -1);
        ArrayList<Integer> range = new ArrayList<>();
        if (currentRangeStart != -1 && currentRangeEnd != -1) {
            range.add(currentRangeStart);
            range.add(currentRangeEnd);
            slider.setRange(range);
        }
        onWindowFocusChanged(true);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 現在のスライダー位置を保存
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentRangeStart", slider.getRange().get(0));
        editor.putInt("currentRangeEnd", slider.getRange().get(1));
        editor.commit();
        super.onPause();
    }
}
