package com.kp.pembekalan.ally.interfaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kp.pembekalan.ally.MainActivity;
import com.kp.pembekalan.ally.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHoldere> {
    private List<Recommendation> recommendationList;

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("getImageBitMap", "Error getting bitmap", e);
        }
        return bm;
    }

    public RecommendationAdapter(List<Recommendation> recommendationList) {
        this.recommendationList = recommendationList;
    }

    @NonNull
    @Override
    public ViewHoldere onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from( viewGroup.getContext() );
        View view = layoutInflater.inflate( R.layout.rv_recommendation_item, viewGroup,false);
        return new ViewHoldere( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldere viewHoldere, int i) {
        viewHoldere.txtName.setText( recommendationList.get( i ).getName() );
        viewHoldere.txtPrice.setText(""+ recommendationList.get( i ).getPrice() );
        viewHoldere.txtDescription.setText( recommendationList.get( i ).getDescription() );
        viewHoldere.imageView.setImageBitmap( getImageBitmap( MainActivity.BASE_URL + recommendationList.get( i ).getImages()) );
    }

    @Override
    public int getItemCount() {
        return recommendationList.size();
    }

    public class ViewHoldere extends RecyclerView.ViewHolder{
        private TextView txtName, txtPrice, txtDescription;
        private ImageView imageView;

        public ViewHoldere(@NonNull View view) {
            super( view );
            this.txtName = txtName;
            this.txtPrice = txtPrice;
            this.txtDescription = txtDescription;
            this.imageView = imageView;

            txtName = itemView.findViewById( R.id.txtNamaMakanan );
            txtDescription = itemView.findViewById( R.id.txtDeskripsi );
            txtPrice = itemView.findViewById( R.id.txtHarga );
            imageView = itemView.findViewById( R.id.imageRecommendation );

        }
    }
}
