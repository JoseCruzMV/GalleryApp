package com.example.galleryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> mImagesPaths;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        mImagesPaths = new ArrayList<>();
        mRecyclerView = findViewById(R.id.images_recycler_view);

        prepareRecyclerView();
    }

    private void requestPermissions() {
        if (checkPermission()) {
            Toast.makeText(this, R.string.permissions_granted_message
                    , Toast.LENGTH_SHORT).show();
            getImagePath();
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    private void prepareRecyclerView() {
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, mImagesPaths);

        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 4);

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void getImagePath() {
        boolean isSDPresent = android.os.Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);

        if (isSDPresent) {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};

            final String orderBy = MediaStore.Images.Media._ID;

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, null, null, orderBy);

            int count = cursor.getCount();

            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);

                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                mImagesPaths.add(cursor.getString(dataColumnIndex));
            }
            recyclerViewAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        Toast.makeText(this, R.string.permissions_granted_message,
                                Toast.LENGTH_SHORT).show();
                        getImagePath();
                    }
                } else {
                    Toast.makeText(this, R.string.permissions_denied_message,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}