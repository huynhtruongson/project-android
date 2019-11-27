package com.example.projectuddd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarkUserAdapter extends BaseAdapter {
    Context  context;
    int layout;
    ArrayList<NhaTro> arr;

    public BookmarkUserAdapter(Context context, int layout, ArrayList<NhaTro> arr) {
        this.context = context;
        this.layout = layout;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);
        ImageView img = view.findViewById(R.id.imageViewHinhBookmarkUser);
        TextView txtGiaTien = view.findViewById(R.id.textViewGiaTienBookmarkUser);
        TextView txtMoTa=view.findViewById(R.id.textViewMoTaBookmarkUser);
        TextView txtDiaChi =view.findViewById(R.id.textViewDiaChiBookmarkUser);
        TextView txtDienTich=view.findViewById(R.id.textViewDienTichBookmaekUser);
        NhaTro nhaTro = arr.get(i);

        Picasso.get().load(nhaTro.getHinh()).fit().centerCrop().into(img);
        txtGiaTien.setText(nhaTro.getGiatien());
        txtMoTa.setText(nhaTro.getMota());
        txtDiaChi.setText(nhaTro.getDiachi());
        txtDienTich.setText(nhaTro.getDientich()+"m2");
        return view;
    }
}
