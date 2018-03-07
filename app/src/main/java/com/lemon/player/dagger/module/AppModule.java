package com.lemon.player.dagger.module;

import android.content.Context;

import com.lemon.player.LemonApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private LemonApplication application;

    public AppModule(LemonApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

}