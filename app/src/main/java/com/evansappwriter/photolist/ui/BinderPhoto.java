package com.evansappwriter.photolist.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evansappwriter.photolist.R;
import com.evansappwriter.photolist.model.Photo;
import com.evansappwriter.photolist.util.AltArrayAdapter;
import com.evansappwriter.photolist.util.Utils;

import java.util.List;


public class BinderPhoto implements AltArrayAdapter.ViewBinder<Photo>  {
    private static final String TAG = "BinderPhoto";

    private static final int MAX_LENGTH_TITLE = 50;



    public BinderPhoto(Context context) {
    }

    @Override
    public void setViewValue(Context context, View view, Photo object) {
        switch (view.getId()) {
            case R.id.title:
                String title = object.getTitle().substring(0, MAX_LENGTH_TITLE);
                ((TextView) view).setText(title);
                break;
        }
    }

    @Override
    public int getItemViewType(List<Photo> objects, int position) {
        return 0;
    }
}
