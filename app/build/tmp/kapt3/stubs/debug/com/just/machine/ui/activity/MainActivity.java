package com.just.machine.ui.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u000f2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\n\u001a\u00020\u0002H\u0014J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\b\u0010\u000e\u001a\u00020\fH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/just/machine/ui/activity/MainActivity;", "Lcom/common/base/CommonBaseActivity;", "Lcom/just/news/databinding/ActivityMainBinding;", "()V", "connectThread", "Lcom/just/machine/util/ConnectThread;", "handler", "Landroid/os/Handler;", "listenerThread", "Lcom/just/machine/util/ListenerThread;", "getViewBinding", "initNavigationView", "", "initSocket", "initView", "Companion", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint
public final class MainActivity extends com.common.base.CommonBaseActivity<com.just.news.databinding.ActivityMainBinding> {
    private com.just.machine.util.ListenerThread listenerThread;
    private com.just.machine.util.ConnectThread connectThread;
    private final android.os.Handler handler = null;
    @org.jetbrains.annotations.NotNull
    public static final com.just.machine.ui.activity.MainActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override
    protected void initView() {
    }
    
    private final void initSocket() {
    }
    
    private final void initNavigationView() {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    protected com.just.news.databinding.ActivityMainBinding getViewBinding() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/just/machine/ui/activity/MainActivity$Companion;", "", "()V", "startMainActivity", "", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * @param context -跳转主界面
         */
        public final void startMainActivity(@org.jetbrains.annotations.Nullable
        android.content.Context context) {
        }
    }
}