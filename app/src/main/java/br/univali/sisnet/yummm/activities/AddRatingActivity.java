package br.univali.sisnet.yummm.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;

import br.univali.sisnet.yummm.R;

public class AddRatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Drawable progress = ratingBar.getProgressDrawable();

        DrawableCompat.setTint(progress, Color.parseColor("#ffd230"));

    }

}
