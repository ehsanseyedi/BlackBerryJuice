package com.BlackBerryJuice.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.BlackBerryJuice.R;
import com.BlackBerryJuice.adapters.ImageGalleryAdapter;
import com.BlackBerryJuice.enums.PaletteColorType;
import com.BlackBerryJuice.util.ImageGalleryUtils;
import com.BlackBerryJuice.view.GridSpacesItemDecoration;

import java.util.ArrayList;

public class ImageGalleryActivity extends AppCompatActivity implements ImageGalleryAdapter.OnImageClickListener {

    // region Member Variables
    private ArrayList<String> mImages;
    private PaletteColorType mPaletteColorType;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_gallery);

        bindViews();

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mImages = extras.getStringArrayList("images");
                mPaletteColorType = (PaletteColorType) extras.get("palette_color_type");
            }
        }

        setUpRecyclerView();
    }
    // endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpRecyclerView();
    }

    // region ImageGalleryAdapter.OnImageClickListener Methods
    @Override
    public void onImageClick(int position) {
        Intent intent = new Intent(ImageGalleryActivity.this, FullScreenImageGalleryActivity.class);

        intent.putStringArrayListExtra("images", mImages);
        intent.putExtra("position", position);
        if (mPaletteColorType != null) {
            intent.putExtra("palette_color_type", mPaletteColorType);
        }
        //Toast.makeText(this,mImages.toString() + " " + position,Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }
    // endregion

    // region Helper Methods
    private void bindViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setUpRecyclerView() {
        int numOfColumns;
        if (ImageGalleryUtils.isInLandscapeMode(this)) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(ImageGalleryActivity.this, numOfColumns));
        mRecyclerView.addItemDecoration(new GridSpacesItemDecoration(ImageGalleryUtils.dp2px(this, 2), numOfColumns));
        ImageGalleryAdapter imageGalleryAdapter = new ImageGalleryAdapter(mImages);
        imageGalleryAdapter.setOnImageClickListener(this);

        mRecyclerView.setAdapter(imageGalleryAdapter);
    }
    // endregion
}
