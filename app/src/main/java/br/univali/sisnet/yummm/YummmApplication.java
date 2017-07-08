package br.univali.sisnet.yummm;

import android.app.Application;

import io.realm.Realm;

public class YummmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
