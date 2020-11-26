package com.example.viewimages.view;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.viewimages.R;


public class ImageOverlayView extends RelativeLayout {

    String imageUrl;
   // String fileUri;

    public ImageOverlayView(Context context, String imageUrl) {
        super(context);
        this.imageUrl = imageUrl;
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void sendShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, imageUrl);
        sendIntent.setType("text/plain");
        getContext().startActivity(sendIntent);
    }

    private void downloadImage(String imageUrl){
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(imageUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (downloadManager != null) {
            Long reference = downloadManager.enqueue(request);
        }

    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_overlay, this);
        view.findViewById(R.id.btn_share).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sharing", Toast.LENGTH_SHORT).show();
            sendShareIntent();
        });
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Downloading", Toast.LENGTH_SHORT).show();
            downloadImage(imageUrl);
        });

    }

}
