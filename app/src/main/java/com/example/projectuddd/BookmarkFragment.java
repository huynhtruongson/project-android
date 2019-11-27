package com.example.projectuddd;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {
    ListView lv;
    BookmarkUserAdapter adapter;
    ArrayList<NhaTro> list;
    DatabaseReference mData;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.bookmark_fragment,container,false);
        lv=view.findViewById(R.id.listViewBookmark);
        list=new ArrayList<>();
        adapter=new BookmarkUserAdapter(getActivity(),R.layout.bookmark_user_item,list);
        mData= FirebaseDatabase.getInstance().getReference("NhaTro");
        mAuth=FirebaseAuth.getInstance();
        lv.setAdapter(adapter);
        loadData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),NhaTroDetail.class);
                NhaTro nhaTro = list.get(i);
                intent.putExtra("nhatro",nhaTro);
                startActivity(intent);
            }
        });
        return view;
    }
    private void loadData() {
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NhaTro nhaTro = dataSnapshot.getValue(NhaTro.class);
                nhaTro.setKey(dataSnapshot.getKey());
                if(nhaTro.getUser().equals(mAuth.getCurrentUser().getUid()) && nhaTro.getCheck().equals("1")) {
                    list.add(nhaTro);
                    adapter.notifyDataSetChanged();
                }
                else if(!nhaTro.getUser().equals(mAuth.getCurrentUser().getUid()) && nhaTro.getCheck2().equals("1")) {
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
}
