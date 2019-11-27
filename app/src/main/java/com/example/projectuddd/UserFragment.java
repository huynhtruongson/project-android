package com.example.projectuddd;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {
    TextView txtNameUser,txtPhoneUser,txtEmailUser;
    Button btnPhongDaDang,btnSignOut;
    ImageView imgUser;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment,container,false);
        txtNameUser=view.findViewById(R.id.textViewNameUser);
        txtPhoneUser=view.findViewById(R.id.textViewPhoneUser);
        txtEmailUser=view.findViewById(R.id.textViewEmailUser);
        imgUser=view.findViewById(R.id.imageViewUser);
        btnPhongDaDang=view.findViewById(R.id.buttonPhongDaDang);
        btnSignOut=view.findViewById(R.id.buttonSignOut);
        mAuth=FirebaseAuth.getInstance();
        mData= FirebaseDatabase.getInstance().getReference("User");
        loadData();
        btnPhongDaDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UserNhaTroActivity.class);
                startActivity(intent);
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(),SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return  view;
    }
    private void loadData() {
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                    txtNameUser.setText(user.getName());
                    txtPhoneUser.setText("SDT: "+user.getPhone());
                    txtEmailUser.setText("Email: "+user.getEmail());
                    Picasso.get().load(user.getImg()).fit().centerCrop().into(imgUser);
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
