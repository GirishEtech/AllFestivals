package com.greeting.greet_app;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    Context context;
    public ConnectionDetector(Context context){
        this.context=context;
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;

    }

}
