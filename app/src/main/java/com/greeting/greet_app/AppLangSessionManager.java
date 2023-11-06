package com.greeting.greet_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppLangSessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "AppLangPref";
    private static final String PREFER_NAME_First = "AppPref";


    public static final String KEY_APP_LANGUAGE = "en";
    public static final String KEY_APP = "en";

    @SuppressLint("CommitPrefEdits")
    public AppLangSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }




    public void setFirst(String lang) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREFER_NAME_First, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(KEY_APP,lang).commit();
    }
    public String getFirst() {
        return _context.getSharedPreferences(PREFER_NAME_First,Context.MODE_PRIVATE).getString(KEY_APP, "true");
    }
    public void setLanguage(String lang) {
        editor.putString(KEY_APP_LANGUAGE,lang);
        editor.commit();
    }

    public void setLanguage1(String lang, Activity activity) {
        pref = activity.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(KEY_APP_LANGUAGE,lang);
        editor.apply();
    }




    public String getLanguage() {
        return pref.getString(KEY_APP_LANGUAGE, "en");
    }








}

