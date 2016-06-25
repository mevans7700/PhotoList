package com.evansappwriter.photolist.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.evansappwriter.photolist.R;

/**
 * Created by markevans on 6/25/16.
 */
public class PhotoActivity extends Activity {
    public static final String PHOTO_URL_KEY = "photo_url";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);

        initUI();
    }

    private void initUI () {
        Bundle b = getIntent().getExtras();
        // Image
        ImageView photoIV = (ImageView) findViewById(R.id.photoIV);
        Glide.with(this)
                .load(b.getString(PHOTO_URL_KEY))
                .fitCenter()
                .placeholder(R.drawable.default_photo)
                .error(R.drawable.default_photo)
                .into(photoIV);
    }
}
