package com.kp.pembekalan.ally;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kp.pembekalan.ally.interfaces.Recommendation;
import com.kp.pembekalan.ally.interfaces.RecommendationAdapter;

import java.util.ArrayList;
import java.util.List;

public class RekomendasiActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Recommendation> recommendationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_rekomendasi );

        recommendationList = (ArrayList<Recommendation>)getIntent().getSerializableExtra("recommendations");

        recyclerView = findViewById( R.id.rv );

        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        RecyclerView.LayoutManager rvLinearLayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvLinearLayoutManager);

        RecommendationAdapter recommendationAdapter = new RecommendationAdapter( this.recommendationList, this );
        recyclerView.setAdapter( recommendationAdapter );
    }
}
