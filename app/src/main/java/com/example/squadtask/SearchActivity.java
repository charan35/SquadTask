package com.example.squadtask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
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

public class SearchActivity extends AppCompatActivity {


    @BindView(R.id.search_et)
    EditText search;
    @BindView(R.id.text)
    TextView text;
    String Text;

    RequestQueue requestQueue;
    private static final String TAG = SearchActivity.class.getSimpleName();

    private RecyclerView recycler_search;
    private RecyclerView.LayoutManager layoutManager;
    private ItemsAdapter adapter;
    private List<Result> petsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        recycler_search = findViewById(R.id.search_recycler);
        layoutManager = new LinearLayoutManager(this);
        recycler_search.setLayoutManager(layoutManager);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                text.setText(charSequence);
                searchForProducts();
                Text = text.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    public void searchForProducts() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=47.6204,-122.3491&radius=2500&type=restaurant&keyword="+Text+"&key=AIzaSyDhHzf6y1brSirE2hPeMjTqSBYE73pzukM", object,
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
                                    Toast.makeText(SearchActivity.this, ja.getString("open_now"), Toast.LENGTH_LONG).show();
                                    Result result1 = new Result();
                                    result1.open_now= ja.getString("open_now");
                                    data1.add(result);
                                }
                                adapter = new ItemsAdapter(SearchActivity.this, data);
                                recycler_search.setAdapter(adapter);
                                recycler_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this,"Error Fetching Items",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}