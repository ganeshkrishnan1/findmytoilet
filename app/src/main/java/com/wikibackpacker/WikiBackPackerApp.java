package com.wikibackpacker;

import android.app.Application;
import android.content.Context;

import com.tapreason.sdk.TapReason;

import java.lang.ref.WeakReference;

/**
 * Created by spyder on 24/06/16.
 */
public class WikiBackPackerApp extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        TapReason.init( "169517018E9655D56D0671216BA0C8AA",
        "VhqCQLG43YDe9eSC",
        new WeakReference<Context>( getApplicationContext() ), "WikiBackPacker");
        TapReason.getConf().enableLogging( true );
    }
}
