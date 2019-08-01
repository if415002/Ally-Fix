package com.kp.pembekalan.ally;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kp.pembekalan.ally.interfaces.APIServices;
import com.kp.pembekalan.ally.interfaces.Order;
import com.kp.pembekalan.ally.interfaces.Recommendation;
import com.kp.pembekalan.ally.interfaces.RecommendationAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RekomendasiActivity extends AppCompatActivity {
    private static Retrofit retrofit;
    public static String BASE_URL = "http://54.169.68.116:4000/";

    private APIServices service;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build();
        }
        return retrofit;
    }

    RecyclerView recyclerView;
    List<Recommendation> recommendationList;
    List<Order> orderList;
    Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_rekomendasi );

       // recommendationList = (ArrayList<Recommendation>) getIntent().getSerializableExtra( "recommendations" );
        orderList = (ArrayList<Order>) getIntent().getSerializableExtra( "order" );

        recyclerView = findViewById( R.id.rv );
/*
        btnOrder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommendationList = (ArrayList<Recommendation>) getIntent().getSerializableExtra( "recommendations" );
                APIServices apiServices = getRetrofitInstance().create( APIServices.class );
                Call<List<Order>> orderListCall = apiServices.orderProduct();
                orderListCall.enqueue( new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        Button btnOrder = (Button) findViewById( R.id.btnOrder );
                        Intent intent = new Intent( getApplicationContext(), OrderActivity.class );
                        startActivity( intent );
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        System.out.println("Order failed");
                    }
                });
            }
        } );*/

//        Button btnOrder = (Button) findViewById( R.id.btnOrder );
        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        RecyclerView.LayoutManager rvLinearLayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvLinearLayoutManager);

        RecommendationAdapter recommendationAdapter = new RecommendationAdapter( this.recommendationList, this );
        recyclerView.setAdapter( recommendationAdapter );
    }
}
