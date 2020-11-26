package com.example.viewimages.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.viewimages.R;
import com.example.viewimages.utils.NavigationHost;
import com.example.viewimages.view.fragments.ImageListFragment;
import com.example.viewimages.viewmodel.ImagesViewModel;
import com.google.android.material.navigation.NavigationView;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity implements NavigationHost {

    private SNavigationDrawer sNavigationDrawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        init();

        if (savedInstanceState == null){
            navigateTo(new ImageListFragment(), false);
        }

    }

    private void init() {

        initNavigation();
    }

    private void initNavigation() {

        sNavigationDrawer = findViewById(R.id.nav_view);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("All",R.drawable.news_bg));
        menuItems.add(new MenuItem("Photos",R.drawable.feed_bg));
        menuItems.add(new MenuItem("Illustration",R.drawable.message_bg));
        menuItems.add(new MenuItem("Vector",R.drawable.music_bg));
        try {
            sNavigationDrawer.setMenuItemList(menuItems);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        sNavigationDrawer.setOnMenuItemClickListener(position -> {
//
//            String typeToLoad;
//
//            switch (position) {
//                case 1:
//                    typeToLoad = "photos";
//                    break;
//                case 2:
//                    typeToLoad = "illustration";
//                    break;
//                case 3:
//                    typeToLoad = "vector";
//                    break;
//                default:
//                    typeToLoad = "all";
//                    break;
//
//            }
//
//            Bundle args = new Bundle();
//            args.putString("type", typeToLoad);
//
//            ImageListFragment fragment = new ImageListFragment();
//            fragment.setArguments(args);
//
//            navigateTo(fragment, false);;
//
//
//        });


    }




    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
               .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.img_fragment_container, fragment);

       if (addToBackStack) transaction.addToBackStack(NavigationHost.TAG);

       transaction.commit();
    }




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isChanged", true);

    }

    public SNavigationDrawer getsNavigationDrawer() {
        return sNavigationDrawer;
    }

}
