package com.example.projectuddd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class NhaTroDetail extends AppCompatActivity {
    DatabaseReference mData;
    FirebaseAuth mAuth;
    ImageView imgHinhChiTiet;
    TextView txtTieuDeChiTiet,txtDiaChiChiTiet,txtMoTaChiTiet,txtDienTichChiTiet,txtSdtChiTiet,txtTenChiTiet,txtToolbar;
    Toolbar toolbar;
    FloatingActionButton fabCall;
    NhaTro nhaTro;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nha_tro_detail);

        mData= FirebaseDatabase.getInstance().getReference("NhaTro");
        mAuth=FirebaseAuth.getInstance();
        imgHinhChiTiet=findViewById(R.id.imageViewHinhChiTiet);
        txtTieuDeChiTiet=findViewById(R.id.textViewTieuDeChiTiet);
        txtDiaChiChiTiet=findViewById(R.id.textViewDiaChiChiTiet);
        txtDienTichChiTiet=findViewById(R.id.textViewDienTichChiTiet);
        txtSdtChiTiet=findViewById(R.id.textViewSdtChiTiet);
        txtMoTaChiTiet=findViewById(R.id.textViewMoTaChiTiet);
        txtTenChiTiet = findViewById(R.id.textViewTenChiTiet);

        fabCall=findViewById(R.id.FloatingActionButtonCall);
        toolbar=findViewById(R.id.toolbarNhaTroDetail);
        txtToolbar=findViewById(R.id.textViewToolbarNhaTroDetail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //disable tittle
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        nhaTro = (NhaTro) intent.getSerializableExtra("nhatro");
        if(nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
            fabCall.setVisibility(View.INVISIBLE);
        }
        Picasso.get().load(nhaTro.getHinh()).fit().centerCrop().into(imgHinhChiTiet);
        txtTieuDeChiTiet.setText(nhaTro.getTieude());
        txtDiaChiChiTiet.setText(nhaTro.getDiachi()+",Quận "+nhaTro.getQuan());
        txtDienTichChiTiet.setText(nhaTro.getDientich());
        txtSdtChiTiet.setText(nhaTro.getSodienthoai()+ " - ");
        txtTenChiTiet.setText(nhaTro.getTen());
        txtMoTaChiTiet.setText(nhaTro.getMota());
        txtToolbar.setText(nhaTro.getGiatien() +"/tháng");

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_nhatro_detail,menu);
        CheckBox checkBox = (CheckBox) menu.findItem(R.id.checkboxToolbarNhaTroDetail).getActionView();
        checkBox.setText("Lưu");
        if(nhaTro.getCheck().equals("1") && nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
            checkBox.setChecked(true);
        }
        else if(nhaTro.getCheck2().equals("1") && !nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
            checkBox.setChecked(true);
        }
        final String key=nhaTro.getKey();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    if(nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
                        nhaTro.setCheck("1");
                    }
                    else if(!nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
                        nhaTro.setCheck2("1");
                    }
                    mData.child(key).setValue(nhaTro);
                    Toast.makeText(NhaTroDetail.this, "Đã lưu", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
                        nhaTro.setCheck("0");
                    }
                    else if(!nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
                        nhaTro.setCheck2("0");
                    }
                    mData.child(key).setValue(nhaTro);
                    Toast.makeText(NhaTroDetail.this, "Bỏ lưu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    private void Call() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        String sdt=nhaTro.getSodienthoai().toString();
        intent.setData(Uri.parse("tel:" + sdt));
        startActivity(intent);
    }
}
