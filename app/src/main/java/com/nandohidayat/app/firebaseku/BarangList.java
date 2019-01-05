package com.nandohidayat.app.firebaseku;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BarangList extends ArrayAdapter<Barang> {
    private Activity context;
    List<Barang> barangs;

    public BarangList(Activity context, List<Barang> barangs) {
        super(context, R.layout.layout_barang_list, barangs);
        this.context = context;
        this.barangs = barangs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_barang_list, null, true);

        TextView textKdBrg = listViewItem.findViewById(R.id.text_kdbrg);
        TextView textNmBrg = listViewItem.findViewById(R.id.text_nmbrg);

        Barang barang = barangs.get(position);
        textKdBrg.setText(barang.getKdbrg());
        textNmBrg.setText(barang.getNmbrg());

        return listViewItem;
    }
}
