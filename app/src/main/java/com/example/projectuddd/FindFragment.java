package com.example.projectuddd;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindFragment extends Fragment {
    EditText edtSearch;
    ImageView imgSearch;
    ListView lv;
    ArrayList<NhaTro> arr;
    BookmarkUserAdapter adapter;
    DatabaseReference mData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.find_fragment,container,false);
        mData= FirebaseDatabase.getInstance().getReference("NhaTro");
        edtSearch=view.findViewById(R.id.editTextSearch);
        imgSearch=view.findViewById(R.id.imageViewSearch);
        lv=view.findViewById(R.id.listViewSearch);
        arr=new ArrayList<>();
        adapter=new BookmarkUserAdapter(getActivity(),R.layout.bookmark_user_item,arr);
        lv.setAdapter(adapter);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(edtSearch.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),NhaTroDetail.class);
                NhaTro nhaTro = arr.get(i);
                intent.putExtra("nhatro",nhaTro);
                startActivity(intent);
            }
        });
        return view;
    }
    private void search(String s) {
        Query firebaseSearchQuery = mData.orderByChild("quan").startAt(s).endAt(s+"\uf8ff");
        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    NhaTro nhaTro =ds.getValue(NhaTro.class);
                    arr.add(nhaTro);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
