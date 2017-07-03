package br.univali.sisnet.yummm.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.univali.sisnet.yummm.R;
import br.univali.sisnet.yummm.domain.Rating;

public class AddRatingActivity extends AppCompatActivity {

    private Rating rating;
    private File picture;
    private LocationManager locationManager;

    private static final int REQUEST_LOCATION_ACCESS = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);

        rating = new Rating();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Drawable progress = ratingBar.getProgressDrawable();

        DrawableCompat.setTint(progress, Color.parseColor("#ffd230"));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initLocationRequest();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_ACCESS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocationRequest();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri pictureUri = FileProvider.getUriForFile(
                    this,
                    "br.univali.sisnet.yummm",
                    picture
            );

        }
    }

    private void initLocationRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_ACCESS);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {}
            @Override
            public void onLocationChanged(Location location) {
                rating.setLatitude(location.getLatitude());
                rating.setLongitude(location.getLongitude());
            }
        });
    }

    private void createNewPictureFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "AA_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            picture = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPicture() {
        createNewPictureFile();
        Uri pictureUri = FileProvider.getUriForFile(
            this,
            "br.univali.sisnet.yummm",
            picture
        );
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

}
