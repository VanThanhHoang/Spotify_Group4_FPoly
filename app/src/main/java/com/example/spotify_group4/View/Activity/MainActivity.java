package com.example.spotify_group4.View.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotify_group4.Model.PlayList;
import com.example.spotify_group4.Model.Singer;
import com.example.spotify_group4.R;
import com.example.spotify_group4.Retrofit.ApiSkyMusic;
import com.example.spotify_group4.databinding.ActivityMainBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private StorageReference storageReference;
    int singerId = -1;
    private static final int REQUEST_CODE_PICK_MUSIC = 1;
    Uri selectedMusicUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storageReference = FirebaseStorage.getInstance().getReference();
         getAllSinger();
        binding.btnAddMusic.setOnClickListener(v->{
            openFileChooser();
        });
        binding.uploadMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.edPlayListName.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên bài hát", Toast.LENGTH_SHORT).show();
                    return;
                }
               if(singerId == -1){
                   Toast.makeText(MainActivity.this, "Vui lòng chọn ca sĩ", Toast.LENGTH_SHORT).show();
                   return;
               }
                if(selectedMusicUri == null){
                    Toast.makeText(MainActivity.this, "Vui lòng chọn bài hát", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadToFirebase(selectedMusicUri);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*"); // Đặt loại file cần chọn (ở đây là file âm thanh)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_PICK_MUSIC);
    }
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_MUSIC && resultCode == RESULT_OK) {
            if (data != null) {
              selectedMusicUri  = data.getData();
              // get file name from file selected
                String fileName = null;
                if (selectedMusicUri != null) {
                    Cursor cursor = getContentResolver().query(selectedMusicUri, null, null, null, null);
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    binding.btnAddMusic.setText(fileName);
                }
            }
        }
    }

    void getAllSinger(){
        Call<List<Singer>> call = ApiSkyMusic.apiSkyMusic.getAllSinger();
        call.enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                List<Singer> singers = response.body();
                fillDataToDropDownMenu(singers);
                for (Singer singer : singers) {
                    Log.d("Singer", singer.getName());
                }
            }
            @Override
            public void onFailure(Call<List<Singer>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }

    void fillDataToDropDownMenu(List<Singer> singers) {
       String [] singerNames = new String[singers.size()];
        for (int i = 0; i < singers.size(); i++) {
           singerNames[i] = singers.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, singerNames);
        binding.atcPlaylist.setAdapter(adapter);
       binding.atcPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              singerId= singers.get(position).getId();
                binding.atcPlaylist.setText(singerNames[position], false);
            }
        });
    }
    private void uploadToFirebase(Uri imageUri) {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference fileRef = storageReference.child("/" + System.currentTimeMillis() + ".mp3");
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Ảnh đã được tải lên thành công
                        // Lấy link của ảnh sau khi tải lên thành công
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String url = uri.toString();
                            Call<Void> call = ApiSkyMusic.apiSkyMusic.insertSong(binding.edPlayListName.getText().toString(),singerId,"https://i.ytimg.com/vi/D164TFHeOcI/maxresdefault.jpg",url,"Khác");
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(MainActivity.this, "Tải lên thành công", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Tải lên thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                            // Sử dụng link ảnh ở đây, ví dụ: lưu vào cơ sở dữ liệu
                            // Nếu bạn muốn thực hiện hành động nào đó với link ảnh, hãy viết code ở đây
                            progressDialog.dismiss(); // Đóng loading indicator sau khi hoàn thành
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss(); // Đóng loading indicator sau khi hoàn thành (thất bại)
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Đã xảy ra lỗi khi tải ảnh lên
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss(); // Đóng loading indicator sau khi hoàn thành (thất bại)
                    });
        } else {
            Toast.makeText(this, "Không có ảnh được chọn", Toast.LENGTH_SHORT).show();
        }
    }

}