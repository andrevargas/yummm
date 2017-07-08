package br.univali.sisnet.yummm.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import br.univali.sisnet.yummm.R;
import br.univali.sisnet.yummm.domain.Rating;
import io.realm.Realm;

public class AddRatingActivity extends AppCompatActivity {

    // Realm stuff
    private Realm realm = Realm.getDefaultInstance();
    private Rating rating = new Rating();

    // Screen widgets
    private EditText etDescription;
    private Spinner spCategory;
    private RatingBar rbValue;

    // Helpers
    private File picture;
    private LocationManager locationManager;

    // Constants
    private static final int REQUEST_LOCATION_ACCESS = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);

        etDescription = (EditText) findViewById(R.id.etDescription);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        rbValue = (RatingBar) findViewById(R.id.rbValue);

        setupUi();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initLocationRequest();

    }

    private void setupUi() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_rating);
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

        DrawableCompat.setTint(rbValue.getProgressDrawable(), Color.parseColor("#FFA000"));

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
            ImageView ivPicture = (ImageView) findViewById(R.id.ivPicture);
            ivPicture.setImageURI(pictureUri);
        }
    }

    private void initLocationRequest() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION_ACCESS);
            return;
        }

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                rating.setLatitude(location.getLatitude());
                rating.setLongitude(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
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

    public void addPicture(View view) {
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

    private boolean validateFields() {

        if (etDescription.getText().length() <= 0) {
            ((TextInputLayout) findViewById(R.id.tilDescription)).setError(
                getString(R.string.error_field_required)
            );
            return false;
        }

        if (spCategory.getSelectedItemPosition() <= 0) {
            Toast.makeText(this, getString(R.string.error_select_category), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (picture == null) {
            Toast.makeText(this, getString(R.string.error_add_picture), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public void onClickSave(View view) {

        if (!validateFields()) return;

        String[] categoryValues = getResources().getStringArray(R.array.category_values);
        int position = spCategory.getSelectedItemPosition();
        int categoryId = Integer.valueOf(categoryValues[position]);

        rating.setId(UUID.randomUUID().toString());
        rating.setDescription(etDescription.getText().toString());
        rating.setRatingValue((int) rbValue.getRating());
        rating.setPicturePath(picture.getAbsolutePath());
        rating.setCategory(categoryId);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(rating);
            }
        });

        finish();

    }

}
