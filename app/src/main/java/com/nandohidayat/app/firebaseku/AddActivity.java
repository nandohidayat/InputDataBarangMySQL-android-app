package com.nandohidayat.app.firebaseku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

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

    DatabaseReference databaseReference;

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
            databaseReference = FirebaseDatabase.getInstance().getReference("barangs");
            buttonDel.setVisibility(View.GONE);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("barangs").child(barang.getKdbrg());
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

        if(ExtraBarang.length() == 0) {
            databaseReference.child(KdBrg).setValue(barang);
            Toast.makeText(this, "Successfully Added", Toast.LENGTH_LONG).show();
        } else {
            databaseReference.setValue(barang);
            Toast.makeText(this, "Change Saved", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.BARANG, "");

        startActivity(intent);
    }

    private void delBarang() {
        databaseReference.removeValue();
        Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.BARANG, "");

        startActivity(intent);
    }
}
