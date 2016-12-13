package com.example.yasuaki.movieseeker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.yasuaki.movieseeker.R;

//Contains useful utilities for a movie seeker app
public class ActivityUtils {

    //Get sort order preference
    public static String getPreferredSortOrder(Context context) {

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        String prefSortOrder = sharedPreferences.getString(
                context.getString(R.string.pref_sort_key),
                context.getString(R.string.sort_order_popularity));

        return prefSortOrder;
    }
}
