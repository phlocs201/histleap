package tech.phlocs.histleap;

import android.content.Intent;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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

import tech.phlocs.histleap.adapter.SliderPointAdapter;
import tech.phlocs.histleap.model.Slider;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Slider slider;
    private GestureDetector gd;

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
                public boolean onDown(MotionEvent e) {
                    Log.d("gesture", e.toString());
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    Log.d("gesture", "scroll");
                    return true;
                }
            }
        );

        this.slider = new Slider();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("hoge", "hogehoge");
        gd.onTouchEvent(event);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int sliderAreaWidth = findViewById(R.id.slider_points).getWidth();
        GridView sliderPoints = (GridView) findViewById(R.id.slider_points);
        sliderPoints.setColumnWidth(sliderAreaWidth / slider.getDivisions().size());
        sliderPoints.setAdapter(new SliderPointAdapter(this, slider));
        changeHeaderText();

        sliderPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleClickToChangeSliderRange(position);
            }
        });
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

        // Add a marker in Sydney and move the camera
        LatLng initialLocation = new LatLng(35.608834, 139.730238);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(35.6096764, 139.7439769))
                .title("品川寺")
        );
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(35.608834, 139.730238))
                .title("品川区役所")
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, (float)15));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                _startSpotDetailActivity();
                return false;
            }
        });
    }

    public void onClickToggleSlider(View view) {
        RelativeLayout inputArea = (RelativeLayout)findViewById(R.id.slider_area);
        if (inputArea.getVisibility() == View.VISIBLE) {
            inputArea.setVisibility(View.INVISIBLE);
        } else {
            inputArea.setVisibility(View.VISIBLE);
        }
    }

    private void _startSpotDetailActivity() {
        Intent i = new Intent(this, SpotDetailActivity.class);
        startActivityForResult(i, 1);
    }

    public void temporaryOnClick(View view) {
        Intent i = new Intent(this, DivisionSettingActivity.class);
        startActivityForResult(i, 1);
    }

    public void handleClickToChangeSliderRange(int position) {
        slider.setRangeByPosition(position);
        GridView sliderPoints = (GridView) findViewById(R.id.slider_points);
        sliderPoints.setAdapter(new SliderPointAdapter(MapsActivity.this, slider));
        changeHeaderText();
    }

    public void changeHeaderText() {
        TextView headerText = (TextView) findViewById(R.id.range);
        headerText.setText(
                slider.getDivisions().get(slider.getRange().get(0)).getName()
                        + " 〜 "
                        + slider.getDivisions().get(slider.getRange().get(1)).getName()
        );
    }

}
