package com.example.squadtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.squadtask.adapter.ItemsAdapter;
import com.example.squadtask.model.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search_rel)
    RelativeLayout search;
    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;

    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=47.6204,-122.3491&radius=2500&type=restaurant&key=AIzaSyDhHzf6y1brSirE2hPeMjTqSBYE73pzukM";
    RequestQueue requestQueue;
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView.LayoutManager layoutManager;
    private ItemsAdapter adapter;
    private List<Result> petsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycler_items.setLayoutManager(layoutManager);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });



    }

    public void getCartItems() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray ja_data = null;
                        try {
                            ja_data = response.getJSONArray("results");
                            int length = response.length();
                            List<Result> data=new ArrayList<>();
                            for(int i=0; i<length; i++) {
                                JSONObject jsonObj = ja_data.getJSONObject(i);
                                Result result = new Result();
                                result.name= jsonObj.getString("name");
                                result.rating= jsonObj.getString("rating");
                                result.icon= jsonObj.getString("icon");
                                data.add(result);

                                JSONObject ja = jsonObj.getJSONObject("opening_hours");
                                int len = ja.length();
                                List<Result> data1=new ArrayList<>();
                                for(int j=0; j<len; j++) {
                                    Result result1 = new Result();
                                    result1.open_now= ja.getString("open_now");
                                    data1.add(result);
                                }
                                adapter = new ItemsAdapter(MainActivity.this, data);
                                recycler_items.setAdapter(adapter);
                                recycler_items.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error Fetching Items",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCartItems();
    }


}