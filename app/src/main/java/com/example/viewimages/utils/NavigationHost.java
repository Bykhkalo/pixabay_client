package com.example.viewimages.utils;

import androidx.fragment.app.Fragment;

public interface NavigationHost {

    String TAG = "tag";

    void navigateTo(Fragment fragment, boolean addToBackStack);

}
