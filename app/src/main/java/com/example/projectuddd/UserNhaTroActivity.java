package com.example.projectuddd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserNhaTroActivity extends AppCompatActivity {
    ListView lv;
    BookmarkUserAdapter adapter;
    ArrayList<NhaTro> list;
    DatabaseReference mData;
    FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_nha_tro);
        lv=findViewById(R.id.listViewUser);
        mData= FirebaseDatabase.getInstance().getReference("NhaTro");
        mAuth=FirebaseAuth.getInstance();

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Bạn có chắc muốn xóa bài đăng này không ?");
        list=new ArrayList<>();
        adapter=new BookmarkUserAdapter(this,R.layout.bookmark_user_item,list);
        lv.setAdapter(adapter);
        loadData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMenu(view,i);
            }
        });


    }

    private void loadData() {
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NhaTro nhaTro = dataSnapshot.getValue(NhaTro.class);
                nhaTro.setKey(dataSnapshot.getKey());
                if(nhaTro.getUser().equals(mAuth.getCurrentUser().getUid())) {
                    list.add(nhaTro);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showMenu(View view, final int i) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_user_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemXem :
                        Intent intent = new Intent(UserNhaTroActivity.this,NhaTroDetail.class);
                        NhaTro nhaTro = list.get(i);
                        intent.putExtra("nhatro",nhaTro);
                        startActivity(intent);
                        break;
                    case R.id.itemSua :
                        Intent intent1 = new Intent(UserNhaTroActivity.this,UpdateNhaTroActivity.class);
                        NhaTro nhaTro2 = list.get(i);
                        intent1.putExtra("nhatroupdate",nhaTro2);
                        startActivity(intent1);
                        break;
                    case R.id.itemXoa :
                        NhaTro nhaTro1 = list.get(i);
                        final String key = nhaTro1.getKey();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(nhaTro1.getHinh());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mData.child(key).removeValue();
                                list.remove(i);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(UserNhaTroActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                }
                return false;
            }
        });
        popupMenu.show();
    }
}
