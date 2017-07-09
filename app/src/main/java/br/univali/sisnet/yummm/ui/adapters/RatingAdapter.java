package br.univali.sisnet.yummm.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.univali.sisnet.yummm.R;
import br.univali.sisnet.yummm.activities.MainActivity;
import br.univali.sisnet.yummm.domain.Rating;
import br.univali.sisnet.yummm.ui.holders.RatingHolder;

public class RatingAdapter extends RecyclerView.Adapter<RatingHolder> {

    private List<Rating> ratingList;
    private MainActivity.OnItemSelectedListener listener;

    public RatingAdapter(List<Rating> ratingList, MainActivity.OnItemSelectedListener listener) {
        this.ratingList = ratingList;
        this.listener = listener;
    }

    @Override
    public RatingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.item_rating, parent, false);
        return new RatingHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RatingHolder holder, int position) {
        holder.bindItem(ratingList.get(position));
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }
}
