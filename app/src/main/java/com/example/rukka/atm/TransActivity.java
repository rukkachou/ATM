package com.example.rukka.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransActivity extends AppCompatActivity {

    private static final String TAG = TransActivity.class.getSimpleName();
    private List<TransEntity> transactions;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

//        new TransTask().execute("http://atm201605.appspot.com/h");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://atm201605.appspot.com/h")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String json = response.body().string();
                Log.d(TAG, "onResponse: " + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJSON(json);
                    }
                });
            }
        });

        recyclerView = findViewById(R.id.content_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void parseJSON(String json) {
        transactions = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                transactions.add(new TransEntity(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(new Adapter());
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_item_trans, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(transactions.get(position));
        }

        @Override
        public int getItemCount() {
            return transactions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView itemDate;
            private final TextView itemAmount;
            private final TextView itemType;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemDate = itemView.findViewById(R.id.item_date);
                itemAmount = itemView.findViewById(R.id.item_amount);
                itemType = itemView.findViewById(R.id.item_type);
            }

            public void bind(TransEntity item) {
                itemDate.setText(item.getDate());
                itemAmount.setText(String.valueOf(item.getAmount()));
                itemType.setText(String.valueOf(item.getType()));
            }
        }
    }

    class TransTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStream is = url.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String lineString = br.readLine();
                while (lineString != null) {
                    sb.append(lineString);
                    lineString = br.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: " + s);
        }
    }
}