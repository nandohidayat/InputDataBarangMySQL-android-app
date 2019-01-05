package com.nandohidayat.app.inputdatabarangmysql;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String BARANG = "com.nandohidayat.app.inputdatabarangmysql.brg";

    ListView listView;

    List<Barang> barangs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_barang);

        barangs = new ArrayList<>();

        getJSON("https://input-data-barang-nandohidayat.c9users.io/api/barang.php");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Barang barang = barangs.get(position);

                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                Gson gson = new Gson();
                intent.putExtra(BARANG, gson.toJson(barang));

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_barang:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra(BARANG, "");
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getJSON(final String urlWebService) {
        class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                try {
                    loadIntoListView(aVoid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        barangs = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String kdbrg = obj.getString("kdbrg");
            String nmbrg = obj.getString("nmbrg");
            String satuan = obj.getString("satuan");
            double hrgjual = obj.getDouble("hrgjual");
            double hrgbeli = obj.getDouble("hrgbeli");
            int stok = obj.getInt("stok");
            int stokmin = obj.getInt("stokmin");
            barangs.add(new Barang(kdbrg, nmbrg, satuan, hrgbeli, hrgjual, stok, stokmin));
        }

        BarangList barangList = new BarangList(MainActivity.this, barangs);
        listView.setAdapter(barangList);
    }
}
