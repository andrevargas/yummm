package br.univali.sisnet.yummm.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.List;

import br.univali.sisnet.yummm.R;
import br.univali.sisnet.yummm.domain.Rating;
import io.realm.Realm;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Realm realm = Realm.getDefaultInstance();
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setupUi();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupUi() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.view_in_map);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        placeMarkers();
    }

    private void placeMarkers() {

        List<Rating> ratingList = realm.where(Rating.class).findAll();

        float[] categoryColors = {
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_YELLOW,
            BitmapDescriptorFactory.HUE_GREEN,
        };

        String[] categoryNames = getResources().getStringArray(R.array.category_names);

        for (Rating item : ratingList) {
            LatLng point = new LatLng(item.getLatitude(), item.getLongitude());
            MarkerOptions marker = new MarkerOptions()
                .position(point)
                .title(categoryNames[item.getCategory()])
                .snippet(getString(
                    R.string.text_rating_info,
                    item.getRatingValue(),
                    new SimpleDateFormat("dd/MM/yyyy").format(item.getCreatedAt())
                ))
                .icon(BitmapDescriptorFactory.defaultMarker(categoryColors[item.getCategory() - 1]));
            map.addMarker(marker);
        }
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(this, AddRatingActivity.class);
        startActivity(intent);
    }

    public void onClickList(View view) {
        finish();
    }

}
