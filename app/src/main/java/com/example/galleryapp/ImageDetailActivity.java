package com.example.galleryapp;

import static com.example.galleryapp.RecyclerViewAdapter.IMAGE_PATH_LABEL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {

    String imagePath;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;

    private float mScaleFactor = 1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imagePath = getIntent().getStringExtra(IMAGE_PATH_LABEL);

        imageView = (ImageView) findViewById(R.id.image_view_detail);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            Picasso.get().load(imageFile).placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}