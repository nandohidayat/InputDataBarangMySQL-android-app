package com.nandohidayat.app.firebaseku;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String BARANG = "com.nandohidayat.app.firebaseku.brg";

    ListView listView;

    List<Barang> barangs;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("barangs");

        listView = findViewById(R.id.list_barang);

        barangs = new ArrayList<>();

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

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                barangs.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Barang barang = postSnapshot.getValue(Barang.class);
                    barangs.add(barang);
                }

                BarangList barangList = new BarangList(MainActivity.this, barangs);
                listView.setAdapter(barangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
