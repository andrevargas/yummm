package br.univali.sisnet.yummm.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import br.univali.sisnet.yummm.R;
import br.univali.sisnet.yummm.domain.Rating;
import br.univali.sisnet.yummm.ui.adapters.RatingAdapter;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm = Realm.getDefaultInstance();

    private RecyclerView rvRatings;
    private RatingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        rvRatings = (RecyclerView) findViewById(R.id.rvRatings);
        adapter = new RatingAdapter(
            realm.where(Rating.class).findAll(),
            new OnItemSelectedListener() {
                @Override
                public void onItemSelect(Rating item) {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    intent.putExtra("rating_id", item.getId());
                    startActivity(intent);
                }
            }
        );

        rvRatings.setLayoutManager(new LinearLayoutManager(this));
        rvRatings.setAdapter(adapter);

    }

    private void setupUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(this, AddRatingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelect(Rating item);
    }

}
