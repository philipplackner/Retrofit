package com.androiddevs.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewAdapter adapter;
    private RecyclerView rvRetroItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRetroItems = findViewById(R.id.rvItems);

        //https://jsonplaceholder.typicode.com provides several json examples
        //we can use to test our HTTP requests
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetDataService service = retrofit.create(GetDataService.class);
        Call<List<RetroItem>> call = service.getAllPhotos();

        //enqueue sends our HTTP request in a separate Thread and
        //notifies us when it got a response or an error
        call.enqueue(new Callback<List<RetroItem>>() {
            @Override
            public void onResponse(Call<List<RetroItem>> call, Response<List<RetroItem>> response) {
                adapter = new RecyclerViewAdapter(MainActivity.this, response.body());
                rvRetroItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rvRetroItems.setAdapter(adapter);

                new ItemTouchHelper(new SwipeToDeleteCallback(MainActivity.this, adapter))
                        .attachToRecyclerView(rvRetroItems);
            }

            @Override
            public void onFailure(Call<List<RetroItem>> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Oops, something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
