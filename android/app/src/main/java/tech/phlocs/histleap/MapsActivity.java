package tech.phlocs.histleap;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tech.phlocs.histleap.model.Slider;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.slider = new Slider();
        GridView sliderPoints = (GridView) findViewById(R.id.slider_points);
        GridView sliderLabels = (GridView) findViewById(R.id.slider_labels);
        sliderPoints.setAdapter(new SliderPointAdapter(this, slider.getDivisions().size()));
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
                Log.d("@@@", "hoge");
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
}
