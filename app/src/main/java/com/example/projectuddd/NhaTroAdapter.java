package com.example.projectuddd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class NhaTroAdapter extends RecyclerView.Adapter<NhaTroAdapter.ViewHolder> {
    Context context;
    ArrayList<NhaTro> arrayList;

    public NhaTroAdapter(Context context, ArrayList<NhaTro> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_nha_tro_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getHinh()).fit().centerCrop().into(holder.img);
        holder.txtGiaTien.setText(arrayList.get(position).getGiatien());
        holder.txtDiaChi.setText(arrayList.get(position).getDiachi()+",Quận "+arrayList.get(position).getQuan());
        holder.txtDienTich.setText(arrayList.get(position).getDientich() + "m2");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txtGiaTien,txtDiaChi,txtDienTich;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imageViewHinh);
            txtGiaTien=itemView.findViewById(R.id.textViewGiaTien);
            txtDiaChi=itemView.findViewById(R.id.textViewDiaChi);
            txtDienTich=itemView.findViewById(R.id.textViewDienTich);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NhaTro nhaTro = arrayList.get(getAdapterPosition());
                    //Toast.makeText(context, nhaTro.getDiachi(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,NhaTroDetail.class);

                    intent.putExtra("nhatro",(Serializable)nhaTro);
                    context.startActivity(intent);
                }
            });
        }

    }

}