package com.example.projectuddd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateNhaTroActivity extends AppCompatActivity {
    DatabaseReference mData;
    StorageReference mStorage;
    FirebaseAuth mAuth;
    EditText edtTieuDe,edtTen,edtDiaChi,edtQuan,edtDienTich,edtGiaTien,edtSdt,edtMoTa;
    ImageView imgHinh;
    Button btnUpdate;
    Uri imgUri;
    NhaTro nhaTro;
    int REQUEST_CODE_UPDATE=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nha_tro);
        mData= FirebaseDatabase.getInstance().getReference("NhaTro");
        mStorage= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        anhXa();
        receiveData();
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoPick();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }
    private void anhXa() {
        edtTieuDe=findViewById(R.id.editTextTieuDeUpdate);
        edtTen=findViewById(R.id.editTextTenUpdate);
        edtDiaChi=findViewById(R.id.editTextDiaChiUpdate);
        edtQuan=findViewById(R.id.editTextQuanUpdate);
        edtDienTich=findViewById(R.id.editTextDienTichUpdate);
        edtGiaTien=findViewById(R.id.editTextGiaTienUpdate);
        edtSdt=findViewById(R.id.editTextSdtUpdate);
        edtMoTa=findViewById(R.id.editTextMoTaUpdate);
        imgHinh=findViewById(R.id.imageViewHinhUpdate);
        btnUpdate=findViewById(R.id.buttonUpdate);
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void photoPick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE_UPDATE);
    }
    private void updateData() {
        final String key = nhaTro.getKey();
        if(imgUri!=null) {
            final StorageReference storageReference = mStorage.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));
            storageReference.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                        throw task.getException();
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        StorageReference storageReference1=FirebaseStorage.getInstance().getReferenceFromUrl(nhaTro.getHinh());
                        storageReference1.delete();
                        nhaTro.setTieude(edtTieuDe.getText().toString().trim());
                        nhaTro.setTen(edtTen.getText().toString().trim());
                        nhaTro.setDiachi(edtDiaChi.getText().toString().trim());
                        nhaTro.setQuan(edtQuan.getText().toString().trim());
                        nhaTro.setDientich(edtDienTich.getText().toString().trim());
                        nhaTro.setGiatien(edtGiaTien.getText().toString().trim());
                        nhaTro.setSodienthoai(edtSdt.getText().toString().trim());
                        nhaTro.setMota(edtMoTa.getText().toString().trim());
                        nhaTro.setHinh(downloadUri.toString());
                        mData.child(key).setValue(nhaTro);
                        Toast.makeText(UpdateNhaTroActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
        else {
            nhaTro.setTieude(edtTieuDe.getText().toString().trim());
            nhaTro.setTen(edtTen.getText().toString().trim());
            nhaTro.setDiachi(edtDiaChi.getText().toString().trim());
            nhaTro.setQuan(edtQuan.getText().toString().trim());
            nhaTro.setDientich(edtDienTich.getText().toString().trim());
            nhaTro.setGiatien(edtGiaTien.getText().toString().trim());
            nhaTro.setSodienthoai(edtSdt.getText().toString().trim());
            nhaTro.setMota(edtMoTa.getText().toString().trim());
            mData.child(key).setValue(nhaTro);
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void receiveData() {
        Intent intent = getIntent();
        nhaTro = (NhaTro) intent.getSerializableExtra("nhatroupdate");
        edtTieuDe.setText(nhaTro.getTieude());
        edtTen.setText(nhaTro.getTen());
        edtDiaChi.setText(nhaTro.getDiachi());
        edtQuan.setText(nhaTro.getQuan());
        edtDienTich.setText(nhaTro.getDientich());
        edtGiaTien.setText(nhaTro.getGiatien());
        edtSdt.setText(nhaTro.getSodienthoai());
        edtMoTa.setText(nhaTro.getMota());
    }
}
