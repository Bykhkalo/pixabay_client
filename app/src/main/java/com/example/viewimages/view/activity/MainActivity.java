package com.example.viewimages.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.viewimages.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void seeVideonOnClick(View view) {
    }

    public void seeImagesOnClick(View view) {
        Intent intent = new Intent(this, ImageListActivity.class);
        startActivity(intent);
    }
}
