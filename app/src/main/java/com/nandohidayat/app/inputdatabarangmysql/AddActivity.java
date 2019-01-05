package com.nandohidayat.app.inputdatabarangmysql;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddActivity extends AppCompatActivity {
    EditText editKdBrg;
    EditText editNmBrg;
    EditText editSatuan;
    EditText editHrgBeli;
    EditText editHrgJual;
    EditText editStok;
    EditText editStokMin;
    Button buttonAdd;
    Button buttonDel;

    String ExtraBarang;
    Barang barang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        ExtraBarang = intent.getStringExtra(MainActivity.BARANG);
        Gson gson = new Gson();
        barang = gson.fromJson(ExtraBarang, Barang.class);

        editKdBrg = findViewById(R.id.kdbrg);
        editNmBrg = findViewById(R.id.nmbrg);
        editSatuan = findViewById(R.id.satuan);
        editHrgBeli = findViewById(R.id.hrgbeli);
        editHrgJual = findViewById(R.id.hrgjual);
        editStok = findViewById(R.id.stok);
        editStokMin = findViewById(R.id.stokmin);
        buttonAdd = findViewById(R.id.btnadd);
        buttonDel = findViewById(R.id.btndel);

        if(ExtraBarang.length() == 0) {
            buttonDel.setVisibility(View.GONE);
        } else {
            buttonAdd.setText("Save");
            editKdBrg.setEnabled(false);

            editKdBrg.setText(barang.getKdbrg());
            editNmBrg.setText(barang.getNmbrg());
            editSatuan.setText(barang.getSatuan());
            editHrgBeli.setText(barang.getHrgbeli() + "");
            editHrgJual.setText(barang.getHrgjual() + "");
            editStok.setText(barang.getStok() + "");
            editStokMin.setText(barang.getStok_min() + "");
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBarang();
            }
        });

        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delBarang();
            }
        });
    }

    private void addBarang() {
        String KdBrg = editKdBrg.getText().toString().trim();
        String NmBrg = editNmBrg.getText().toString().trim();
        String Satuan = editSatuan.getText().toString().trim();
        double HrgBeli = Double.parseDouble(editHrgBeli.getText().toString().trim());
        double HrgJual = Double.parseDouble(editHrgJual.getText().toString().trim());
        int Stok = Integer.parseInt(editStok.getText().toString().trim());
        int StokMin = Integer.parseInt(editStokMin.getText().toString().trim());

        Barang barang = new Barang(KdBrg, NmBrg, Satuan, HrgBeli, HrgJual, Stok, StokMin);

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder builder;

            if(ExtraBarang.length() == 0) {
                builder = HttpUrl.parse("https://input-data-barang-nandohidayat.c9users.io/api/create.php").newBuilder();
            } else {
                builder = HttpUrl.parse("https://input-data-barang-nandohidayat.c9users.io/api/update.php").newBuilder();
            }

            builder.addQueryParameter("kdbrg", KdBrg);
            builder.addQueryParameter("nmbrg", NmBrg);
            builder.addQueryParameter("satuan", Satuan);
            builder.addQueryParameter("hrgjual", HrgJual + "");
            builder.addQueryParameter("hrgbeli", HrgBeli + "");
            builder.addQueryParameter("stok", Stok + "");
            builder.addQueryParameter("stokmin", StokMin + "");

            String url = builder.build().toString();

            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (response.body().string().equals("success")) {
                                    if(ExtraBarang.length() == 0) {
                                        Toast.makeText(AddActivity.this, "Data added", Toast.LENGTH_LONG);
                                    } else {
                                        Toast.makeText(AddActivity.this, "Update Successfully", Toast.LENGTH_LONG);
                                    }
                                } else {
                                    if(ExtraBarang.length() == 0) {
                                        Toast.makeText(AddActivity.this, "Create failed", Toast.LENGTH_LONG);
                                    } else {
                                        Toast.makeText(AddActivity.this, "Failed to Update", Toast.LENGTH_LONG);
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.BARANG, "");

        startActivity(intent);
    }

    private void delBarang() {
        String KdBrg = editKdBrg.getText().toString().trim();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder builder;

            builder = HttpUrl.parse("https://input-data-barang-nandohidayat.c9users.io/api/delete.php").newBuilder();

            builder.addQueryParameter("kdbrg", KdBrg);

            String url = builder.build().toString();

            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (response.body().string().equals("success")) {
                                    Toast.makeText(AddActivity.this, "Data successfully deleted", Toast.LENGTH_LONG);
                                } else {
                                    Toast.makeText(AddActivity.this, "Delete failed", Toast.LENGTH_LONG);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.BARANG, "");

        startActivity(intent);
    }
}
