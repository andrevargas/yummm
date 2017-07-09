package br.univali.sisnet.yummm.ui.holders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import br.univali.sisnet.yummm.R;
import br.univali.sisnet.yummm.domain.Rating;

public class RatingHolder extends RecyclerView.ViewHolder {

    private View itemView;

    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvRatingInfo;
    private ImageView ivThumbnail;

    public RatingHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;

        tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
        tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        tvRatingInfo = (TextView) itemView.findViewById(R.id.tvRatingInfo);
        ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
    }

    public void bindItem(final Rating item) {
        Resources res = itemView.getContext().getResources();

        String[] categoryNames = res.getStringArray(R.array.category_names);
        tvCategory.setText(categoryNames[item.getCategory()]);

        tvDescription.setText(item.getDescription());
        tvRatingInfo.setText(res.getString(R.string.text_rating_info,
            item.getRatingValue(),
            new SimpleDateFormat("dd/MM/yyyy").format(item.getCreatedAt())
        ));

        Bitmap picture = BitmapFactory.decodeFile(item.getPicturePath());
        ivThumbnail.setImageBitmap(picture);
    }

}
